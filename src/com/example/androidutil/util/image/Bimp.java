package com.example.androidutil.util.image;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class Bimp {
	public static int max = 0;
	
	public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();   //选择的图片的临时列表
	
	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 500)
					&& (options.outHeight >> i <= 500)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		int orientation = Bimp.readPictureDegree(path);//获取旋转角度  
		if(Math.abs(orientation) > 0){  
			bitmap = Bimp.rotaingImageView(orientation, bitmap);//旋转图片  
		} 
		return bitmap;
	}
	
	public static Bitmap revitionOriginalImageSize(String path) throws IOException {
		Bitmap bitmap = null;
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(
					new File(path)));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, options);
			in.close();
			int i = 0;
			bitmap = null;
			while (true) {
				if ((options.outWidth >> i <= 1000)
						&& (options.outHeight >> i <= 1000)) {
					in = new BufferedInputStream(
							new FileInputStream(new File(path)));
					options.inSampleSize = (int) Math.pow(2.0D, i);
					options.inJustDecodeBounds = false;
					bitmap = BitmapFactory.decodeStream(in, null, options);
					break;
				}
				i += 1;
			}
			int orientation = Bimp.readPictureDegree(path);//获取旋转角度  
			if(Math.abs(orientation) > 0){  
				bitmap = Bimp.rotaingImageView(orientation, bitmap);//旋转图片  
			}
		} catch (OutOfMemoryError e) {
			try {
				bitmap = revitionImageSize(path);
			} catch (Throwable e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} 
		return bitmap;
	}
	
	public static void clearImageItem(){
		try {
			max = 0;
			for (int i = 0; i < tempSelectBitmap.size(); i++) {
				Bitmap bitmap = tempSelectBitmap.get(i).getBitmap();
				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
					tempSelectBitmap.get(i).setBitmap(null);
				}
			}
			tempSelectBitmap.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 读取图片的方向
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {  
	    int degree  = 0;  
	    try {  
	        ExifInterface exifInterface = new ExifInterface(path);  
	        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);  
	        switch (orientation) {  
	            case ExifInterface.ORIENTATION_ROTATE_90:  
	                degree = 90;  
	                break;  
	            case ExifInterface.ORIENTATION_ROTATE_180:  
	                degree = 180;  
	                break;  
	            case ExifInterface.ORIENTATION_ROTATE_270:  
	                degree = 270;  
	                break;  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return degree;  
	}  
	
	/**
	 * 旋转图片
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
	    //旋转图片 动作  
	    Matrix matrix = new Matrix();;  
	    matrix.postRotate(angle);  
	    // 创建新的图片  
	    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
	            bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
	    return resizedBitmap;  
	}  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
