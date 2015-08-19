package com.pkesslas.serge.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Pierre-Elie on 02/03/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "serge.db";
	public static final String TABLE_CONTACT = "contacts";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LASTNAME = "lastname";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_ADDRESS = "address";
	public static final String COLUMN_PHOTO = "photo";

	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table " + TABLE_CONTACT + "("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_LASTNAME + " text not null, "
			+ COLUMN_NAME + " text not null, "
			+ COLUMN_NUMBER + " text not null, "
			+ COLUMN_ADDRESS + " text not null, "
			+ COLUMN_EMAIL + " text not null, "
			+ COLUMN_PHOTO + " text);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
		onCreate(db);
	}
}
