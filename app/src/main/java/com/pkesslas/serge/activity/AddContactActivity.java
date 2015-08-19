package com.pkesslas.serge.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkesslas.serge.R;
import com.pkesslas.serge.Serge;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddContactActivity extends ActionBarActivity implements View.OnClickListener {

	private ImageView addPicture;
	private TextView save;
	private EditText lastName, name, number, mail, address;
	private Uri selectedPhoto = Uri.parse("null");

	private static final int SELECT_PHOTO = 2;
	private static final int REQUEST_IMAGE_CAPTURE = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setBackgroundColor(Color.parseColor(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("colorType", "#009330")));
		}

		addPicture = (ImageView) findViewById(R.id.photo);
		lastName = (EditText) findViewById(R.id.lastname);
		name = (EditText) findViewById(R.id.name);
		number = (EditText) findViewById(R.id.phone_number);
		mail = (EditText) findViewById(R.id.mail);
		address = (EditText) findViewById(R.id.adresse);
		save = (TextView) findViewById(R.id.save);

		addPicture.setOnClickListener(this);
		save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.photo) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.choose_picture_dialog)
					.setItems(R.array.select_picture, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (which == 0) {
								Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
								photoPickerIntent.setType("image/*");
								startActivityForResult(photoPickerIntent, SELECT_PHOTO)	;
							} else if (which == 1) {
								Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
									startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
								}
							}
						}
					});
			builder.create().show();
		} else if (id == R.id.save) {
			if (lastName.getText().length() == 0) {
				lastName.setError(getResources().getString(R.string.empty_field_error));
			} if (name.getText().length() == 0) {
				name.setError(getResources().getString(R.string.empty_field_error));
			} if (number.getText().length() == 0) {
				number.setError(getResources().getString(R.string.empty_field_error));
			} if (mail.getText().length() == 0) {
				mail.setError(getResources().getString(R.string.empty_field_error));
			} if (address.getText().length() == 0) {
				address.setError(getResources().getString(R.string.empty_field_error));
			} else {
				String num = number.getText().toString();
				if (num.startsWith("0")) {
					num = num.replaceFirst("0", "+33");
				}
				Serge.mySQLiteManager().open();
				Serge.mySQLiteManager().createContact(
						lastName.getText().toString(),
						name.getText().toString(),
						num,
						mail.getText().toString(),
						address.getText().toString(),
						selectedPhoto.toString()
				);
				Serge.mySQLiteManager().close();
				Toast.makeText(this, getResources().getString(R.string.contact_saved), Toast.LENGTH_LONG).show();

				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);
				finish();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		if (requestCode == SELECT_PHOTO) {
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				selectedPhoto = selectedImage;
				try {
					InputStream imageStream = getContentResolver().openInputStream(selectedImage);
					Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
					addPicture.setImageBitmap(yourSelectedImage);
				} catch (IOException e) {
					Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
				}
			}
		} else if (requestCode == REQUEST_IMAGE_CAPTURE) {
			if (resultCode == RESULT_OK) {
				Bundle extras = imageReturnedIntent.getExtras();
				Bitmap imageBitmap = (Bitmap) extras.get("data");
				addPicture.setImageBitmap(imageBitmap);
				saveBitmapToFile(imageBitmap);
			}
		}
	}

	public void saveBitmapToFile(Bitmap bitmap) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File f = new File(Environment.getExternalStorageDirectory() + File.separator + name.getText().toString() + " " + timestamp+ ".jpg");
		try {
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());

			fo.close();
			selectedPhoto = Uri.fromFile(f);
		} catch (IOException ignored) {}
	}
}
