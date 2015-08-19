package com.pkesslas.serge.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.pkesslas.serge.Serge;
import com.pkesslas.serge.activity.ChatActivity;
import com.pkesslas.serge.model.Message;

import java.util.Calendar;

/**
 * Created by Pierre-Elie on 04/03/15.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

	public static final String SMS_BUNDLE = "pdus";

	public void onReceive(Context context, Intent intent) {
		Serge.mySQLiteManager().open();
		Bundle intentExtras = intent.getExtras();
		if (intentExtras != null) {
			Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
			Message message = null;
			String address = "";

			for (int i = 0; i < sms.length; ++i) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

				String smsBody = smsMessage.getMessageBody();
				address = smsMessage.getOriginatingAddress();

				message = new Message(smsBody, Long.toString(Calendar.getInstance().getTimeInMillis()), address);
			}

			ChatActivity inst = ChatActivity.getInstance();
			if (!Serge.mySQLiteManager().isContactExistByNumber(address)) {
				Serge.mySQLiteManager().createContact("", address, address, "", "", "null");

			}
			if (inst != null) {
				inst.updateList(message, address);
			}
		}
		Serge.mySQLiteManager().close();
	}
}