package com.pkesslas.serge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pkesslas.serge.model.Message;
import com.pkesslas.serge.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Pierre-Elie on 17/02/15.
 */
public class ListChatAdapter extends ArrayAdapter<Message> {
	private final Context context;
	private ArrayList<Message> messages;

	private TextView name, date, text;

	public ListChatAdapter(Context context, ArrayList<Message> message) {
		super(context, R.layout.list_chat_elem_receiv, message);
		this.context = context;
		this.messages = message;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView;
		Calendar calendar = Calendar.getInstance();
		Message message = messages.get(position);

		if (message.getAuthor().equals(getContext().getResources().getString(R.string.me))) {
			rowView = inflater.inflate(R.layout.list_chat_elem_send, parent, false);
		} else {
			rowView = inflater.inflate(R.layout.list_chat_elem_receiv, parent, false);
		}

		text = (TextView) rowView.findViewById(R.id.message);
		date = (TextView) rowView.findViewById(R.id.date);
		name = (TextView) rowView.findViewById(R.id.name);
		calendar.setTimeInMillis(Long.parseLong(message.getDate()));
		text.setText(message.getMessage());
		name.setText(message.getAuthor());
		date.setText(calendar.getTime().toString());
		return rowView;
	}
}
