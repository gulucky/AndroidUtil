package com.example.androidutil.util.toast;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil {
	
	public static void showToast(final Context context, final String showContent){
		try {
			Handler handler = new Handler(Looper.getMainLooper()); 
			handler.post(new Runnable() {
				public void run() {
					try {
						Toast.makeText(context, showContent, Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	

}
