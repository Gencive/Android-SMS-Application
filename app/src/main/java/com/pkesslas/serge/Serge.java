package com.pkesslas.serge;

import android.app.Application;
import android.content.Context;

import com.pkesslas.serge.helper.MySQLiteManager;

/**
 * Created by Pierre-Elie on 02/03/15.
 */
public class Serge extends Application {

	private static Context context;
	private static MySQLiteManager mySQLiteManager;

	@Override
	public void onCreate() {
		Serge.context = this;
	}

	public static MySQLiteManager mySQLiteManager() {
		if (mySQLiteManager != null) {
			return mySQLiteManager;
		}
		mySQLiteManager = new MySQLiteManager(Serge.context);
		return mySQLiteManager;
	}
}
