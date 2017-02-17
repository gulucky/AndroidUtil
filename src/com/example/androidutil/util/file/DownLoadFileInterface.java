package com.example.androidutil.util.file;

import android.widget.ImageView;

public interface DownLoadFileInterface {
	public void downloadVideoFinished(String servervideoUrl, String videoUrl, ImageView imageView);
	public void downloadFileFinished(String downloadFileServerUrl, String fileUrl);
	public void play(String voiceLocalUrl);
	public void show(ImageView imageView, String imageLocalUrl);
	public void downloadHeadPhotoFinished(ImageView imageView, String imageLocalUrl);
}
