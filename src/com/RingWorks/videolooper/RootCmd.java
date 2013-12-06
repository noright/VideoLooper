package com.RingWorks.videolooper;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import android.util.Log;

public class RootCmd {

	private static final String TAG = "VideoLooper";


	public static String RootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		DataInputStream is = null;
		if (command == null)
			command = "";
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			// Log.v(TAG, "Exec command:" + command);
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
			is = new DataInputStream(process.getInputStream());
			while (is.available() > 0) {
				String result = is.readUTF();
				Log.v(TAG, "command=" + command + "  result=" + result);
				return result;
			}
		} catch (Exception e) {
			Log.d(TAG, "ROOT REE" + e.getMessage());
			return null;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) { }
		}
		return null;
	}

}
