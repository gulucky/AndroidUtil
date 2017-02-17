package com.example.androidutil.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends Activity{
	
	private static List<Activity> activities = new ArrayList<Activity>();
	
//	private static Activity curentActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			activities.add(this);
//			curentActivity = this;
			if (savedInstanceState != null) {
				finish();
				return;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			finish();
		}
	}
	
//	@Override  
//	public Resources getResources() {  
//	    Resources res = super.getResources();  
//	    Configuration config = res.getConfiguration();  
//	    config.fontScale = (float) 3; //1 设置正常字体大小的倍数  
//	    res.updateConfiguration(config, res.getDisplayMetrics());  
//	    return res;  
//	}
	
	public static void clearSameActivity(Activity activity){
		try {
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i).getClass().getName().equals(activity.getClass().getName())) {
					activities.get(i).finish();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static <T> boolean isExistActivity(Class<T> class1){
		boolean result = false;
		try {
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i).getClass().getName().equals(class1.getName())) {
					result = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static <T> void finishTheActivity(Class<T> class1){
		try {
			for (int i = 0; i < activities.size(); i++) {
				if (activities.get(i).getClass().getName().equals(class1.getName())) {
					activities.get(i).finish();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	}
	
	@Override
	protected void onPause() {
	    super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		activities.remove(this);
		super.onDestroy();
	}
	
	public static void addActivity(Activity activity){
		try {
			activities.add(activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removeActivity(Activity activity){
		try {
			activities.remove(activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void clearAllActivitys(){
		for (int i = 0; i < activities.size(); i++) {
			try {
				activities.get(i).finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void showToastOnActivity(final String text){
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(BaseActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
//	private static Handler h = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			try {
//				Toast.makeText(IMApplication.getInstance(), "你的账号已在其他设备登录", Toast.LENGTH_SHORT).show();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			super.handleMessage(msg);
//		}
//	};
	
}
