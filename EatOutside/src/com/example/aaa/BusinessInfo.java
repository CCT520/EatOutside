package com.example.aaa;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.example.model.Business;
import com.example.search.BookMeal;
import com.example.search.SearchNearResturant;
import com.example.tool.ConnectServer;
import com.example.tool.ConvertTool;
import com.example.search.BookMeal;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class BusinessInfo extends Activity implements OnClickListener {
	
	private DemoApplication DemoApp;
	private Business store;
	private TextView address,rating,name,costAvg,tel;
	private Handler handler;
	private ImageView picIV;	// ��ʾ�̻�ͼƬ��ImageView
	private String original;	// ����һ���洢Server���ص�ԭʼ���ݵ��ַ���
	private String result;		// ����һ��original������ȡ�Ľ���ַ���
	private ProgressDialog pdialog;		// ���ȿ�
	private Button collect;		// �ղ�
	private Button Bookbt;      //����
	private ImageButton NaviBt;
	private double LatiS;
	private double LongiS;
	private Bitmap bitmap;	//����̻�ͼƬ
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DemoApp = (DemoApplication) getApplication(); 
		setContentView(R.layout.page6);
		name = (TextView) findViewById(R.id.name);
		address = (TextView) findViewById(R.id.address);
		rating = (TextView) findViewById(R.id.rating);
		costAvg = (TextView) findViewById(R.id.costAve);
		tel = (TextView) findViewById(R.id.tel);
		picIV = (ImageView) findViewById(R.id.imageView1);
		collect = (Button) findViewById(R.id.collect);
		NaviBt = (ImageButton) findViewById(R.id.imageButtonNavi);
		Bookbt = (Button) findViewById(R.id.booknow);
	// find first page button
	findViewById(R.id.back).setOnClickListener(this); 
	
	// ��intent��ȡ���̻���Ϣ
	Intent intent = getIntent();
	Bundle bundle = intent.getExtras();
	store = (Business) bundle.getSerializable("business");
	LatiS = bundle.getDouble("�������");
	LongiS = bundle.getDouble("γ�����");
	System.out.println("�յ���㰡������"+LatiS+LongiS+store.getLa()+store.getLong());
	
	if(store!=null){
		name.setText(store.getNa());
		tel.setText(store.getTel());
		address.setText(store.getAddr());
		costAvg.setText(store.getPrice());
		rating.setText(store.getRating());
		if(store.getBitmapBytes()!=null){
			picIV.post(new Runnable(){
				@Override
				public void run() {
					// ��ʾ�̻�ͼƬ
					picIV.setImageBitmap(ConvertTool.getBitmap(store.getBitmapBytes()));
				}
			});
		}else{
			new Thread(new GetBusinessPic()).start();
		}	
	}
	// Ϊ�ղذ�ť�����¼�
	collect.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			pdialog = ProgressDialog.show(BusinessInfo.this, "���ڼ���...", "ϵͳ���ڴ�����������");
			new Thread(new AddCollection()).start();
		}
	});
	// �½��̣߳�����û��Ǵ�CollectionPage�����ģ�collected����Ϊtrue����ťӦ������Ϊ������
	new Thread(new CollectedBusiness()).start();
	handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0x101){
				pdialog.dismiss();
				if(result.equals(ConnectServer.FAIL)){
					// ����һ����ʾ��
					new AlertDialog.Builder(BusinessInfo.this).setTitle("��ʾ")//������ʾ
					.setMessage(ConnectServer.FAIL)// ������ʾ����
					.setNegativeButton("����", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
					return;
				}
				if(result.equals(ConnectServer.ISEXISTED)){
					// ����һ����ʾ��
					new AlertDialog.Builder(BusinessInfo.this).setTitle("��ʾ")//������ʾ
					.setMessage("���Ѿ��ղع�����̻���")// ������ʾ����
					.setNegativeButton("����", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
				}else
					// ����һ����ʾ��
					new AlertDialog.Builder(BusinessInfo.this).setTitle("��ʾ")//������ʾ
					.setMessage("�ղسɹ���")// ������ʾ����
					.setNegativeButton("����", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
				// ���İ�ťģʽ
				collect.setText("���ղ�");
				collect.setEnabled(false);
			}
			if(msg.what == 0x102){	// ���̻���collected����Ϊtrue
				collect.setText("���ղ�");
				collect.setEnabled(false);
			}
			if(msg.what == 0x103 && bitmap!=null)
				picIV.setImageBitmap(bitmap);
			super.handleMessage(msg);
		}
	};
	
	NaviBt.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			System.out.println("�ҵ�ȫ��λ��"+Double.valueOf(DemoApp.preferences.getString("�ҵ�γ��",String.valueOf(DemoApp.Mylo)))+"dasdsdsd"+Double.valueOf(DemoApp.preferences.getString("�ҵ�γ��",String.valueOf(DemoApp.Myla))));
			LatLng pt1 = new LatLng(Double.valueOf(DemoApp.preferences.getString("�ҵ�γ��",String.valueOf(DemoApp.Mylo))), Double.valueOf(DemoApp.preferences.getString("�ҵľ���",String.valueOf(DemoApp.Myla))));
			LatLng pt2 = new LatLng(store.getLa(), store.getLong());
			// ���� ��������
			NaviPara para = new NaviPara();
			para.startPoint = pt1;
			para.endPoint = pt2;
			BaiduMapNavigation.openWebBaiduMapNavi(para, BusinessInfo.this);
		}
		
	});
	
	Bookbt.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(store.getonline_reservation_url().equals("����Ԥ��"))
				Bookbt.setText("���Ǻܱ�Ǹ�ĸ��������ò�����֧������Ԥ������");
			else{
				//Bookbt.setText("����");
				Intent intent = new Intent();
				Bundle bundle= new Bundle();
				bundle.putString("������ַ", store.getonline_reservation_url());
				intent.putExtras(bundle);
				intent.setClass(BusinessInfo.this,BookMeal.class);
			    startActivity(intent);
			}
			
		}
		
	});
	
}
	public void onClick(View view) {  
      switch (view.getId()) {  
           case R.id.back:  
        	    finish();
                break;            
            default:  
                break;  
        }  
    }  
	/**
	 *  class AddCollection�������̻����뵽�ղؼ���
	 */
	class AddCollection implements Runnable{

		@Override
		public void run() {
			String action = "addCollection.action?businessId=" + store.getbusi();
			System.out.println(store.getbusi());
			original = ConnectServer.send(action);
			if(!original.equals(ConnectServer.FAIL)){	// �������ɹ�
				result = ConvertTool.convertToMessage(original);	
			}else
				result = ConnectServer.FAIL;
			Message m = new Message();
			m.what = 0x101;	//�Ӷ�ȡ�û���Ϣ�̷߳���
			handler.sendMessage(m);	// ������Ϣ
		}
	}
	/**
	 * class CollectedBusiness������û��Ǵ��ղؼ�ҳ�������
	 * ��ô���̻���collectedΪtrue����ťӦ�� ���ò�����
	 */
	class CollectedBusiness implements Runnable{

		@Override
		public void run() {
			if(store.isCollected() == true){
				Message m = new Message();
				m.what = 0x102;	//�Ӷ�ȡ�û���Ϣ�̷߳���
				handler.sendMessage(m);	// ������Ϣ
			}
		}
		
	}
	/**
	 * class GetBusinessPic �������̻���Ϣ��ͼƬ�ֽ���Ϊ��ʱ��Ϊ���ȡ��Ƭ
	 */
	class GetBusinessPic implements Runnable{

		@Override
		public void run() {
			System.out.println("������������������������������������������������������������������������������������ͼƬ��ַ"+store.getS_photo_url());
			bitmap = ConnectServer.getPicture(store.getS_photo_url());
			Message m = new Message();
			m.what = 0x103;	//�Ӷ�ȡ�û���Ϣ�̷߳���
			handler.sendMessage(m);	// ������Ϣ
		}
		
	}
}