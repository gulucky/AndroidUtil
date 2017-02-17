package com.example.androidutil.util.image;

import android.widget.ImageView;

public interface DownLoadOriginalInterface {
	public void show(ImageView imageView, String imageLocalUrl);
	public void onProgressUpdate(Integer... values);
}
