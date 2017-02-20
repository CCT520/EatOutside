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
	public final static String FAIL = "���������ʧ��";	// ��������ʧ�ܵĳ����ַ���
	public final static String SUCCESS = "success";
	public final static String ISEXISTED = "isExisted";	// ������̻��Ѿ��ղ���
	public final static String target = "http://192.168.0.102:8080/EatOutside/";
	public final static String picPath = target + "image/user.jpg";
	private static String original;		// ����һ���洢������������Ϣ��String
	/**
	 * 
	 * @param action �����URL
	 * @return �������ɹ������شӷ������õ�����Ϣ
	 * 			�������ʧ�ܣ����ء����������ʧ�ܡ�����FAIL
	 */
	public static String send(String action){
		String targetURL =target + action; //Ҫ�ύ��Ŀ���ַ
		HttpClient httpclient = new DefaultHttpClient();	// ����HttpClient
		// ��������ʱʱ��Ͷ�ȡ��ʱʱ��
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5*1000);
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5*1000);
		HttpGet httpRequest = new HttpGet(targetURL);			// ����HttpGet���Ӷ���
		Log.d("url", targetURL);
		HttpResponse httpResponse;
		try {
			httpResponse = httpclient.execute(httpRequest);	//ִ��HttpClient����
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				original = EntityUtils.toString(httpResponse.getEntity());		// ��ȡ���ص��ַ���
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
	// ������ַ��ȡͼƬ
	public static Bitmap getPicture(String path){
		Bitmap bm = null;
		try {
			URL url = new URL(path);	//����URL����
			URLConnection conn = url.openConnection();//��ȡURL�����Ӧ������
			conn.connect();				// ������
			InputStream is = conn.getInputStream();		//��ȡ������
			bm = BitmapFactory.decodeStream(is);		// ��ȡ����������
		}catch(MalformedURLException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return bm;
	}
}
