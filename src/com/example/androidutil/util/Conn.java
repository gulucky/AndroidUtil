package com.example.androidutil.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class Conn {
	
	//友盟是否允许发送错误信息
	public static final boolean IS_REPORT_ERRER_LOG = false;
	// URL,webview	
	public static String URL = "";//192.168.0.173:1100    124.95.128.252:8888
	public final static String FILE_URL = "IM";
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
 

	// 去掉double多余的0
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");// 去掉多余的0
			s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
		}
		return s;
	}
	
	//保留小数
	public static String formatDouble(double d){
		try {
			DecimalFormat df =new DecimalFormat("#0.00");  
			String flowString = df.format(d);
			return flowString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	//保留小数
	public static String formatDoubleToString(double d){
		try {
			return subZeroAndDot(formatDouble(d));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static  String formatFlows(float flow){
		String result = "";
		try {
			if(flow >= 1000){
				result = formatDouble((double)flow / 1024);
				result = subZeroAndDot(result);
				result += "G";
			}else {
				result = formatDouble(flow);
				result = subZeroAndDot(result);
				result = result + "M";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 解析时间
	 * @param jsondate
	 * @return
	 */
	public static String parseDate(String jsondate, SimpleDateFormat myFmt2) {
		try {
			String jsondateString = jsondate.replace("/Date(", "").replace(")/", "");
			long longTime = Long.valueOf(jsondateString).longValue();
			return myFmt2.format(new java.util.Date(longTime));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return jsondate;
	}

	//加载框
	public static Object jiazaiClass=null;
	
    
	// 获取距离
	private static final double EARTH_RADIUS = 6378137.0;

	public static double GetDistance(double lat_a, double lng_a, double lat_b, double lng_b) {

		double radLat1 = (lat_a * Math.PI / 180.0);

		double radLat2 = (lat_b * Math.PI / 180.0);

		double a = radLat1 - radLat2;

		double b = (lng_a - lng_b) * Math.PI / 180.0;

		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)

		+ Math.cos(radLat1) * Math.cos(radLat2)

		* Math.pow(Math.sin(b / 2), 2)));

		s = s * EARTH_RADIUS;

		s = Math.round(s * 10000) / 10000;

		return s;

	}
	
	
	public static String phoneNumber = "";
	
	public static String deviceID = "";
	
	public static String getPhoneNumber(Context context){
		String phoneNum = "0";
		try {
			TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			phoneNum = tm.getLine1Number();
			phoneNum = phoneNum.substring(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(phoneNum)) {
			phoneNum = "0";
		}
		return phoneNum;
	}
	
	public static String getDeviceInfo(Context context) {
	    try{
	      JSONObject json = new JSONObject();
	      TelephonyManager tm = (TelephonyManager) context
	          .getSystemService(Context.TELEPHONY_SERVICE);

	      String device_id = tm.getDeviceId();

	      WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

	      String mac = wifi.getConnectionInfo().getMacAddress();
	      json.put("mac", mac);

	      if( TextUtils.isEmpty(device_id) ){
	        device_id = mac;
	      }

	      if( TextUtils.isEmpty(device_id) ){
	        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
	      }
	      json.put("device_id", device_id);
	      return device_id;
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	  return "0";
	}
	
	//获取本地手机参数
	public static String getParamString(String param){
		if (TextUtils.isEmpty(param)) {
			return "";
		}
		StringBuffer result = new StringBuffer("");
//		try {
//			if (param.contains("?")) {
//				result.append("&");
//			}else {
//				result.append("?");
//			}
//			if (isLogin()) {
//				result.append("ActionPhone=").append(loginUser.getMen_Phone()).append("&");
//				result.append("ActionMemID=").append(loginUser.getMem_ID()).append("&");
//			}else {
//				if (TextUtils.isEmpty(phoneNumber)) {
//					phoneNumber = getPhoneNumber(context1);
//				}
//				result.append("ActionPhone=").append(phoneNumber).append("&");
//				result.append("ActionMemID=").append("0").append("&");
//			}
//			if (TextUtils.isEmpty(deviceID)) {
//				deviceID = getDeviceInfo(context1);
//			}
//			result.append("ActionPhoneKey=").append(deviceID).append("&");
//			result.append("ActionPhoneType=Android");
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
		return result.toString();
	}
}
