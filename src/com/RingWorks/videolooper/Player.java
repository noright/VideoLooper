package com.RingWorks.videolooper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Messenger;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barcode.KeyCodeTable;
import com.example.barcode.Main;
import com.farcore.playerservice.AmPlayer;
import com.zunidata.watchdog.NoWatchDogJni;
import com.zunidata.watchdog.WatchDogJNI;
import com.zunidata.zunidataapi.ZunidataEnvironment;

public class Player extends Activity {
	
    private AmPlayer video = new AmPlayer();
    
    private String res="";
    private KeyCodeTable kt=null;
	private static final String TAG = "VideoLooper";
	private HandlerThread timeWatchDog;
	private Handler timeWatchDogHandler;
	private long watchDogTimeDelay = 20 * 1000;
	WatchDogJNI watchdogjni;
	boolean watchdogEnableOK;

	private String contentName = "CONTENT";
	private boolean isPlayImage;
	private boolean isImageStretch;
	private int imagePlayInterval;
//	private boolean isSetWatchDog;
//	private int restartDuration;
	private boolean isUpdate;
	private String updatePath;
	
//	private int downCount;
//	private long firstDownTime;
//	private long lastDownTime;
	private boolean isPlayVideo = false;
//	private int closeCount;
	
	private RelativeLayout main;
//	private MyVideoView mVideoView;
	private ImageView mImageView;
	private TextView mTextView;
	private TextView mToastView;
	private Toast toast;
	private int toastCount;

	private ActionReceiver actionReceiver;
	private String sdPath;
	private String contentPath;
	private String udPath;

	private boolean updateMediaOK = false;
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private int LONG_PRESS_TIME = 3000;
	private Timer timer;
	private Timer ti;
	private Timer td;
	boolean wantStop = false;
	private boolean again=false;
	private final static int MEDIA_TYPE_VIDEO = 0x01;
	private final static int MEDIA_TYPE_IMAGE = 0x02;
	private final static int REQUEST_CLOSE = 0x03;
	private final static int REQUEST_RESTART = 0x04;
	private List<String> playList;
	private int playIndex = 0;
	private boolean isSecondTime = false;
	private boolean isFirstTime = true;
	private int nullFile = 0;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x01:
				BitmapDrawable drawable = (BitmapDrawable) mImageView
					.getDrawable();
				Bitmap bm = drawable.getBitmap();
				if (null != bm && !bm.isRecycled()) {
					bm.recycle();
					bm = null;
				}
				mImageView.setVisibility(View.GONE);
				video.Close();
				playMedia();
				break;
				
			case 0x02:
				video.Close();
				mTextView.setText(R.string.loading);
				break;
				
			case 0x03:
				video.Close();
				mTextView.setText(R.string.sdcard_not_found);
				break;
				
			case 0x04:
				video.Close();
				mTextView.setText(R.string.updatemedia);
				break;
				
			case 0x05:
				video.Close();
				mTextView.setText(R.string.udremove);
				break;
				
			case 0x06:
				video.Close();
				mTextView.setText(R.string.updatemedia_fail);
				break;
				
