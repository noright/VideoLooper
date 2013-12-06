package com.RingWorks.videolooper;

import java.io.DataOutputStream;

public class HideStatusBar {
	
	public static void enable() {
		runRootCommand("setprop mbx.hideStatusBar.enable true");
		runRootCommand("setprop vplayer.hideStatusBar.enable true");
	}
	
	public static void disable() {
		runRootCommand("setprop mbx.hideStatusBar.enable false");
		runRootCommand("setprop vplayer.hideStatusBar.enable false");
	}
	
	public static boolean runRootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		return true;
	} 
}
