package com.RingWorks.videolooper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public abstract class PlayerHandler extends Handler {

	WeakReference<Activity> mActivity;
	boolean first=true;
	public PlayerHandler(){
		
	}
	public PlayerHandler(Activity activity) {
		
        mActivity = new WeakReference<Activity>(activity);
	}
	abstract void playMedia();
	public void handleMessage(Message msg) {
        switch (msg.what) {
        case MsgWhat.TIME_MSG:
            int current_time=msg.arg1;
            int full_time=msg.arg2;
            System.out.println(current_time +":"+full_time);
            break;
        case MsgWhat.STATUS_MSG:      	
        	switchstatus(msg.arg1, msg.arg2);
        	break;
        case MsgWhat.AUDIO_MSG:
        	System.out.println("audio");
        	break;
        case MsgWhat.ERROR_MSG:
        	System.out.println("error");
        	break;
        case MsgWhat.UI_MSG:
        	System.out.println("ui");
        	break;
        default:
        	System.out.println("weichuli");
        }
	}
	
	class MsgWhat{
		static final int TIME_MSG=1000;
		static final int STATUS_MSG=1001;
		static final int AUDIO_MSG=1002;
		static final int ERROR_MSG=1003;
		static final int UI_MSG=1004;
	}
	class MsgPlayerStatus{
		static final int PLAYER_INITING=0x10001;
		static final int PLAYER_TYPE_REDY=0x10002;
		static final int PLAYER_INITOK=0x10003;

		static final int PLAYER_RUNNING=0x20001;
		static final int PLAYER_BUFFERING=0x20002;
		static final int PLAYER_PAUSE=0x20003;
		static final int PLAYER_SEARCHING=0x20004;
		static final int PLAYER_SEARCHOK=0x20005;
		static final int PLAYER_START=0x20006;
		static final int PLAYER_FF_END=0x20007;
		static final int PLAYER_FB_END=0x20008;
		
		static final int PLAYER_ERROR=0x30001;
		static final int PLAYER_PLAYEND=0x30002;
		static final int PLAYER_STOPED=0x30003;
		static final int PLAYER_EXIT=0x30004;
		
		static final int DIVX_AUTHOR_ERR=0x40001;
		static final int DIVX_EXPIRED=0x40002;
		static final int DIVX_RENTAL=0x40003;
	}
	private void switcherror(int error){		
		System.out.println(Errorno.getErrorInfo(error));		
	}
	
	/*
	 * 
	 * 
	 * 
	 */
	boolean out=false;
	private void switchstatus(int status,int error){
		switch(status){
		case MsgPlayerStatus.PLAYER_INITING:
			System.out.println("aaa1");
			break;
		case MsgPlayerStatus.PLAYER_TYPE_REDY:
			System.out.println("aaa2");
			break;
		case MsgPlayerStatus.PLAYER_INITOK:
			System.out.println("aaa3");
			break;
			
		case MsgPlayerStatus.PLAYER_RUNNING:
			System.out.println("aaa4");
			break;
		case MsgPlayerStatus.PLAYER_BUFFERING:
			System.out.println("aaa5");
			break;
		case MsgPlayerStatus.PLAYER_PAUSE:
			System.out.println("aaa6");
			break;
		case MsgPlayerStatus.PLAYER_SEARCHING:
			System.out.println("aaa7");
			break;
		case MsgPlayerStatus.PLAYER_SEARCHOK:
			System.out.println("aaa8");
			break;
		case MsgPlayerStatus.PLAYER_START:
			writeFile(GlobalString.Fb0Blank, "1");
			if(first){
				writeFile(GlobalString.Fb1Blank, "1");
				first=false;
			}
			
			writeFile(GlobalString.Videoaxis, "0 0 0 0");
			System.out.println("aaa9");
			break;
		case MsgPlayerStatus.PLAYER_FF_END:
			System.out.println("aaa10");
			break;
		case MsgPlayerStatus.PLAYER_FB_END:
			System.out.println("aaa11");
			break;
			
		case MsgPlayerStatus.PLAYER_ERROR:
			switcherror(error);
			writeFile(GlobalString.Fb0Blank, "0");
			System.out.println("aaa12");
			break;
		case MsgPlayerStatus.PLAYER_PLAYEND:
			out=true;
			System.out.println("aaa13");
			break;
		case MsgPlayerStatus.PLAYER_STOPED:
			out=false;
			System.out.println("aaa14");
			break;
		case MsgPlayerStatus.PLAYER_EXIT:
			writeFile(GlobalString.Fb0Blank, "0");
			if(out)playMedia();
			System.out.println("aaa15");
			break;
			
		case MsgPlayerStatus.DIVX_AUTHOR_ERR:
			System.out.println("aaa16");
			break;
		case MsgPlayerStatus.DIVX_EXPIRED:
			System.out.println("aaa17");
			break;
		case MsgPlayerStatus.DIVX_RENTAL:
			System.out.println("aaa18");
			break;
		}
	}
	private void writeFile(String file, String value){
		File OutputFile = new File(file);
		if(!OutputFile.exists()) {        	
        	return;
        }

    	try {
			BufferedWriter out = new BufferedWriter(new FileWriter(OutputFile), 32);
    		try {
				Log.d("", "----------------set" + file + ": " + value);
    			out.write(value);    
    		} 
			finally {
				out.close();
			}
		}
		catch (IOException e) {
			Log.e("", "IOException when write "+OutputFile);
		}
	}
	
}
