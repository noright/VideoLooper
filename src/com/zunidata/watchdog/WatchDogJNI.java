package com.zunidata.watchdog;
import java.io.File;

public class WatchDogJNI {	
	
	
	private native boolean enableWatchDog();
	private native boolean disableWatchDog();
	private native boolean outputHighPules();
	private native int open();
	private native void close();
	
	private boolean wdEnable;
	private static boolean hasWatchDog;
	
	//final boolean DEBUG=GlobalString.DEBUG;
	static 
	{		
			hasWatchDog=false;
			System.out.println(new File("/system/lib/libwatchdogjni.so").exists());
			if(new File("/system/lib/libwatchdogjni.so").exists()){
				hasWatchDog=true;
				System.loadLibrary("watchdogjni");	
			}					
	}
	private static WatchDogJNI watchdogjni = null;
	private WatchDogJNI(){
		wdEnable=false;
	}	
	
	public static WatchDogJNI getInstance()
	{
		if(watchdogjni == null)
			watchdogjni = new WatchDogJNI();	
		return watchdogjni;
	}
////////////////////////////////////////////////////////////	
	public boolean enableWD() throws NoWatchDogJni{
		if(!hasWatchDog)throw new NoWatchDogJni();
		if(!wdEnable){
			watchdogjni.open();
			watchdogjni.enableWatchDog();
		}
		wdEnable=true;
		return true;
		
	}
	
	
	public boolean feedWD() throws NoWatchDogJni{
		if(!hasWatchDog)throw new NoWatchDogJni();
		if(wdEnable)
			outputHighPules();
		return true;
		
	}
	
	public boolean disableWD() throws NoWatchDogJni{
		if(!hasWatchDog)throw new NoWatchDogJni();
		if(wdEnable){
			watchdogjni.disableWatchDog();
			watchdogjni.close();
			wdEnable=false;
		}
		return true;
	}

}
