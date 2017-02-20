package com.example.aaa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.example.tool.DemoApiTool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.model.Business; 

public class InitPage extends Activity{

	private List<Business> list;
	private Handler handler;
	private DemoApplication DemoApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init);
		DemoApp = (DemoApplication) getApplication(); 
		DemoApp.preferences  = getSharedPreferences("crazyit",MODE_WORLD_READABLE);
		DemoApp.editor = DemoApp.preferences.edit();
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				list = Business.GetInfor(get());
				Log.i("size", ""+list.size());
				Message m = new Message();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				m.what = 0x101;	//�Ӷ�ȡ�û���Ϣ�̷߳���
				handler.sendMessage(m);	// ������Ϣ
			}
			
		}).start();
		
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("b1", list.get(0));
				bundle.putSerializable("b2", list.get(1));
				bundle.putSerializable("b3", list.get(2));
				intent.putExtras(bundle);
				intent.setClass(InitPage.this, MainTabActivity.class);
				startActivity(intent);
				finish();
				super.handleMessage(msg);
			}
			
		};
		
	}

	public String get(){
		// �Ӵ��ڵ�����ȡ���̻��ַ�������ת����Business����
        String singleUrl = "http://api.dianping.com/v1/business/find_businesses?";
        Map<String, String> param = new HashMap<String, String>();
        param.put("category", "��ʳ");
        param.put("city", "����");
        param.put("limit", "3");
        param.put("sort", "2");
        return DemoApiTool.requestApi(singleUrl, DemoApiTool.appKey, DemoApiTool.secret, param);
       
	}
}
