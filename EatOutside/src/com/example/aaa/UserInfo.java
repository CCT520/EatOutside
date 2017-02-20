package com.example.aaa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

import org.apache.http.conn.ConnectTimeoutException;

import com.example.model.User;
import com.example.tool.ConnectServer;
import com.example.tool.ConvertTool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;


public class UserInfo extends Activity implements OnClickListener{
	private User user;		// ����һ��User����
	private Handler handler;	//����һ��Handler
	private String original;	// ����һ���洢Server���ص�ԭʼ���ݵ��ַ���
	private String result;		// ����һ��original������ȡ�Ľ���ַ���
	private TextView nameTV;	// ����һ������������TextView
	private TextView genderTV;
	private TextView birthTV;
	private ImageView pic;		// ����һ�������û�ͷ���ImageView
	private Bitmap bitmap;		// ����һ���û�ͷ���bitmap
	private ProgressDialog pdialog;		// ���ȿ�
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Button setting;		// ����һ����ʾ������Ϣ���õİ�ť
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.page2);
		findViewById(R.id.collection).setOnClickListener(this);  
		findViewById(R.id.bug).setOnClickListener(this); 
		pic = (ImageView) findViewById(R.id.imagephoto);
		nameTV = (TextView) findViewById(R.id.name);
		genderTV = (TextView) findViewById(R.id.genderS);
		birthTV = (TextView) findViewById(R.id.birthS);
		setting = (Button) findViewById(R.id.install);
		// ��ʾ���ȿ�
		pdialog=ProgressDialog.show(UserInfo.this, "���ڼ���...", "ϵͳ���ڴ�����������");
		// �¿���һ���߳����ӷ�������ȡ�û���Ϣ
		new Thread(new GetUserFromServer()).start();
		// Ϊsetting��ť�����¼������user��=null�����ת����9ҳ
		setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(user!=null){
					Intent intent3 = new Intent();
					Bundle bundle = new Bundle();	//ʵ����bundle
					bundle.putSerializable("user", user);	//��user���󱣴浽bundle��
					intent3.setClass(UserInfo.this,UserInfoUpdate.class);
					intent3.putExtras(bundle);		// ����bundle��Intent��
					// ��bitmapת���ֽ��ٴ���ȥ��
					ByteArrayOutputStream baos=new ByteArrayOutputStream();  
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
					byte [] bitmapByte =baos.toByteArray();  
					intent3.putExtra("bitmap", bitmapByte);  
					Log.i("Page9", "ToPage9");
					startActivity(intent3); 
				}else{
					Message m = new Message();
					m.what = 0x102;	//�Ӷ�ȡ�û���Ϣ�̷߳���
					handler.sendMessage(m);	// ������Ϣ
				}
					
			}
		});
		// ����handler����ʾ�û�����
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what==0x101){
					pdialog.dismiss();
					if(!ConnectServer.FAIL.equals(result) && null!=user){	// ����ɹ�,������
						nameTV.setText(user.getUserName());
						genderTV.setText(user.getGender());
						birthTV.setText(sdf.format(user.getBirth()));
					}
					else
						alert();
				}
				if(msg.what==0x102)
					alert();
			}
		};
	}// end of onCreate
		public void onClick(View view) {  
		      switch (view.getId()) {  
		      case R.id.collection:
				Intent intent2 = new Intent();
				intent2.setClass(UserInfo.this,CollectionPage.class);
				startActivity(intent2);  
				break;
		      case R.id.bug:
				Intent intent4 = new Intent();
				intent4.setClass(UserInfo.this,BugReport.class);
				startActivity(intent4);  
				break;
			  default:
				  break;
		      }
		}
		class GetUserFromServer implements Runnable{

			@Override
			public void run() {
				
					String action = "returnUser.action";
					original = ConnectServer.send(action);
					Log.i("return", original);
					if(!original.equals(ConnectServer.FAIL)){
						result = ConvertTool.convertToUserString(original);
						if(result!=null)
							user = ConvertTool.convertToUserObject(result);
						else
							result = ConnectServer.FAIL;
						// �ӷ�������ȡͼƬ
						bitmap = ConnectServer.getPicture(ConnectServer.picPath);
						pic.post(new Runnable(){
							@Override
							public void run() {
								pic.setImageBitmap(bitmap);
							}
						});
					}else
						result = ConnectServer.FAIL;
				
				Message m = new Message();
				m.what = 0x101;	//�Ӷ�ȡ�û���Ϣ�̷߳���
				handler.sendMessage(m);	// ������Ϣ
			}
		}
		
		public void alert(){
			new AlertDialog.Builder(UserInfo.this).setTitle("��ʾ")//������ʾ
			.setMessage(ConnectServer.FAIL)// ������ʾ����
			.setNegativeButton("����", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}
		
	}
	
