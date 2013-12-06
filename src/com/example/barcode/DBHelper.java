package com.example.barcode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private final static String DATABASE_NAME = "barcode.db";
	public static final int DATABASE_VERSION = 1;
	private final static String TABLE_NAME = "products";

	public static final String ID = "_id";
	public static final String BARCODE = "barcode";
	public static final String NAME = "name";
	public static final String PRICE = "price";
	public static final String DETAIL = "detail";
	
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +" ("
												+ ID +" integer primary key autoincrement,"
												+ BARCODE +" text,"
												+ NAME +" text,"
												+ PRICE +" text,"
												+ DETAIL +" text);";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);

		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787302177869\", \"Linux開發工具箱：專案開發的最有效途徑\", \"58元\", \"作者：（美）John Fusco著，賈嚴磊，董西廣，王在奇譯\n出版社：清華大學出版社\n價格：58元\")"); 
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9789866072000\", \"Google Android SDK開發範例大全\", \"NT950\", \"作者：佘志龍、陳昱勳、鄭名傑、陳小鳳\n出版社：悅知文化\n價格：NT950\")"); 
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787121100000\", \"iPhone SDK 3 開發指南\", \"65元\", \"作者：（美）Bill Dudney Chris Adamson著，（美）李亮，楊武，張永強，苟振興譯\n出版社：電子工業出版社\n價格：65元\")"); 
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " + 
				"(null, \"9787111347446\", \"Linux內核設計的藝術：圖解Linux作業系統架構設計與實現原理\", \"79元\", \"作者：新設計團隊\n出版社：機械工業出版社\n價格：79元\")"); 
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " + 
				"(null, \"9787111357629\", \"深入理解Android\", \"69元\", \"作者：鄧凡平\n出版社：機械工業出版社\n價格：69元\")"); 
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " + 
				"(null, \"9787111267768\", \"Python學習手冊\", \"89元\", \"作者：Mark Lutz著，侯靖等譯\n出版社：機械工業出版社\n價格：89元\")"); 
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9789861819174\", \"iPhone程式設計範例經典\", \"NT480\", \"作者：Paul Deitel等著，楊仁和譯\n出版社：碁峰資訊股份有限公司\n價格：NT480\")"); 
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787564115197\", \"LINUX系統程式設計\", \"56元\", \"作者：ROBERT LOVE著，O'REILLY TAIWAN譯\n出版社：東南大學出版社\n價格：56元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " + 
				"(null, \"9789574428618\", \"品牌的秘密\", \"NT580\", \"作者：World Branding Committee著，陳怡伶譯\n出版社：旗標出版有限公司\n價格：NT580\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787111276869\", \"Objective-C2.0程式設計（原書第2版）\", \"66元\", \"作者：（美）Stephen G. Kochan著，張波，黃湘琴等譯\n出版社：機械工業出版社\n價格：66元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787302059998\", \"VB.NET和SQL Server 2000高級程式設計——創建高效資料層\", \"59元\", \"作者：Tony Brain Denise Gosnell等著，康博譯\n出版社：清華大學出版社\n價格：59元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787115142009\", \"Windows 9X/Me/NT/2000/XP/2003 DOS命令列技術大全\", \"68元\", \"作者：劉曉輝等著\n出版社：人民郵電出版社\n價格：68元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787111323570\", \"Linux內核A完全參考手冊\", \"79元\", \"作者：邱鐵，周玉，鄧瑩瑩\n出版社：機械工業出版社\n價格：79元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787508338637\", \"Linux設備驅動程式\", \"69元\", \"作者：（美）科波特（Corbet,J.）等，魏永明，耿岳，鐘書毅譯\n出版社：中國電力出版社\n價格：69元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787564115203\", \"LINUX Networking Cookbook（中文版）\", \"88元\", \"作者：CARLA SCHRODER著，馮亮譯\n出版社：東南大學出版社\n價格：88元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787121035753\", \"Programming ASP.NET中文版\", \"99元\", \"作者：（美）裡伯提，（美）赫威茲著，瞿傑，趙立東，張昊譯\n出版社：電子工業出版社\n價格：99元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787111337270\", \"Android技術內幕 系統磁碟區\", \"69元\", \"作者：楊豐盛\n出版社：機械工業出版社\n價格：69元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787115156655\", \"Flash8基礎與實例精講\", \"39元\", \"作者：騰飛科技\n出版社：人民郵電出版社\n價格：39元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787115298027\", \"Android深度探索（卷1）HAL與驅動開發\", \"99元\", \"作者：李寧\n出版社：人民郵電出版社\n價格：99元\")");
		db.execSQL("INSERT INTO " + TABLE_NAME + "(" + ID + ", "+ BARCODE + ", " + NAME + ", " + PRICE + ", "+ DETAIL + ") VALUES " +
				"(null, \"9787508356464\", \"Head First HTML與CSS.XHTML(中文版)\", \"79元\", \"作者：（美）弗裡曼等，魏永明，林旺，張曉坤譯\n出版社：中國電力出版社\n價格：79元\")");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	

	

}
