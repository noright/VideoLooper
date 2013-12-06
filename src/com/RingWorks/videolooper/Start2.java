package com.RingWorks.videolooper;

import com.RingWorks.videolooper.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
//import android.os.SystemProperties;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.VideoView;

public class Start2 extends Activity {
	
	private RelativeLayout start;
	VideoView videoView ;
	int startState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		System.out.println("start2");
		setContentView(R.layout.loading);		
		start = (RelativeLayout) findViewById(R.id.start);
		startState = getIntent().getIntExtra("startState", 0x01);
		if (startState == 0x01) {
			start.removeAllViews();
			videoView = new VideoView(Start2.this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			videoView.setLayoutParams(params);
			start.addView(videoView);
			String uri = "android.resource://" + getPackageName() + "/" + R.raw.black;				
			videoView.setVideoPath(uri);
			videoView.setSystemUiVisibility(0);
			
			videoView.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					videoView.start();
					videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
			                View.SYSTEM_UI_FLAG_LOW_PROFILE);
				}
			});
			
			videoView.setOnCompletionListener(new OnCompletionListener() {	
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					start.removeAllViews();
					System.out.println("play end");
					Intent intent = new Intent(Start2.this, Start3.class);
					setResult(0x01, intent);
					finish();
				}
			});
			
		} else if (startState == 0x02) {
			
			start.removeAllViews();
			videoView = new VideoView(Start2.this);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			videoView.setLayoutParams(params);
			start.addView(videoView);
			String uri = "android.resource://" + getPackageName() + "/" + R.raw.loading;				
			videoView.setVideoPath(uri);
			videoView.setSystemUiVisibility(0);
			
			videoView.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					videoView.start();
					videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
			                View.SYSTEM_UI_FLAG_LOW_PROFILE);
				}
			});
			
			videoView.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					start.removeAllViews();
					finish();
					Intent intent = new Intent(Start2.this, Start.class);
					startActivity(intent);
				}
			});
			
		}
	}
}
