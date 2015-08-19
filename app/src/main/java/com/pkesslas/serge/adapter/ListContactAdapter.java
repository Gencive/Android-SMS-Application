package com.pkesslas.serge.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pkesslas.serge.R;
import com.pkesslas.serge.model.Contact;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Pierre-Elie on 17/02/15.
 */
public class ListContactAdapter extends ArrayAdapter<Contact> {
	private final Context context;
	private List<Contact> contacts;

	private TextView name, number, id;
	private ImageView photo;

	public ListContactAdapter(Context context, List<Contact> contacts) {
		super(context, R.layout.list_contact_elem, contacts);
		this.context = context;
		this.contacts = contacts;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView;
		rowView = inflater.inflate(R.layout.list_contact_elem, parent, false);

		Contact contact = contacts.get(position);

		name = (TextView) rowView.findViewById(R.id.name);
		id = (TextView) rowView.findViewById(R.id.test);
		number = (TextView) rowView.findViewById(R.id.number);
		photo = (ImageView) rowView.findViewById(R.id.photo);

		name.setText(contact.getName());
		number.setText(contact.getNumber());
		id.setText(Long.toString(contact.getId()));

		if (!contact.getPhoto().equals("null"))  {
			Uri uri = Uri.parse(contact.getPhoto());
			try {
				InputStream imageStream = context.getContentResolver().openInputStream(uri);
				Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
				photo.setImageBitmap(yourSelectedImage);
			} catch (FileNotFoundException ignored) {}
		}
		return rowView;
	}
}
