package com.example.androidutil.util;

import android.app.Activity;
import android.os.Environment;

public class Util {
	private static Util util;
	public static int flag = 0;
	private Util(){
		
	}
	
	public static Util getInstance(){
		if(util == null){
			util = new Util();
		}
		return util;
	}
	
	/**
	 * 判断是否有sdcard
	 * @return
	 */
	public boolean hasSDCard(){
		boolean b = false;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			b = true;
		}
		return b;
	}
	
	/**
	 * 得到sdcard路径
	 * @return
	 */
	public String getExtPath(){
		String path = "";
		if(hasSDCard()){
			path = Environment.getExternalStorageDirectory().getPath();
		}
		return path;
	}
	
	/**
	 * 得到/data/data/rideEasy.imagedownload目录
	 * @param mActivity
	 * @return
	 */
	public String getPackagePath(Activity mActivity){
		return mActivity.getFilesDir().toString();
	}

}
