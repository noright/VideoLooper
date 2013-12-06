package com.example.barcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.RingWorks.videolooper.Player;
import com.RingWorks.videolooper.R;


public class Main extends Activity {
	TextView tv;
	ScrollView svBarcode;
	Button button;
	DBHelper dbhelper;
	SQLiteDatabase db;
	String name[] = new String[100], price[] = new String[100],detail[] = new String[100];
	int num = 0;
	TextView textView;
//	ListView listView;
	Button bu;
	Menu menu00;
//	ArrayAdapter<String> adapter;
	List<String> data = new ArrayList<String>();
	SoundPool soundPool;
	int spId;
	AudioManager am;
	AlertDialog.Builder alert;
	Boolean isShowAlert = false;
	Boolean isFromPause = false;
	Boolean isFirstTime = false;
	String res = "";
	KeyCodeTable kt = null;
	
	int count=0;
	Timer timer=null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main0);
		kt=new KeyCodeTable();
		dbhelper = new DBHelper(this);
		db = dbhelper.getReadableDatabase();
		textView =(TextView) findViewById(R.id.textView1);
//		listView =(ListView) findViewById(R.id.listView1);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, data);
//		listView.setAdapter(adapter);
//        listView.setFocusable(false);
//        listView.setTranscriptMode(2);
        timer=new Timer();
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        spId = soundPool.load(this,R.raw.di, 1);
        if(getIntent().hasExtra("res"))res=getIntent().getStringExtra("res");
        
        alert = new AlertDialog.Builder(this);   
       
//        listView.setOnItemClickListener(new OnItemClickListener() {
//        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                    long arg3) {
//        		if (price[arg2] != null) {
//            		Intent intent = new Intent(Main.this, DetailInfo.class);
//            		intent.putExtra("detail", detail[arg2]);
//                    startActivity(intent);
//        		}
//            }
//        });
//        textView.setText("��ǰ�l�a��"+res);
        getinfo(res);
        res="";
        timer.schedule(timerTask, 1000,1000);
	}

//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add("Clear");
//		menu00 = menu;
//		return true;
//
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case 0:
//			data.clear();
//			textView.setText("");
//			menu00.removeItem(0);
//
//			break;
//		}
//		return true;
//	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
	//	Log.v("lll", String.valueOf(keyCode));
//		if (!menu00.hasVisibleItems()) {
//			menu00.add("Clear");
//		}
		if (keyCode == 155 || keyCode == 156)
			res = res + '\t';
		else if (keyCode == 66) {
			getinfo(res);
			System.out.println(res);
			//res = "��ǰ�l�a��" + res;
			//textView.setText(res);
			//res = "";
		} else if (kt.getChar(keyCode) == ' ')
			;
		else
			res = res + new KeyCodeTable().getChar(keyCode);
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onDestroy() {
		detail = null;
		deleteDatabase("barcode.db");
		db.close();
		super.onDestroy();
	}

	private void getinfo(String barcode) {
		System.out.println(barcode);
		if (num < name.length) {

			soundPool.play(spId, 1, 1, 1, 0, 1);

			Cursor cursor = db.rawQuery(
					"select * from products where barcode=?",
					new String[] { String.valueOf(barcode) });

			if (cursor.moveToFirst()) {
				name[num] = cursor.getString(cursor.getColumnIndex("name"));
				price[num] = cursor.getString(cursor.getColumnIndex("price"));
				detail[num] = "商品名『"
						+ cursor.getString(cursor.getColumnIndex("name"))
						+ "』\n條碼：" + barcode + "\n\n詳細信息\n"
						+ cursor.getString(cursor.getColumnIndex("detail"));
			}

			cursor.close();

			if (name[num] == null || price[num] == null) {
				name[num] = "無此商品信息";
				detail[num] = "無此商品信息";
				data.add(barcode + "　" + name[num]);
			} else {
				data.add(barcode + "『" + name[num] + "』　　" + price[num]);
			}
			System.out.println(detail[num]);
			System.out.println(num);
			textView.setText(detail[num]);
			
//			adapter.notifyDataSetChanged();
//			listView.setSelection(listView.getCount());
			res="";
			count=0;
			num++;			

			
		} else {

			if (!isShowAlert) {
				isShowAlert = true;

				alert.setTitle("Prompt");
				alert.setMessage("\nThe number of products exceeds "
						+ name.length + ". Your history will be cleared.\n");

				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								data.clear();
//								adapter.notifyDataSetChanged();

								num = 0;
								for (int l = 0; l < name.length; l++) {
									name[l] = null;
									price[l] = null;
									detail[l] = null;
								}

								isShowAlert = false;
							}
						});

				alert.setCancelable(false);
				alert.show();
			}
		}
		
		
	}

	TimerTask timerTask = new TimerTask() {
		
		public void run() {
			count++;
			System.out.println("qq"+count);
			if(count==10){
				Intent intent = new Intent(Main.this, Player.class);
				setResult(RESULT_OK,intent);
//				startActivity(intent);	
	            timer.cancel();
	            finish();
			}		
		}
	};
}
