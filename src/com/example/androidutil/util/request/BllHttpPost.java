package com.example.androidutil.util.request;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;

import com.example.androidutil.util.Conn;

public class BllHttpPost {
	
//	/**
//	 * 发送交流图片
//	 * @param url		
//	 * @param fileUrl
//	 * @return
//	 */
//	public static String sendFriendZonePicture(List<FriendZoneImageBaseInfo> friendZoneImageBaseInfos){
//		String result = "";
//		String target = Conn.URL + "Home/FileUploadRingImg"; // 要提交的目标地址
//		try {
//			HttpPost httpRequest = new HttpPost(target); // 创建HttpPost对象
//			HttpClient httpClient = new DefaultHttpClient();
//			httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Charset.forName("UTF-8"));
//			MultipartEntity mpEntity = new MultipartEntity(); //文件传输
//			for (int i = 0; i < friendZoneImageBaseInfos.size(); i++) {
//				mpEntity.addPart("aaa" + i, new FileBody(new File(friendZoneImageBaseInfos.get(i).getLoaclUrl())));
//			}
//			httpRequest.setEntity(mpEntity);
//			HttpResponse httpResponse = httpClient.execute(httpRequest, new BasicHttpContext());
//			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 如果请求成功
//				result += EntityUtils.toString(httpResponse.getEntity()); // 获取返回的字符串
//			}
//		} catch (Throwable e) {
//			e.printStackTrace();
//		} 
//		return result;
//	}
	
	
	
	
	/**
	 * 发送交流图片
	 * @param url		
	 * @param fileUrl
	 * @return
	 */
	public static String sendPicture(String localUrl){
		String result = "";
		String target = Conn.URL + "/Home/FileUpload"; // 要提交的目标地址
		try {
			HttpPost httpRequest = new HttpPost(target); // 创建HttpPost对象
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Charset.forName("UTF-8"));
			File file = new File(localUrl);
			MultipartEntity mpEntity = new MultipartEntity(); //文件传输
			mpEntity.addPart("aaa", new FileBody(file));
			mpEntity.addPart("bbb", new StringBody(file.getName(), Charset.forName("UTF-8")));
			httpRequest.setEntity(mpEntity);
			HttpResponse httpResponse = httpClient.execute(httpRequest, new BasicHttpContext());
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 如果请求成功
				result += EntityUtils.toString(httpResponse.getEntity()); // 获取返回的字符串
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	/**
	 * 发送图片
	 * @param url		
	 * @param localFileUrl
	 * @return
	 */
	public static String sendPicture(String url, String localFileUrl){
		String result = "";
		String target = Conn.URL + url; // 要提交的目标地址
		try {
			HttpPost httpRequest = new HttpPost(target); // 创建HttpPost对象
			HttpClient httpClient = new DefaultHttpClient();
			httpRequest.setEntity(new FileEntity(new File(localFileUrl), "utf-8"));
			HttpResponse httpResponse = httpClient.execute(httpRequest, new BasicHttpContext());
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 如果请求成功
				result += EntityUtils.toString(httpResponse.getEntity()); // 获取返回的字符串
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static String sendUrl(String url,List<NameValuePair> params) {
		String result = "";
		String target = Conn.URL+url;	//要提交的目标地址
		HttpClient httpclient = new DefaultHttpClient();	//创建HttpClient对象
		HttpPost httpRequest = new HttpPost(target);	//创建HttpPost对象
		//params.add(new BasicNameValuePair("nickname", nickname.getText().toString()));	//昵称
		//params.add(new BasicNameValuePair("content", content.getText().toString()));	//内容
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8")); //设置编码方式
			HttpResponse httpResponse = httpclient.execute(httpRequest);	//执行HttpClient请求
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){	//如果请求成功
				result += EntityUtils.toString(httpResponse.getEntity());	//获取返回的字符串
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String sendErrorLog(String url,List<NameValuePair> params){
		String result = "";
		String target = Conn.URL + url; // 要提交的目标地址
		HttpClient httpclient = new DefaultHttpClient(); // 创建HttpClient对象
		HttpPost httpRequest = new HttpPost(target); // 创建HttpPost对象
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, "utf-8")); // 设置编码方式
			HttpResponse httpResponse = httpclient.execute(httpRequest); // 执行HttpClient请求
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 如果请求成功
				result += EntityUtils.toString(httpResponse.getEntity()); // 获取返回的字符串
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
