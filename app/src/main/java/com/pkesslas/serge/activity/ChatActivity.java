package com.pkesslas.serge.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pkesslas.serge.model.Contact;
import com.pkesslas.serge.adapter.ListChatAdapter;
import com.pkesslas.serge.model.Message;
import com.pkesslas.serge.R;
import com.pkesslas.serge.Serge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


public class ChatActivity extends ActionBarActivity implements View.OnClickListener {
	private ArrayList<Message> smsMessagesList = new ArrayList<Message>();
	private static ChatActivity instance;
	private ListChatAdapter chatAdapter;
	private Contact contact;

	private TextView sendButton;
	private EditText sendList;
	private ListView chatList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		instance = this;
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setBackgroundColor(Color.parseColor(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("colorType", "#009330")));
		}

		getContact();

		setTitle(contact.getLastName() + " " + contact.getName());

		sendButton = (TextView) findViewById(R.id.send_button);
		sendList = (EditText) findViewById(R.id.send_text);
		chatList = (ListView) findViewById(R.id.list_message);

		sendButton.setOnClickListener(this);

		buildSmsList();
	}

	private void getContact() {
		Intent mIntent = getIntent();
		String id = mIntent.getStringExtra("id");
		Serge.mySQLiteManager().open();
		contact = Serge.mySQLiteManager().getContactFromId(id);
		Serge.mySQLiteManager().close();
	}

	public static ChatActivity getInstance() {
		return instance;
	}

	public void buildSmsList() {
		ContentResolver contentResolver = getContentResolver();
		Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
		Cursor smsSentCursor = contentResolver.query(Uri.parse("content://sms/sent"), null, null, null, null);

		getSmsWithCursor(smsInboxCursor, contact.getName());
		getSmsWithCursor(smsSentCursor, getResources().getString(R.string.me));

		Collections.sort(smsMessagesList, new Comparator<Message>() {
			@Override
			public int compare(Message lhs, Message rhs) {
				return (int)(Long.parseLong(lhs.getDate()) - Long.parseLong(rhs.getDate()));
			}
		});

		chatAdapter = new ListChatAdapter(this, smsMessagesList);
		chatList.setAdapter(chatAdapter);
	}

	public void getSmsWithCursor(Cursor cursor, String name) {
		int indexBody = cursor.getColumnIndex("body");
		int indexAddress = cursor.getColumnIndex("address");
		int indexDate = cursor.getColumnIndex("date");

		if ((indexBody < 0 || !cursor.moveToFirst())) {
			return;
		}

		do {
			Message message;
			String str = cursor.getString(indexBody);
			if (cursor.getString(indexAddress).equals(contact.getNumber())) {
				message = new Message(str, cursor.getString(indexDate), name);
				smsMessagesList.add(message);
			}
		} while (cursor.moveToNext());
	}

	private boolean contactExist() {
		return true;
	}

	public void updateList(final Message message, String number) {
		if (number.equals(contact.getNumber())) {
			if (!message.getAuthor().equals(getResources().getString(R.string.me))) {
				message.setAuthor(contact.getName());
			}
			chatAdapter.insert(message, chatAdapter.getCount());
			chatAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.send_button && sendList.getText().length() > 0) {
			String toPhoneNumber = contact.getNumber();
			String smsMessage = sendList.getText().toString();
			try {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(toPhoneNumber, null, smsMessage, null, null);
				Message message = new Message(smsMessage, Long.toString(Calendar.getInstance().getTimeInMillis()), getResources().getString(R.string.me));
				updateList(message, contact.getNumber());
			} catch (Exception e) {
				e.printStackTrace();
			}
			sendList.setText("");
		}
	}
}
