package com.example.androidutil.util.badge;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

public class BadgeUtil {

    /**
     * Set badge count<br/>
     * 针对 Samsung / xiaomi / sony 手机有效
     * @param context The context of the application package.
     * @param count Badge count to be set
     */
    @SuppressLint("DefaultLocale") 
    public static void setBadgeCount(Context context, int count, Notification notification) {
        try {
			if (count <= 0) {
			    count = 0;
			} else {
			    count = Math.max(0, Math.min(count, 99));
			}

			if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
			    sendToXiaoMi(context, count, notification);
			} else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
//			    sendToSony(context, count);
			} else if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
			    sendToSamsumg(context, count);
			} else if (Build.MANUFACTURER.equalsIgnoreCase("htc")) {
//				sendToHTC(context, count);
			} else if (Build.MANUFACTURER.equalsIgnoreCase("lg")) {
//				sendToLG(context, count);
			} else if (Build.MANUFACTURER.equalsIgnoreCase("huawei")) {
				sendToHuaWei(context, count);
			} else {
//            Toast.makeText(context, "Not Support", Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
            
    }

    /**
     * 向华为发送未读消息数广播
     * @param count
     */
    private static void sendToHuaWei(Context context, int count) {
    	try{
    		String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }
		    Bundle bunlde =new Bundle();
		    bunlde.putString("package", context.getPackageName());   // com.test.badge is your package name
		    bunlde.putString("class", launcherClassName);  // com.test. badge.MainActivity is your apk main activity
		    bunlde.putInt("badgenumber",count);
		    context.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, bunlde);
	    }catch(Exception e){
	        e.printStackTrace();
	    }

    }
    
    /**
     * 向小米手机发送未读消息数广播
     * 需要和通知一起设置
     * @param count
     */
    private static void sendToXiaoMi(Context context, int count, Notification notification) {
    	try {
			if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")
					&& notification != null) {
				Field field = notification.getClass().getDeclaredField("extraNotification");
				Object extraNotification = field.get(notification);
				Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
				method.invoke(extraNotification, count);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }


//    /**
//     * 向索尼手机发送未读消息数广播<br/>
//     * 据说：需添加权限：<uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" /> [未验证]
//     * @param count
//     */
//    private static void sendToSony(Context context, int count){
//        String launcherClassName = getLauncherClassName(context);
//        if (launcherClassName == null) {
//            return;
//        }
//
//        boolean isShow = true;
//        if (count == 0) {
//          isShow = false;
//        }
//        Intent localIntent = new Intent();
//        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE",isShow);//是否显示
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",launcherClassName );//启动页
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));//数字
//        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());//包名
//        context.sendBroadcast(localIntent);
//    }


    /**
     * 向三星手机发送未读消息数广播
     * @param count
     */
    private static void sendToSamsumg(Context context, int count){
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }
    
//    /**
//     * 向LG手机发送未读消息数广播
//     * @param count
//     */
//    private static void sendToLG(Context context, int count){
//        String launcherClassName = getLauncherClassName(context);
//        if (launcherClassName == null) {
//            return;
//        }
//        Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
//        badgeIntent.putExtra("badge_count_package_name", context.getPackageName());
//        badgeIntent.putExtra("badge_count_class_name", launcherClassName);
//        badgeIntent.putExtra("badge_count", count);
//        context.sendBroadcast(badgeIntent);
//    }
//    
//    /**
//     * 向HTC手机发送未读消息数广播
//     * @param count
//     */
//    private static void sendToHTC(Context context, int count){
//        String launcherClassName = getLauncherClassName(context);
//        if (launcherClassName == null) {
//            return;
//        }
//        Intent badgeIntent = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
//        badgeIntent.putExtra("packagename", launcherClassName);
//        badgeIntent.putExtra("count", count);
//        context.sendBroadcast(badgeIntent);
//    }


    /**
     * 重置、清除Badge未读显示数<br/>
     * @param context
     */
    public static void resetBadgeCount(Context context) {
        setBadgeCount(context, 0, null);
    }


    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the
     *         "android:name" attribute.
     */
    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }
}
