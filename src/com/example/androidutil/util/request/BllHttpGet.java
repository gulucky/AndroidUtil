package com.example.androidutil.util.request;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.example.androidutil.util.Conn;
public class BllHttpGet {
	
	
	public static String sendOtherUrl(String url) {
		String result = "";
		try {
			String target = url; // 要提交的目标地址
			HttpClient httpclient = new DefaultHttpClient();// 创建HttpClient对象
			HttpGet httpRequest = new HttpGet(target); // 创建HttpGet连接对象
			HttpResponse httpResponse;

			httpResponse = httpclient.execute(httpRequest); // 执行HttpClient请求
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity()); // 获取返回的字符串
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}
	
	public static String sendUrl(String param) {
		String result = "";
		try {
			String target = Conn.URL + param + Conn.getParamString(param); // 要提交的目标地址
			HttpClient httpclient = new DefaultHttpClient();// 创建HttpClient对象
			HttpGet httpRequest = new HttpGet(target); // 创建HttpGet连接对象
			HttpResponse httpResponse;

			httpResponse = httpclient.execute(httpRequest); // 执行HttpClient请求
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(httpResponse.getEntity()); // 获取返回的字符串
			}
		} catch (Exception e) {
			e.getMessage();
		}
		return result;
	}
	
}
