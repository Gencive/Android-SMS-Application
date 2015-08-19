package com.pkesslas.serge.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.pkesslas.serge.helper.MySQLiteHelper;
import com.pkesslas.serge.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pierre-Elie on 02/03/15.
 */
public class MySQLiteManager {
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private Context context;

	private String[] allColumns = {
			MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_LASTNAME,
			MySQLiteHelper.COLUMN_NAME,
			MySQLiteHelper.COLUMN_NUMBER,
			MySQLiteHelper.COLUMN_EMAIL,
			MySQLiteHelper.COLUMN_ADDRESS,
			MySQLiteHelper.COLUMN_PHOTO,
	};

	public MySQLiteManager(Context context) {
		this.context = context;
		dbHelper = new MySQLiteHelper(this.context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Contact createContact(String lastName, String name, String number, String email, String address, String photo) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_LASTNAME, lastName);
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_NUMBER, number);
		values.put(MySQLiteHelper.COLUMN_EMAIL, email);
		values.put(MySQLiteHelper.COLUMN_ADDRESS, address);
		values.put(MySQLiteHelper.COLUMN_PHOTO, photo);


		long insertId = database.insert(MySQLiteHelper.TABLE_CONTACT, null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACT,
				allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Contact newComment = cursorToContact(cursor);
		cursor.close();
		return newComment;
	}

	public void deleteContact(Contact contact) {
		long id = contact.getId();
		database.delete(MySQLiteHelper.TABLE_CONTACT, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Contact> getAllContacts() {
		List<Contact> contacts = new ArrayList<Contact>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACT,
				allColumns, null, null, null, null, "LOWER(name) asc");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Contact contact = cursorToContact(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}
		cursor.close();
		return contacts;
	}

	public void updateContact(Contact contact) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(MySQLiteHelper.COLUMN_ID, contact.getId());
		contentValues.put(MySQLiteHelper.COLUMN_LASTNAME, contact.getLastName());
		contentValues.put(MySQLiteHelper.COLUMN_NAME, contact.getName());
		contentValues.put(MySQLiteHelper.COLUMN_NUMBER, contact.getNumber());
		contentValues.put(MySQLiteHelper.COLUMN_ADDRESS, contact.getAdress());
		contentValues.put(MySQLiteHelper.COLUMN_EMAIL, contact.getEmail());
		contentValues.put(MySQLiteHelper.COLUMN_PHOTO, contact.getPhoto());
		database.update(MySQLiteHelper.TABLE_CONTACT, contentValues, MySQLiteHelper.COLUMN_ID + "=" + Long.toString(contact.getId()), null);
	}

	public Contact getContactFromId(int id) {
		Cursor cursor = database.rawQuery(
				"SELECT " + "*"
				+ " FROM " + MySQLiteHelper.TABLE_CONTACT
				+ " WHERE " + MySQLiteHelper.COLUMN_ID + " = '" + Integer.toString(id) + "'",
				null);
		return cursorToContact(cursor);
	}

	public Contact getContactFromId(String id) {
		Cursor cursor = database.rawQuery(
				"SELECT " + "*"
				+ " FROM " + MySQLiteHelper.TABLE_CONTACT
				+ " WHERE " + MySQLiteHelper.COLUMN_ID + " = '" + id + "'",
				null);
		cursor.moveToFirst();
		return cursorToContact(cursor);
	}

	public boolean isContactExistByNumber(String number) {
		Cursor cursor = database.rawQuery(
				"SELECT " + "*"
						+ " FROM " + MySQLiteHelper.TABLE_CONTACT
						+ " WHERE " + MySQLiteHelper.COLUMN_NUMBER + " = '" + number + "'",
				null);
		cursor.moveToFirst();
		if (cursor.getCount() == 0) {
			cursor.close();
			return false;
		}
		cursor.close();
		return true;
	}

	private Contact cursorToContact(Cursor cursor) {
		Contact contact = new Contact();
		contact.setId(cursor.getLong(0));
		contact.setLastName(cursor.getString(1));
		contact.setName(cursor.getString(2));
		contact.setNumber(cursor.getString(3));
		contact.setAdress(cursor.getString(4));
		contact.setEmail(cursor.getString(5));
		contact.setPhoto(cursor.getString(6));
		return contact;
	}
}
