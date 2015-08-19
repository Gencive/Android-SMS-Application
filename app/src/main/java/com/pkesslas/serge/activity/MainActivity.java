package com.pkesslas.serge.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkesslas.serge.R;
import com.pkesslas.serge.Serge;
import com.pkesslas.serge.adapter.ListContactAdapter;
import com.pkesslas.serge.model.Contact;

import java.util.Calendar;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

	private TextView btnAdd;
	private ListView listView;
	private Toolbar toolbar;
	private Calendar date = null;
	private Boolean showDate = true;

	private static final int RELOAD_CODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);

		listView = (ListView) findViewById(R.id.list_contact);

		buildListContact();

		btnAdd = (TextView) findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(this);

	}

	private void buildListContact() {
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setBackgroundColor(Color.parseColor(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("colorType", "#009330")));
		}
		Serge.mySQLiteManager().open();
		listView.setAdapter(new ListContactAdapter(this, Serge.mySQLiteManager().getAllContacts()));
		Serge.mySQLiteManager().close();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getApplication(), ShowContactActivity.class);
				String _id = Long.toString(((Contact) listView.getItemAtPosition(position)).getId());
				intent.putExtra("id", _id);
				showDate = false;
				startActivity(intent);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			showDate = false;
			startActivity(new Intent(this, PreferencesActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_add) {
			showDate = false;
			startActivityForResult(new Intent(this, AddContactActivity.class), RELOAD_CODE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (date != null && showDate) {
			Toast.makeText(this, date.getTime().toString(), Toast.LENGTH_SHORT).show();
		}
		showDate = true;
		buildListContact();
	}

	@Override
	protected void onStop() {
		super.onPause();

		date = Calendar.getInstance();
		Log.d("Date ", date.getTime().toString());
	}
}
