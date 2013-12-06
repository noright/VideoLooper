package com.RingWorks.videolooper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootUpReceiver extends BroadcastReceiver {
	
	SharedPreferences settings;
	public void onReceive(Context context, Intent intent) {
		settings=context.getSharedPreferences("VideoLoop", Context.MODE_PRIVATE);
		System.out.println("cpucpu"+settings.getBoolean("boot", true));
		if(settings.getBoolean("boot", true)){
			Intent i = new Intent(context, Start3.class);
			i.putExtra("DelayStart", true);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}
	
}