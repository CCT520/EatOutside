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
	private User user;		// 声明一个User对象
	private Handler handler;	//声明一个Handler
	private String original;	// 声明一个存储Server返回的原始数据的字符串
	private String result;		// 声明一个original经过提取的结果字符串
	private TextView nameTV;	// 声明一个代表姓名的TextView
	private TextView genderTV;
	private TextView birthTV;
	private ImageView pic;		// 声明一个代表用户头像的ImageView
	private Bitmap bitmap;		// 声明一个用户头像的bitmap
	private ProgressDialog pdialog;		// 进度框
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Button setting;		// 声明一个表示个人信息设置的按钮
	
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
		// 显示进度框
		pdialog=ProgressDialog.show(UserInfo.this, "正在加载...", "系统正在处理您的请求");
		// 新开启一个线程来从服务器获取用户信息
		new Thread(new GetUserFromServer()).start();
		// 为setting按钮设置事件，如果user！=null则可以转到第9页
		setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(user!=null){
					Intent intent3 = new Intent();
					Bundle bundle = new Bundle();	//实例化bundle
					bundle.putSerializable("user", user);	//将user对象保存到bundle中
					intent3.setClass(UserInfo.this,UserInfoUpdate.class);
					intent3.putExtras(bundle);		// 保存bundle到Intent中
					// 将bitmap转成字节再传过去！
					ByteArrayOutputStream baos=new ByteArrayOutputStream();  
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
					byte [] bitmapByte =baos.toByteArray();  
					intent3.putExtra("bitmap", bitmapByte);  
					Log.i("Page9", "ToPage9");
					startActivity(intent3); 
				}else{
					Message m = new Message();
					m.what = 0x102;	//从读取用户信息线程发送
					handler.sendMessage(m);	// 发送消息
				}
					
			}
		});
		// 设置handler，显示用户名字
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(msg.what==0x101){
					pdialog.dismiss();
					if(!ConnectServer.FAIL.equals(result) && null!=user){	// 请求成功,有数据
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
						// 从服务器获取图片
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
				m.what = 0x101;	//从读取用户信息线程发送
				handler.sendMessage(m);	// 发送消息
			}
		}
		
		public void alert(){
			new AlertDialog.Builder(UserInfo.this).setTitle("提示")//设置提示
			.setMessage(ConnectServer.FAIL)// 设置显示内容
			.setNegativeButton("返回", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			}).show();
		}
		
	}
	
