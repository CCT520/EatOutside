package com.example.tool;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ConnectServer {
	public final static String FAIL = "请求服务器失败";	// 代表请求失败的常量字符串
	public final static String SUCCESS = "success";
	public final static String ISEXISTED = "isExisted";	// 代表该商户已经收藏了
	public final static String target = "http://192.168.0.102:8080/EatOutside/";
	public final static String picPath = target + "image/user.jpg";
	private static String original;		// 声明一个存储服务器返回信息的String
	/**
	 * 
	 * @param action 请求的URL
	 * @return 如果请求成功，返回从服务器得到的消息
	 * 			如果请求失败，返回“请求服务器失败”，即FAIL
	 */
	public static String send(String action){
		String targetURL =target + action; //要提交的目标地址
		HttpClient httpclient = new DefaultHttpClient();	// 创建HttpClient
		// 设置请求超时时间和读取超时时间
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5*1000);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5*1000);
		HttpGet httpRequest = new HttpGet(targetURL);			// 创建HttpGet连接对象
		Log.d("url", targetURL);
		HttpResponse httpResponse;
		try {
			httpResponse = httpclient.execute(httpRequest);	//执行HttpClient请求
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				original = EntityUtils.toString(httpResponse.getEntity());		// 获取返回的字符串
			}
			else
				original = FAIL;
		}catch(ConnectTimeoutException e){
			e.printStackTrace();
			original = FAIL;
		}catch(ClientProtocolException e){
			e.printStackTrace();
			original = FAIL;
		}catch(IOException e){
			e.printStackTrace();
			original = FAIL;
		}
		Log.i("test", "original="+original);
		return original;
	}
	// 根据网址获取图片
	public static Bitmap getPicture(String path){
		Bitmap bm = null;
		try {
			URL url = new URL(path);	//创建URL对象
			URLConnection conn = url.openConnection();//获取URL对象对应的链接
			conn.connect();				// 打开连接
			InputStream is = conn.getInputStream();		//获取输入流
			bm = BitmapFactory.decodeStream(is);		// 获取输入流对象
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return bm;
	}
}
