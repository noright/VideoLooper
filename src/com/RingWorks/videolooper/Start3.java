package com.RingWorks.videolooper;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Start3 extends Activity {
	
	boolean isRestart = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		Intent intent2 = new Intent(Start3.this, Start2.class);
		startActivityForResult(intent2, 0);
		System.out.println("start3");
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent intent) {
		System.out.println("resultCode: " + resultCode);
		switch(resultCode) {
		case 0x01:
			isRestart = true;
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		HideStatusBar.enable();
		if (isRestart) {
			Intent intent2 = new Intent(Start3.this, Start2.class);
			intent2.putExtra("startState", 0x02);
			startActivity(intent2);
			
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					finish();
				}
			};
			
			new Timer().schedule(timerTask, 5000);
			System.out.println("add timer");
		}
	}	
}
