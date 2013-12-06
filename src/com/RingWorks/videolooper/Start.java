package com.RingWorks.videolooper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import com.RingWorks.videolooper.R;
import com.zunidata.zunidataapi.ZunidataEnvironment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.VideoView;

public class Start extends Activity {
	
	private RelativeLayout start;
	private TextView mTextView;
	VideoView videoView ;
	
	private static final String TAG = "VideoLooper";	
	private static final String[] modelName = { "7UT", "10UT", "10N", "19N", 
		"6-APPC", "7-APPC", "10-APPC", "FMT-7AT", "FMT-10AT", "7RT", "MB211", "MB222" };
	private boolean isZuniModel = false;
	
	private boolean delayStart = false;
	
	public static String contentName = "CONTENT";
	public static boolean isPlayImage = true;
	public static boolean isImageStretch = false;
	public static int imagePlayInterval = 3000;
	public static boolean isSetWatchDog = false;
	public static int restartDuration = 6;
	public static boolean isUpdate = false;
	public static String updatePath = null;

	
	private int LONG_PRESS_TIME = 5000;
	private Timer timer;
	
	int playIndex = -1;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0x01:
					startPlayer();
					break;
				default:
					break;
			}
		}
	};

	public void startPlayer() {

		Intent intent = new Intent(Start.this, Player.class);
		intent.putExtra("contentName", contentName);
		intent.putExtra("isPlayImage", isPlayImage);
		intent.putExtra("isImageStretch", isImageStretch);
		intent.putExtra("imagePlayInterval", imagePlayInterval);
		if (isUpdate) {
			intent.putExtra("isUpdate", isUpdate);
			intent.putExtra("updatePath", updatePath);
			isUpdate = false;
		}

		overridePendingTransition(0, 0);
		startActivity(intent); 
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		ExitApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.loading);		
		start = (RelativeLayout) findViewById(R.id.start);
		
		mTextView = new TextView(getApplicationContext());
		RelativeLayout.LayoutParams txtView = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	mTextView.setLayoutParams(txtView); 
    	mTextView.setTextSize(30);
    	mTextView.setTextColor(Color.WHITE);
    	mTextView.setGravity(Gravity.CENTER);
    	mTextView.setText(R.string.loading);
//    	
		int c;
		for (c = 0; c < modelName.length; c++) {
			if (android.os.Build.MODEL.contains(modelName[c]))
				break;
		}
		
		if (c == modelName.length) {
			mTextView.setText("Your device is not supported.");
			start.addView(mTextView);		
		} else {
			isZuniModel = true;
			
			if (getIntent() != null) {
				 if (getIntent().hasExtra("DelayStart")) {
					 delayStart = getIntent().getBooleanExtra("DelayStart", false);
				 }
			}
			
			File m = new File("/mnt");
			File[] mf = m.listFiles();
			String mp = null;
			if (mf.length != 0) {
				for (int i = 0; i < mf.length; i++) {
					if (mf[i].toString().contains("sd")
							&& !mf[i].toString().equals("/mnt/sdcard")
							&& !mf[i].toString().equals("/mnt/sdcard/external_sdcard")
							&& mf[i].canWrite()) {
						mp = mf[i].toString() + "/";
						break;
					}
				}
			}
			
			if (mp != null) {
				isUpdate = true;
				updatePath = mp;
			} 
			startService(new Intent(Start.this, StorageService.class));
		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		HideStatusBar.enable();
			readyTostartPlayer();
	}

	protected void readyTostartPlayer() {		
		if (isZuniModel) {
			getConfig();
			Log.i(TAG, "isUpdate = " + isUpdate);
			if (delayStart) {
				new Thread() {
					public void run() {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								
							}
						mHandler.sendEmptyMessage(0x01);
						delayStart = false;
					}
				}.start();
			} else {
				mHandler.sendEmptyMessage(0x01);
			}
			
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Intent intent = new Intent(Start.this, StorageService.class);
		stopService(intent);
	}
	
 	@Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	switch(event.getAction()) {
    		case MotionEvent.ACTION_DOWN:
		    	Log.i(TAG, "Touch Down");
				if (event.getX() <= 50 && event.getY() <= 50) {
					timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
						    Log.i(TAG, "Long-press catched, apk will shutdown.");
							Start.this.finish();
							HideStatusBar.disable();
						}
					}, LONG_PRESS_TIME);
		    	}
    			break;
    			
    		case MotionEvent.ACTION_UP:
		    	Log.i(TAG, "Touch Up");
				if (timer != null) {
					timer.cancel();
				}
    			break;
    			
    		case MotionEvent.ACTION_MOVE:
    			Log.i(TAG, "Touch Move");
    			if (timer != null && (event.getX() > 50 || event.getY() > 50)) {
    				timer.cancel();
    			}
    			break;
    	}
    	return true;
    }

    
	@Override
	public void onBackPressed() { 

	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
    }
    
   public static String getFilePath() {
		String path = null;
		if (ZunidataEnvironment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
				|| ZunidataEnvironment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			path = ZunidataEnvironment.getExternalStoragePath() + "/" + contentName + "/";
			if (new File(path).exists() && new File(path).canRead() && new File(path).isDirectory())
				return path;
		}
		return null;
	}
	

	private void getConfig() {
		String sdPath = ZunidataEnvironment.getExternalStoragePath();
		File sdFile = new File(sdPath);
		File configFile = new File(sdPath +"/.config.txt");
		String [] default_config = {
			"content_name = " + contentName,
			"play_image = " + isPlayImage,
			"image_stretch = " + isImageStretch,
			"image_interval = " + imagePlayInterval,
		};
		
		if (!sdFile.exists() || !sdFile.canWrite()) {
			return;
		}

		if (!configFile.exists()) {
			Log.i(TAG, "config.txt is not exist. create one.");
			try {
				FileOutputStream os = new FileOutputStream(configFile);
				for (int i = 0; i<default_config.length; i++) {
					os.write((default_config[i]).getBytes());
					os.write("\r\n".getBytes());
				}
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try{
				FileInputStream is = new FileInputStream(configFile);
				byte [] buffer = new byte[is.available()];
				is.read(buffer);
				String configAll = new String(buffer);
				String [] config  = configAll.split("\n");
				String [] config_name = new String[config.length];
				String [] config_value = new String[config.length];

				for (int i = 0; i < config.length; i++) {
					config_name[i] = config[i];
					config_value[i] = config[i];
					
					if (config_name[i].contains(" ")) {
						config_name[i] = config_name[i].substring(0, config_name[i].indexOf(" "));
					}
					
					if (config_value[i].contains("=") && 
							((config_value[i].length() - 1) > (config_value[i].indexOf("=") + 2))) {
						
						config_value[i] = config_value[i].substring(config_value[i].indexOf("=") + 2, 
								config_value[i].length() - 1);
					}
					
					if (config_name[i].equalsIgnoreCase("content_name")) {
						contentName = config_value[i];
					} else if (config_name[i].equalsIgnoreCase("play_image")) {
						isPlayImage = Boolean.parseBoolean(config_value[i]);
					} else if (config_name[i].equalsIgnoreCase("image_stretch")) {
						isImageStretch = Boolean.parseBoolean(config_value[i]);
					} else if (config_name[i].equalsIgnoreCase("image_interval")) {
						imagePlayInterval = Integer.parseInt(config_value[i]);
					}
				}
				
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
}

