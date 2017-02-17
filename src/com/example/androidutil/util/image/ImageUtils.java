package com.example.androidutil.util.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.androidutil.util.Conn;
import com.example.androidutil.util.Util;
import com.example.androidutil.util.file.DownLoadFileInterface;

/**
 * 处理图片的工具类
 */
public class ImageUtils {

	/**
	 * 图片基准路径
	 */
	public static final String BASE_SDCARD_IMAGES = Util.getInstance()
			.getExtPath() + "/" + Conn.FILE_URL + "/image/";

	private static final String TAG = "ImageUtils";
	
	public static final String FORMAT = ".jpg1";

	/**
	 * 判断文件是否存在
	 * 
	 * @param 文件在本地的完整名
	 * @return
	 */
	private static boolean judgeExists(String fullName) {

		try {
			File file = new File(fullName);
			return file.exists();
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取最后的‘/’后的文件名
	 * 
	 * @param name
	 * @return
	 */
	private static String getLastName(String name) {
		if (TextUtils.isEmpty(name)) {
			return "";
		}
		int lastIndexOf = 0;
		try {
			lastIndexOf = name.lastIndexOf('/');
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return !name.equals("") ? name.substring(lastIndexOf + 1) + "1" : "";
	}

	/**
	 * 拼接一个完整的本地文件名
	 * 
	 * @param 文件的网络路径
	 * @return
	 */
	public static String getImageFullName(String name) {
		return BASE_SDCARD_IMAGES + getLastName(name);
	}
	
	/**
	 * 服务器路径和本地是否匹配
	 * @return
	 */
	public static boolean isMatchingImage(String serverUrl, String localUrl){
		boolean isMatch = false;
		try {
			if (TextUtils.isEmpty(serverUrl)) {
				return  true;
			}
			String fullName = getImageFullName(serverUrl);
			if (judgeExists(fullName) && localUrl.equals(fullName)) {
				isMatch = true;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return isMatch;
	}

	/**
	 * 通过该网络路径获取Bitmap
	 * 
	 * @param 该图片的网络路径
	 */
	public static Bitmap getBitmap(String urlPath) {
		Bitmap bitmap = null;
		try {
			String fullName = getImageFullName(urlPath);
			if (ImageUtils.judgeExists(fullName)) {
				/* 存在就直接使用 */
//			Log.e(TAG, "使用了sdcard里的图片");
//				bitmap = BitmapFactory.decodeFile(fullName);
				bitmap = Bimp.revitionOriginalImageSize(fullName);
			} else {
				
				/* 去下载图片,下载完成之后返回该对象 */
//			Log.e(TAG, "去下载了图片");
				bitmap = downloadAndSaveBitmap(urlPath, fullName);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	/**
	 * 判断图片是否存在
	 * @param urlPath
	 * @return true 不存在  false 存在
	 */
	public static Boolean getBitmapBoolean(String urlPath) {

		String fullName = getImageFullName(urlPath);
		if (ImageUtils.judgeExists(fullName)) {
			return false;
		} else {
			return true;

		}
	}

	/**
	 * 下载保存图片
	 * 
	 * @param urlPath
	 *            下载路径
	 * @param fullName
	 *            文件保存路径+文件名
	 * @return
	 */
	private static Bitmap downloadAndSaveBitmap(String urlPath, String fullName) {

		Bitmap bitmap = downloadImage(urlPath,fullName);

//		if (bitmap != null) {
//
//			saveBitmap(fullName, bitmap);
//
//		}

		return bitmap;
	}

	/**
	 * 保存图片
	 * 
	 * @param fullName
	 * @param bitmap
	 */
	private static void saveBitmap(String fullName, byte[] buffer) {
		FileOutputStream fos = null;
		try {
			File file = new File(fullName);
			if (!file.exists()) {
				creatFolder(fullName);
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			fos.write(buffer);
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "图片保存失败，异常信息是：" + e.toString());
		} finally {
			try {
				if (fos != null) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				fos = null;
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建保存文件的文件夹
	 * 
	 * @param fullName
	 *            带文件名的文件路径
	 * @return
	 */
	private static void creatFolder(String fullName) {
		if (getLastName(fullName).contains(".")) {
			String newFilePath = fullName.substring(0,
					fullName.lastIndexOf('/'));
			File file = new File(newFilePath);
			file.mkdirs();
		}
	}

	/**
	 * 下载图片
	 * 
	 * @param urlPath
	 * @param fullName 
	 * @return
	 */
	private static Bitmap downloadImage(String urlPath, String fullName) {

		try {
			byte[] byteData = getImageByte(urlPath);
			if (byteData == null) {
				Log.e(TAG, "没有得到图片的byte，问题可能是path：" + urlPath);
				return null;
			}
			saveBitmap(fullName, byteData);
			return Bimp.revitionOriginalImageSize(fullName);
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取图片的byte数组
	 * 
	 * @param urlPath
	 * @return
	 */
	private static byte[] getImageByte(String urlPath) {
		InputStream in = null;
		byte[] result = null;
		try {
			int index = urlPath.lastIndexOf("/");
			String firstString = urlPath.substring(0, index + 1);
			String secondString = urlPath.substring(index + 1, urlPath.length());
			urlPath = firstString + Uri.encode(secondString);
			URL url = new URL(urlPath);
			HttpURLConnection httpURLconnection = (HttpURLConnection) url
					.openConnection();
			httpURLconnection.setConnectTimeout(10000);  
			httpURLconnection.setReadTimeout(10000);  
			httpURLconnection.setDoInput(true);
			httpURLconnection.connect();
			if (httpURLconnection.getResponseCode() == 200) {
				in = httpURLconnection.getInputStream();
				result = readInputStream(in, httpURLconnection.getContentLength());
				in.close();
			} else {
				Log.e(TAG, "下载图片失败，状态码是：" + httpURLconnection.getResponseCode());
			}
		} catch (Throwable e) {
			Log.e(TAG, "下载图片失败，原因是：" + e.toString());
			e.printStackTrace();
		} finally {
			Log.e(TAG, "下载图片失败!");
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 将输入流转为byte数组
	 * 
	 * @param in
	 * @param length 
	 * @return
	 * @throws Throwable
	 */
	private static byte[] readInputStream(InputStream in, int length) throws Exception {
		int progress = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
			try {
				if (originalTask != null) {
					progress += len;
					originalTask.updateProgress(progress * 100 / length);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		baos.close();
		in.close();
		if (originalTask != null) {
			originalTask.updateProgress(100);
		}
		return baos.toByteArray();

	}

	/**
	 * 此方法用来异步加载图片
	 * 
	 * @param imageview
	 * @param path
	 * @param downLoadImageFilePlay 
	 */
	public static void downloadAsyncTask(final ImageView imageview,
			final String path, final DownLoadFileInterface downLoadImageFilePlay) {
		new AsyncTask<String, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				return getBitmap(path);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				if (result != null && imageview != null) {
					downLoadImageFilePlay.show(imageview, getImageFullName(path));
				} else {
					Log.e(TAG, "在downloadAsyncTask里异步加载图片失败！");
				}
			}
			
			protected void onPreExecute() {
				System.out.println();
			};

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] {});

	}
	
	/**
	 * 此方法用来异步加载图片
	 * 
	 * @param imageview
	 * @param path
	 * @param downLoadImageFilePlay 
	 */
	public static void downloadAsyncTask(final ImageView imageview,
			final String path, final DownLoadHeadCallBack downLoadImageFilePlay) {
		new AsyncTask<String, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				return getBitmap(path);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				if (result != null && imageview != null) {
					downLoadImageFilePlay.show(path, imageview, getImageFullName(path));
				} else {
					Log.e(TAG, "在downloadAsyncTask里异步加载图片失败！");
				}
			}

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] {});

	}
	
	
	private static OriginalTask originalTask;
	
	public static class OriginalTask extends AsyncTask<String, Integer, Bitmap>{
		
		private ImageView imageview;
		private String path;
		private DownLoadOriginalInterface downLoadFile;
		
		public OriginalTask(ImageView imageview, String path, DownLoadOriginalInterface downLoadFile){
			this.imageview = imageview;
			this.path = path;
			this.downLoadFile = downLoadFile;
		}
		
		public void updateProgress(Integer... values){
			publishProgress(values);
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			return getBitmap(path);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result != null && imageview != null) {
				if (downLoadFile != null) {
					downLoadFile.show(imageview, getImageFullName(path));
				}
			} else {
				if (downLoadFile != null) {
					downLoadFile.show(imageview, "");
				}
				Log.e(TAG, "在downloadAsyncTask里异步加载图片失败！");
			}
			originalTask = null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			if (downLoadFile != null) {
				downLoadFile.onProgressUpdate(values);
			}
		}

	}
	
	/**
	 * 此方法用来异步加载原图
	 * 
	 * @param imageview
	 * @param path
	 * @param downLoadFile 
	 */
	public static void downloadOriginalTask(ImageView imageview,
			String path, DownLoadOriginalInterface downLoadFile) {
		originalTask = new OriginalTask(imageview, path, downLoadFile);
		originalTask.execute(new String[] {});
	}
	
	public static void downloadHeadPhoto(final ImageView imageview,
			final String path, final DownLoadFileInterface downLoadImageFilePlay) {
		new AsyncTask<String, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				return getBitmap(path);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				if (result != null && imageview != null) {
					downLoadImageFilePlay.downloadHeadPhotoFinished(imageview, getImageFullName(path));
				} else {
					Log.e(TAG, "在downloadAsyncTask里异步加载图片失败！");
				}
			}
			
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] {});

	}
	
	public static void downloadAsyncTaskBnt(final ImageButton imageview,
			final String path) {
		new AsyncTask<String, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				return getBitmap(path);
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				if (result != null && imageview != null) {
					imageview.setImageBitmap(result);
				} else {
					Log.e(TAG, "在downloadAsyncTask里异步加载图片失败！");
				}
			}

		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] {});

	}

}