			case 0x07:
				video.Close();
				mTextView.setText(R.string.no_media);
				break;
			}
			
			if (msg.what != 0x01 && msg.what != 0x08 ) {

				video.Close();
				mImageView.setVisibility(View.GONE);
				mTextView.setVisibility(View.VISIBLE);
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		ExitApplication.getInstance().addActivity(this);

		setContentView(R.layout.activity_player);
		main = (RelativeLayout) findViewById(R.id.main);
		main.setGravity(Gravity.CENTER);
		
		mImageView = new ImageView(getApplicationContext());
		RelativeLayout.LayoutParams imgView = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mImageView.setLayoutParams(imgView);
		main.addView(mImageView);
		mImageView.setVisibility(View.GONE);

		mTextView = new TextView(getApplicationContext());
		RelativeLayout.LayoutParams txtView = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mTextView.setLayoutParams(txtView);
		mTextView.setTextSize(30);
		mTextView.setTextColor(Color.WHITE);
		mTextView.setGravity(Gravity.CENTER);
		main.addView(mTextView);
		mTextView.setVisibility(View.GONE);
		
		mToastView = new TextView(getApplicationContext());
		RelativeLayout.LayoutParams tstView = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mToastView.setLayoutParams(tstView);
		mToastView.setText(R.string.no_update);
		mToastView.setTextSize(24);
		mToastView.setTextColor(Color.RED);
		mToastView.setBackgroundColor(Color.DKGRAY);
		mToastView.setHeight(50);
		mToastView.getBackground().setAlpha(180);
		mToastView.setGravity(Gravity.CENTER);
		kt=new KeyCodeTable();
		if (getIntent() != null) {
			Log.i(TAG, "Get Config From File:");
			if (getIntent().hasExtra("contentName")) {
				contentName = getIntent().getStringExtra("contentName");
				Log.i(TAG, "contentName = " + contentName);
			}
			if (getIntent().hasExtra("isPlayImage")) {
				isPlayImage = getIntent().getBooleanExtra("isPlayImage", false);
				Log.i(TAG, "isPlayImage = " + isPlayImage);
			}
			if (getIntent().hasExtra("isImageStretch")) {
				isImageStretch = getIntent().getBooleanExtra("isImageStretch",
						false);
				Log.i(TAG, "isImageStretch = " + isImageStretch);
			}
			if (getIntent().hasExtra("imagePlayInterval")) {
				imagePlayInterval = getIntent().getIntExtra(
						"imagePlayInterval", 0);
				Log.i(TAG, "imagePlayInterval = " + imagePlayInterval);
			}
			
			if (getIntent().hasExtra("isUpdate")) {
				isUpdate = getIntent().getBooleanExtra("isUpdate", false);
				Log.i(TAG, "isUpdate = " + isUpdate);
			}
			if (getIntent().hasExtra("updatePath")) {
				updatePath = getIntent().getStringExtra("updatePath");
				Log.i(TAG, "updatePath = " + updatePath);
			}
		}

		if (isImageStretch) {
			mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
		}
///////////////////////////////////////////////////////////////////////////////
		watchdogjni = WatchDogJNI.getInstance();
		if(watchdogjni!=null){
		try {
			watchdogjni.enableWD();
		} catch (NoWatchDogJni e) {
			System.out.println("wu");
		}
			timeWatchDog = new HandlerThread("WatchDog");
			timeWatchDog.start();
			timeWatchDogHandler = new Handler(timeWatchDog.getLooper());
			timeWatchDogHandler.post(timeWatchDogRunnable);
		//}
}
		actionReceiver = new ActionReceiver();
		actionReceiver.registerScreenActionReceiver(getApplicationContext());

	}
	Builder mydialog;	
	boolean quit,boot;
	@Override
	protected void onResume() {
		super.onResume();
		settings=this.getSharedPreferences("VideoLoop", MODE_PRIVATE);
		mydialog=new AlertDialog.Builder(this).setTitle("Settings").setPositiveButton("确定", new dialogClick());
		mydialog.setMultiChoiceItems(new String[] {"开机自启动","退出"}, 
				new boolean[] {settings.getBoolean("boot", true),false}, 
				new DialogInterface.OnMultiChoiceClickListener() {			
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				System.out.println("which"+which+"ischeced"+isChecked);
				if(which==0){
					boot=isChecked;
					System.out.println("which"+boot);
				}
				
				if(which==1){
					quit=isChecked;
					System.out.println("which"+quit);
				}
			}
		});
		
		if(again){
			if(isPlayVideo){
				wantStop=false;
				video.showAll();
			}			
			playMedia();
			again=false;
			return;
		}
		sdPath = ZunidataEnvironment.getExternalStoragePath();
		contentPath = sdPath + "/" + contentName + "/";
		Log.i(TAG, "Play Content: " + contentPath);

		if (ZunidataEnvironment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) && sdPath.contains("sdcard")) {

			if (updatePath != null) {
				Log.i(TAG, "U-Disk Insert: " + updatePath);
				int gui = getUserInfo(updatePath + "userinfo.txt");
				if (gui == 2) {
					video.Close();
					mHandler.sendEmptyMessage(0x04);
					updateMedia(updatePath);
				} else if (gui == 1 || gui == 3) {
					if (isUpdate) {
						// Toast.makeText(Player.this, R.string.no_update,
						// Toast.LENGTH_LONG).show();
						toast = new Toast(getApplicationContext());
						toast.setView(mToastView);
						toast.setDuration(Toast.LENGTH_LONG);
						toastDisplay();
					}

					File ct = new File(contentPath);
					if (ct.exists()) {
						if (mkPlayList(contentPath)) {
							// mHandler.sendEmptyMessage(0x02);
							playMedia();
						} else {
							mHandler.sendEmptyMessage(0x07);
						}
					} else {
						mHandler.sendEmptyMessage(0x07);
					}
				}
			} else {
				File ct = new File(contentPath);
				if (ct.exists()) {
					if (mkPlayList(contentPath)) {
						// mHandler.sendEmptyMessage(0x02);
						playMedia();
					} else {
						mHandler.sendEmptyMessage(0x07);
					}
				} else {
					mHandler.sendEmptyMessage(0x07);
				}
			}

		} else {
			mHandler.sendEmptyMessage(0x03);
		}


	}

	private Runnable timeWatchDogRunnable = new Runnable() {
		public void run() {
			Log.w(TAG, "-----------------------------");
			Log.w(TAG, "[DEBUG] watchdog scanned.");
			Log.w(TAG, "-----------------------------");
			
			try {
				watchdogjni.feedWD();
			} catch (NoWatchDogJni e) {
				System.out.println("weidakai");
			}
			
			Calendar cal = Calendar.getInstance();
			int hour = cal.get(Calendar.HOUR_OF_DAY);
			int min = cal.get(Calendar.MINUTE);

			if (timeWatchDogHandler != null) {
				timeWatchDogHandler.postDelayed(this, watchDogTimeDelay);
			}
		}
	};

	
	private boolean mkPlayList(String path) {
		playList = new ArrayList<String>();
		String[] filelist = new File(path).list();

		if (filelist.length == 0) {
			return false;
		}
		
		int j = 0, k = 0;
		String [] list1 = new String[filelist.length];
		String [] list2 = new String[filelist.length];

		if (isPlayImage) {
			for (int i = 0; i < filelist.length; i++) {
				if (getMediaType(filelist[i]) == MEDIA_TYPE_VIDEO
						|| getMediaType(filelist[i]) == MEDIA_TYPE_IMAGE) {
					if ((filelist[i].charAt(0) >= 'a' && filelist[i].charAt(0) <= 'z')
							|| (filelist[i].charAt(0) >= 'A' && filelist[i]
									.charAt(0) <= 'Z')) {
						list1[j++] = filelist[i];
					} else {
						list2[k++] = filelist[i];
					}
				}
			}
		} else {
			for (int i = 0; i < filelist.length; i++) {
				if (getMediaType(filelist[i]) == MEDIA_TYPE_VIDEO) {
					if ((filelist[i].charAt(0) >= 'a' && filelist[0].charAt(0) <= 'z')
							|| (filelist[i].charAt(0) >= 'A' && filelist[0]
									.charAt(0) <= 'Z')) {
						list1[j++] = filelist[i];
					} else {
						list2[k++] = filelist[i];
					}
				}
			}
		}
		
		sortList(list1, j);
		sortList(list2, k);
		
		for (int i = 0; i < j; i++) {
			playList.add(list1[i]);
		}
		
		for (int i = 0; i < k; i++) {
			playList.add(list2[i]);
		}
		
		Log.i(TAG, "Play List: " + playList);

		if (playList.size() == 0) {
			return false;
		}

		return true;

	}

	
	private void sortList(String [] str, int num) {
		String temp = null;
		for (int i = 0; i < num; i++) {
			for (int j = i+1; j < num; j++) {
				if (str[i] != null && str[j] != null) { 
					if (str[i].compareToIgnoreCase(str[j]) > 0) {
						temp = str[i];
						str[i] = str[j];
						str[j] =temp;
					}
				}
			}
		}
	}
	
	@SuppressLint("HandlerLeak")
	class handlerimp extends PlayerHandler{
		@Override
		public void playMedia() {
			String filename = playList.get(playIndex++);		
			
			if (playIndex >= playList.size()) {
				playIndex = 0;

				if (nullFile >= playList.size()) {
					mHandler.sendEmptyMessage(0x07);
				} else {
					nullFile = 0;
				}
			}

			if (nullFile < playList.size()) {
				
				int mediaType = getMediaType(filename);
				switch (mediaType) {
				case MEDIA_TYPE_VIDEO:
					isPlayVideo = true;					
					video.Init();					
					//video.RegisterClientMessager(p_msg.getBinder());
					video.RegisterClientMessager(new Messenger(new handlerimp()).getBinder());
					video.Open(contentPath + filename);
					video.Play();				
					break;
		
				case MEDIA_TYPE_IMAGE:			
					Bitmap bm = getLocalBitmap(contentPath + filename);
					if (bm == null) {
						nullFile++;
						playMedia();
					} else {
						mImageView.setVisibility(View.VISIBLE);
						
						mImageView.setImageBitmap(bm);
						
						ti = new Timer();
						ti.schedule(new TimerTask() {
							@Override
							public void run() {						
								mHandler.sendEmptyMessage(0x01);
							}
						}, imagePlayInterval);
						
					}
					
					break;
				}
			}			
		}


		
	}
	
	public void playMedia() {
		mTextView.setVisibility(View.GONE);	
		String filename = playList.get(playIndex++);		
		
		if (playIndex >= playList.size()) {
			playIndex = 0;

			if (nullFile >= playList.size()) {
				mHandler.sendEmptyMessage(0x07);
			} else {
				nullFile = 0;
			}
		}

		if (nullFile < playList.size()) {
			
			int mediaType = getMediaType(filename);
			switch (mediaType) {
			case MEDIA_TYPE_VIDEO:
				isPlayVideo = true;					
				video.Init();					
				//video.RegisterClientMessager(p_msg.getBinder());
				video.RegisterClientMessager(new Messenger(new handlerimp()).getBinder());
				video.Open(contentPath + filename);
				video.Play();				
				break;
	
			case MEDIA_TYPE_IMAGE:			
				Bitmap bm = getLocalBitmap(contentPath + filename);
				if (bm == null) {
					nullFile++;
					playMedia();
				} else {
					mImageView.setVisibility(View.VISIBLE);
					
					mImageView.setImageBitmap(bm);
					
					ti = new Timer();
					ti.schedule(new TimerTask() {
						@Override
						public void run() {						
							mHandler.sendEmptyMessage(0x01);
						}
					}, imagePlayInterval);
					
				}
				
				break;
			}
		}
		
	}
	

	
	

	private int getMediaType(String media) {
		if (media.endsWith(".mp4") || media.endsWith(".MP4")
				|| media.endsWith(".mpg") || media.endsWith(".MPG")
				|| media.endsWith(".mpeg") || media.endsWith(".MPEG")
				|| media.endsWith(".avi") || media.endsWith(".AVI")) {
			return MEDIA_TYPE_VIDEO;
		} else if (media.endsWith(".jpg") || media.endsWith(".JPG")
				|| media.endsWith(".png") || media.endsWith(".PNG")
				|| media.endsWith(".bmp") || media.endsWith(".BMP")) {
			return MEDIA_TYPE_IMAGE;
		}
		return -1;
	}

	private Bitmap getLocalBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		opts.inSampleSize = computeSampleSize(opts, -1, dm.widthPixels
				* dm.heightPixels);
		opts.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, opts);
	}

	// @Leo
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	// @Leo
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	class dialogClick implements OnClickListener{
		@Override
		public void onClick(DialogInterface dialog, int which) {			
			System.out.println("oooooooooo");			
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("boot", boot);
			editor.commit();
			System.out.println("which"+boot);
			if(quit){
				video.close();
				ExitApplication.getInstance().exit(Player.this);
			}
			video.showAll();
		}		
	}
	
	SharedPreferences settings;
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			if (event.getX()<= 50 && event.getY() <= 50) {
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Log.i(TAG, "Long-press catched, apk will shutdown.");						
						try {
							watchdogjni.disableWD();
						} catch (NoWatchDogJni e) {
							System.out.println("weidakai");
						}
						video.CloseAll();	
					}
				}, LONG_PRESS_TIME);				
				mydialog.show();
			}
			break;
			
		case MotionEvent.ACTION_UP:
			Log.i(TAG, "Touch Up");
			if (timer != null) {
				timer.cancel();
			}
		    //Player.this.finish();
			break;
			
		case MotionEvent.ACTION_MOVE:

			if (timer != null && (event.getX() > 50 || event.getY() > 50)) {
				timer.cancel();
			}
			break;
		}
		return true;
	}

	class ActionReceiver extends BroadcastReceiver {
		private boolean isRegisterReceiver = false;

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(FinalData.Action.START_PLAY)) {
				Log.i(TAG, "Received " + action);
				if (intent.hasExtra("UdPath")) {
					if (updateMediaOK) {
						mHandler.sendEmptyMessage(0x02);
						Player.this.finish();
					}
				} else {
					mHandler.sendEmptyMessage(0x02);
					Player.this.finish();
				}

			} else if (action.equals(FinalData.Action.STOP_PLAY)) {
				Log.i(TAG, "Received " + action);
				if (ti != null) {
					ti.cancel();
				}
				mHandler.sendEmptyMessage(0x03);

			} else if (action.equals(FinalData.Action.UPDATE_CONTENT)) {
				Log.i(TAG, "Received " + action);
				if (ZunidataEnvironment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					if (intent.hasExtra("UdPath")) {
						String up = intent.getStringExtra("UdPath") + "/";
						Log.i(TAG, "U-Disk Insert: " + up);
						int gui = getUserInfo(up + "userinfo.txt");
						if (gui == 2) {
							
							mHandler.sendEmptyMessage(0x04);
							updateMedia(up);
						} else if (gui == 1 || gui == 3) {
							toast = new Toast(getApplicationContext());
							toast.setView(mToastView);
							toast.setDuration(Toast.LENGTH_LONG);
							toastDisplay();
						}
					}
				} else {
					mHandler.sendEmptyMessage(0x03);
				}

				// } else if (action.equals(FinalData.Action.UPDATE_FW)) {
				// Log.i(TAG, "Received " + action);
			}

		}

		public void registerScreenActionReceiver(Context context) {
			if (!isRegisterReceiver) {
				isRegisterReceiver = true;
				IntentFilter filter = new IntentFilter();
				filter.addAction(FinalData.Action.START_PLAY);
				filter.addAction(FinalData.Action.STOP_PLAY);
				filter.addAction(FinalData.Action.UPDATE_CONTENT);
				// filter.addAction(FinalData.Action.UPDATE_FW);
				Log.i(TAG,
						"Register Receiver: START_PLAY, STOP_PLAY, UPDATE_CONTENT");
				context.registerReceiver(ActionReceiver.this, filter);
			}
		}

		public void unRegisterScreenActionReceiver(Context context) {
			if (isRegisterReceiver) {
				isRegisterReceiver = false;
				Log.i(TAG,
						"Unregister Receiver: START_PLAY, STOP_PLAY, UPDATE_CONTENT");
				context.unregisterReceiver(ActionReceiver.this);
			}
		}
	}
	

	private void toastDisplay() {
		td = new Timer();
		td.schedule(new TimerTask() {
			@Override
			public void run() {
				toastCount++;
				toast.show();
				if (toastCount < 4) {
					toastDisplay();
				} else {
					toastCount = 0;
					td.cancel();
				}				
			}
		}, 3000);
	}
	
	
	public void updateMedia(String str) {
		udPath = str;
		if (ti != null) {
			ti.cancel();
		}
		new Thread() {
			@Override
			public void run() {
				try {
					
					delFile(contentPath);
					copyFile(udPath, contentPath);
					if (checkMD5(udPath, contentPath)) {
						Log.i(TAG, "Copy End!");
						mHandler.sendEmptyMessage(0x05);
						udPath = null;
						updateMediaOK = true;
					} else {
						mHandler.sendEmptyMessage(0x06);
						udPath = null;
						delFile(contentPath);
					}
				} catch (Exception e) {
					mHandler.sendEmptyMessage(0x06);
					udPath = null;
					delFile(contentPath);

				}
			}
		}.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if (ti != null) {
			ti.cancel();
		}
		
		//SystemProperties.set("vplayer.hideStatusBar.enable", "false");
		
	}

	@Override
	protected void onDestroy() {
		//if (rDuration > 0) {
		if (timeWatchDogHandler != null) {
			timeWatchDogHandler.removeCallbacks(timeWatchDogRunnable);
		}
	//}
		
		if (timeWatchDog != null) {
			timeWatchDog.quit();
		}
		///////////////////////////////////////////////////////
		if (actionReceiver != null) {
			actionReceiver
					.unRegisterScreenActionReceiver(getApplicationContext());
			actionReceiver = null;
		}

		super.onDestroy();
	}
	
	

	private int getUserInfo(String str) {
		File userFile = new File(str);
		if (!userFile.exists()) {
			return 1;

		} else if (userFile.canWrite()) {
			try {
				FileInputStream is = new FileInputStream(userFile);
				byte[] buffer = new byte[is.available()];
				is.read(buffer);
				String info = new String(buffer);
				String [] infos  = info.split("\n");

				for (int i = 0; i < infos.length; i++) {
					if (infos[i].toLowerCase().contains("updatemedia=yes")) {
						Log.i(TAG, "Get userinfo.txt: updatemedia=yes");
						is.close();
						return 2;
					} else if (infos[i].toLowerCase().contains("updatemedia=no")) {
						Log.i(TAG, "Get userinfo.txt: updatemedia=no");
						is.close();
						return 3;
					} 
				}
				return 3;
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public void delFile(String delPath) {
		try {
			File a = new File(delPath);
			if (!a.exists()) {
				return;
			}
			String[] file = a.list();
			File temp = null;
			int i;
			for (i = 0; i < file.length; i++) {
				temp = new File(delPath + file[i]);
				if (temp.isDirectory()) {
					delFile(delPath + file[i] + "/");
				}
				Log.i(TAG, "Delete: " + temp);
				temp.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "Delete Failed!");
			throw new RuntimeException();
		}
	}

	public void copyFile(String cpFromPath, String cpToPath) {
		updateMediaOK = false;
		(new File(cpToPath)).mkdirs();
		FileInputStream input = null;
		FileOutputStream output = null;
		try {
			File a = new File(cpFromPath);
			if (!a.exists()) {
				return;
			}
			String[] file = a.list();
			File temp = null;
			int i;
			for (i = 0; i < file.length; i++) {
				temp = new File(cpFromPath + file[i]);
				if (!temp.isHidden()) {
					if (getMediaType(file[i]) == MEDIA_TYPE_VIDEO
							|| getMediaType(file[i]) == MEDIA_TYPE_IMAGE) {
						if (temp.isFile()) {
							Log.i(TAG, "Copy: " + temp);
							input = new FileInputStream(temp);
							output = new FileOutputStream(cpToPath
									+ (temp.getName()).toString());
							byte[] b = new byte[1024 * 5];
							int len;
							while ((len = input.read(b)) != -1) {
								output.write(b, 0, len);
							}
							output.flush();
							output.close();
							input.close();
						}
						// if (temp.isDirectory()) {
						// Log.i(TAG, "Copy: " + temp);
						// copyFile(cpFromPath + file[i] + "/", cpToPath+ file[i] +
						// "/");
						// }
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "Copy Failed!");
			// close(input);
			// close(output);
			try {
				output.flush();
				output.close();
				input.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			throw new RuntimeException();
		}
	}


	private static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String getMD5(String filename) {
		InputStream fis;
		byte[] buffer = new byte[1024 * 8];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return toHexString(md5.digest());
		} catch (Exception e) {
			return null;
		}
	}

	private boolean checkMD5(String path1, String path2) {
		try {
			int i, j = 0;
			String[] allFile = new File(path1).list();
			String[] visFile = new String[allFile.length];
			File temp = null;
			for (i = 0; i < allFile.length; i++) {
				temp = new File(path1 + allFile[i]);
				if (!temp.isHidden()
						&& (getMediaType(allFile[i]) == MEDIA_TYPE_VIDEO
						|| getMediaType(allFile[i]) == MEDIA_TYPE_IMAGE)) {
					// if (temp.isFile()) {
					visFile[j++] = allFile[i];
					// }
				}
			}
			for (i = 0; i < visFile.length; i++) {
				if (visFile[i] != null) {
					if (!getMD5(path1 + visFile[i]).equals(
							getMD5(path2 + visFile[i]))) {
						Log.i(TAG, "Check MD5 Wrong");
						return false;
					}
				}
			}
			Log.i(TAG, "Check MD5 OK");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "Check MD5 Failed");
			throw new RuntimeException();
		}

	}
	@Override
	public void onBackPressed() { 

	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		/////////////////////////leagcy
////		System.out.println(keyCode);
//////		System.out.println(res);
////		if(keyCode==155||keyCode==156)
////			res=res+'\t';
////		else if(keyCode==66){
////			System.out.println(res);
////			Intent intent =new Intent(Player.this, Main.class);
////			intent.putExtra("res", res);
////			res="";				
////			again=true;
////			video.CloseAll();
////			startActivityForResult(intent, 0);
////			
////		}else if(kt.getChar(keyCode)==' ')
////				;
////			else
////				res=res+new KeyCodeTable().getChar(keyCode);	
//////////////////////////////////////leagcy	
//		return super.onKeyDown(keyCode, event);
//	}
	
	
//	private Messenger p_msg =new Messenger(new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//		switch(msg.what){
//			case 1001:
//				switch (msg.arg1) {					
//				case 0x30004:	
//					if(wantStop){
//						break;
//					}
//					isPlayVideo = false;
//					video.Close();
//					video.CloseAll();
//					playMedia();
//				}
//			}
//		}
//	});	
	
}

