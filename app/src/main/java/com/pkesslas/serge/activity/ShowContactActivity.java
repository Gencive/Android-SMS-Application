package com.pkesslas.serge.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkesslas.serge.R;
import com.pkesslas.serge.Serge;
import com.pkesslas.serge.activity.ChatActivity;
import com.pkesslas.serge.activity.EditContactActivity;
import com.pkesslas.serge.model.Contact;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class ShowContactActivity extends ActionBarActivity implements View.OnClickListener {
	private TextView chat, edit, delete, call, name, lastName, number, adress, email;
	private String id;
	private ImageView photo;
	private Contact contact;

	private static int RELOAD_CODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_contact);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setBackgroundColor(Color.parseColor(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("colorType", "#009330")));
		}

		Intent mIntent = getIntent();
		id = mIntent.getStringExtra("id");

		chat = (TextView) findViewById(R.id.chat);
		edit = (TextView) findViewById(R.id.edit);
		delete = (TextView) findViewById(R.id.delete);
		call = (TextView) findViewById(R.id.call);
		chat.setOnClickListener(this);
		call.setOnClickListener(this);
		edit.setOnClickListener(this);
		delete.setOnClickListener(this);

		name = (TextView) findViewById(R.id.name);
		lastName = (TextView) findViewById(R.id.lastname);
		number = (TextView) findViewById(R.id.phone_number);
		adress = (TextView) findViewById(R.id.adresse);
		email = (TextView) findViewById(R.id.mail);

		buildContact();

		setTitle(contact.getLastName() + " " + contact.getName());
	}

	private void buildContact() {
		Serge.mySQLiteManager().open();
		contact = Serge.mySQLiteManager().getContactFromId(id);
		Serge.mySQLiteManager().close();

		lastName.setText(contact.getLastName());
		name.setText(contact.getName());
		number.setText(contact.getNumber());
		adress.setText(contact.getAdress());
		email.setText(contact.getEmail());

		buildPicture();
	}

	private void buildPicture() {
		photo = (ImageView) findViewById(R.id.photo);
		if (!contact.getPhoto().equals("null")) {
			Uri uri = Uri.parse(contact.getPhoto());
			try {
				InputStream imageStream = this.getContentResolver().openInputStream(uri);
				Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
				photo.setImageBitmap(yourSelectedImage);
			} catch (FileNotFoundException ignored) {}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.edit) {
			Intent intent = new Intent(this, EditContactActivity.class);
			intent.putExtra("id", this.id);
			startActivityForResult(intent, RELOAD_CODE);
		} else if (id == R.id.delete) {
			Serge.mySQLiteManager().open();
			Serge.mySQLiteManager().deleteContact(contact);
			Serge.mySQLiteManager().close();
			Intent returnIntent = new Intent();
			setResult(RESULT_OK, returnIntent);
			finish();
		} else if (id == R.id.chat) {
			Intent intent = new Intent(this, ChatActivity.class);
			intent.putExtra("id", this.id);
			startActivity(intent);
		} else if (id == R.id.call) {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + contact.getNumber()));
			startActivity(callIntent);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RELOAD_CODE) {
			if (resultCode == RESULT_OK) {
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);
				buildContact();
			}
		}
	}
}
