package com.RingWorks.videolooper;

import com.zunidata.zunidataapi.ZunidataEnvironment;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class StorageService extends Service {

	ActionReceiver actionReceiver;
	private static final String TAG = "VideoLooper";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {		
		if (actionReceiver == null) {
			actionReceiver = new ActionReceiver();
			actionReceiver.registerScreenActionReceiver(getApplicationContext());
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		if (actionReceiver != null) {
			actionReceiver.unRegisterScreenActionReceiver(getApplicationContext());
			actionReceiver = null;
		}
		super.onDestroy();
	}
	
	class ActionReceiver extends BroadcastReceiver {
    	private boolean isRegisterReceiver = false;
		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			String evPath = ZunidataEnvironment.getExternalStoragePath();
			Intent i = new Intent();
			
			if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
				Log.i(TAG, "Received " + action);
				
				if (!intent.getData().getPath().contains("flash")) {
				
					if (intent.getData().getPath().equals(evPath)) {
						Log.i(TAG, "SD Card Mounted");
						i.setAction(FinalData.Action.START_PLAY);
					} else {
						Log.i(TAG, "U-Disk Mounted");
						i.setAction(FinalData.Action.UPDATE_CONTENT);
						i.putExtra("UdPath", intent.getData().getPath());
					}
				}

				
			} else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
				Log.i(TAG, "Received " + action);	
				
				if (intent.getData().getPath().equals(evPath)) {
					Log.i(TAG, "SD Removed");
					i.setAction(FinalData.Action.STOP_PLAY);
				} else {
					Log.i(TAG, "U-Disk Removed");
					i.setAction(FinalData.Action.START_PLAY);
					i.putExtra("UdPath", intent.getData().getPath());
				}
				
			} 
			
			sendBroadcast(i);
			
		}
    	
		public void registerScreenActionReceiver(Context context) {
			if (!isRegisterReceiver) {
				isRegisterReceiver = true;
				IntentFilter filter = new IntentFilter();
				filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
				//filter.addAction(Intent.ACTION_MEDIA_REMOVED);
				filter.addAction(Intent.ACTION_MEDIA_EJECT);
				filter.addDataScheme("file");
				context.registerReceiver(ActionReceiver.this, filter);
			}
		}

		public void unRegisterScreenActionReceiver(Context context) {
			if (isRegisterReceiver) {
				isRegisterReceiver = false;
				context.unregisterReceiver(ActionReceiver.this);
			}
		}
    }

}
