package com.example.androidutil.util.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.example.androidutil.util.Conn;
import com.example.androidutil.util.Util;

/**
 * 文件下载工具类
 * */
public class FileHelper {
	
	/**
	 * 图片基准路径
	 */
	public static final String BASE_SDCARD_IMAGES = Util.getInstance()
			.getExtPath() + "/" + Conn.FILE_URL + "/";
	
	public static final String AMR_FORMAT = ".amr";
	public static final String MP4_FORMAT = ".mp4";

	/**
	 * 判断文件是否存在
	 * 
	 * @param 文件在本地的完整名
	 * @return
	 */
	public static boolean judgeExists(String fullName) {
		boolean isExit = false;
		try {
			File file = new File(fullName);
			isExit = file.exists();
			if (isExit) {
				long size = FileSizeUtil.getFileSizes(file);
				if (size <= 0) {
					isExit = false;
					file.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExit;
	}

	/**
	 * 获取最后的‘/’后的文件名
	 * 
	 * @param name
	 * @return
	 */
	private static String getLastName(String name) {
		int lastIndexOf = 0;
		try {
			name = name.replace("http://", "");
			lastIndexOf = name.indexOf('/');
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return !name.equals("") ? name.substring(lastIndexOf + 1) : "";
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

	public static File DownloadFromUrlToData(String serverUrl) {
		FileOutputStream fout = null; 
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			String fullName = getImageFullName(serverUrl);
			if (FileHelper.judgeExists(fullName)) {
				return new File(fullName);
			}
			int index = serverUrl.lastIndexOf("/");
			String firstString = serverUrl.substring(0, index + 1);
			String secondString = serverUrl.substring(index + 1, serverUrl.length());
			serverUrl = firstString + Uri.encode(secondString);
			URL url = new URL(serverUrl);
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection();
			urlCon.setConnectTimeout(10000);
			urlCon.setReadTimeout(10000);
			/* Open a connection to that URL. */
			URLConnection ucon = url.openConnection();
			/*
			 * * Define InputStreams to read from the URLConnection.
			 */
			is = ucon.getInputStream();
			
			creatFolder(fullName);
			File file = new File(fullName);
			if (!file.exists()) {
				creatFolder(fullName);
				file.createNewFile();
			}
			fout = new FileOutputStream(file);
			bis = new BufferedInputStream(is);
			
			byte[] buffer = new byte[4*1024];  
			int count;
			while ((count = bis.read(buffer)) != -1) {
				fout.write(buffer, 0, count); 
			}
			return file;
		} catch (Throwable e) {
			return null;
		}finally{  
            try {  
            	if (fout != null) {
            		fout.close();  
				}
            	if (is != null) {
					is.close();
				}
            	if (bis != null) {
					bis.close();
				}
            } catch (Exception e) {  
            	fout = null;
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
//		if (getLastName(fullName).contains(".")) {
			try {
				String newFilePath = fullName.substring(0,
						fullName.lastIndexOf('/'));
				File file = new File(newFilePath);
				file.mkdirs();
			} catch (Throwable e) {
				e.printStackTrace();
			}
//		}
	}
	
	//文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
	public static int CopySdcardFile(String fromFile, String serverUrl){
		try{
			String fullName = getImageFullName(serverUrl);
			if (FileHelper.judgeExists(fullName)) {
				return 0;
			}
			creatFolder(fullName);
			
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(fullName);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0){
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return 0;
		} catch (Throwable ex){
			ex.printStackTrace();
			return -1;
		}

	}
	
	/**
	 * 保存聊天图片或视频
	 * @param fromFile
	 * @param pastFileName
	 * @return
	 */
	public static String saveCopyFile(String fromFile, String fullName){
		String result = "";
		try{
//			String fullName = Util.getInstance().getExtPath() + "/" + IMApplication.getInstance().getResources().getString(R.string.app_name) +"/" + pastFileName;
			creatFolder(fullName);
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(fullName);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0){
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			result = fullName;
		} catch (Throwable ex){
			ex.printStackTrace();
		}
		return result;

	}
	
	/**
	 * 保存头像
	 * @param fromFile
	 * @param pastFileName
	 * @return
	 */
	public static String saveHeadPhotoCopyFile(String fromFile, String fullName){
		String result = "";
		try{
			creatFolder(fullName);
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(fullName);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0){
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			result = fullName;
		} catch (Throwable ex){
			ex.printStackTrace();
		}
		return result;

	}
	
	public static void downloadFile(final String downloadFileServerUrl, final DownLoadFileInterface downLoadFilePlay) {
		new AsyncTask<String, Integer, File>() {
			@Override
			protected File doInBackground(String... params) {
				try {
					return DownloadFromUrlToData(downloadFileServerUrl);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(File result) {
				super.onPostExecute(result);
				try {
					if (downLoadFilePlay != null) {
						downLoadFilePlay.downloadFileFinished(downloadFileServerUrl,result.getPath());
					}
				} catch (Throwable e) {
					if (downLoadFilePlay != null) {
						downLoadFilePlay.downloadFileFinished(downloadFileServerUrl, "");
					}
					e.printStackTrace();
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] {});
	}
	
	public static void downloadVideo(final ImageView imageView, final String downloadFileServerUrl, final DownLoadFileInterface downLoadFilePlay) {
		new AsyncTask<String, Integer, File>() {
			@Override
			protected File doInBackground(String... params) {
				try {
					return DownloadFromUrlToData(downloadFileServerUrl);
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(File result) {
				super.onPostExecute(result);
				try {
					if (downLoadFilePlay != null && result != null) {
						downLoadFilePlay.downloadVideoFinished(downloadFileServerUrl, result.getPath(), imageView);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] {});
	}
	
}
