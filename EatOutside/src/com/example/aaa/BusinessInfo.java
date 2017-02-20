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
	private ImageView picIV;	// 表示商户图片的ImageView
	private String original;	// 声明一个存储Server返回的原始数据的字符串
	private String result;		// 声明一个original经过提取的结果字符串
	private ProgressDialog pdialog;		// 进度框
	private Button collect;		// 收藏
	private Button Bookbt;      //订餐
	private ImageButton NaviBt;
	private double LatiS;
	private double LongiS;
	private Bitmap bitmap;	//存放商户图片
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
	
	// 从intent中取得商户信息
	Intent intent = getIntent();
	Bundle bundle = intent.getExtras();
	store = (Business) bundle.getSerializable("business");
	LatiS = bundle.getDouble("经度起点");
	LongiS = bundle.getDouble("纬度起点");
	System.out.println("终点起点啊啊啊啊"+LatiS+LongiS+store.getLa()+store.getLong());
	
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
					// 显示商户图片
					picIV.setImageBitmap(ConvertTool.getBitmap(store.getBitmapBytes()));
				}
			});
		}else{
			new Thread(new GetBusinessPic()).start();
		}	
	}
	// 为收藏按钮增加事件
	collect.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			pdialog = ProgressDialog.show(BusinessInfo.this, "正在加载...", "系统正在处理您的请求");
			new Thread(new AddCollection()).start();
		}
	});
	// 新建线程，如果用户是从CollectionPage过来的，collected属性为true，按钮应该设置为不可用
	new Thread(new CollectedBusiness()).start();
	handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0x101){
				pdialog.dismiss();
				if(result.equals(ConnectServer.FAIL)){
					// 构建一个提示框
					new AlertDialog.Builder(BusinessInfo.this).setTitle("提示")//设置提示
					.setMessage(ConnectServer.FAIL)// 设置显示内容
					.setNegativeButton("返回", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
					return;
				}
				if(result.equals(ConnectServer.ISEXISTED)){
					// 构建一个提示框
					new AlertDialog.Builder(BusinessInfo.this).setTitle("提示")//设置提示
					.setMessage("您已经收藏过这家商户！")// 设置显示内容
					.setNegativeButton("返回", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
				}else
					// 构建一个提示框
					new AlertDialog.Builder(BusinessInfo.this).setTitle("提示")//设置提示
					.setMessage("收藏成功！")// 设置显示内容
					.setNegativeButton("返回", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
				// 更改按钮模式
				collect.setText("已收藏");
				collect.setEnabled(false);
			}
			if(msg.what == 0x102){	// 该商户的collected属性为true
				collect.setText("已收藏");
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
			System.out.println("我的全局位置"+Double.valueOf(DemoApp.preferences.getString("我的纬度",String.valueOf(DemoApp.Mylo)))+"dasdsdsd"+Double.valueOf(DemoApp.preferences.getString("我的纬度",String.valueOf(DemoApp.Myla))));
			LatLng pt1 = new LatLng(Double.valueOf(DemoApp.preferences.getString("我的纬度",String.valueOf(DemoApp.Mylo))), Double.valueOf(DemoApp.preferences.getString("我的经度",String.valueOf(DemoApp.Myla))));
			LatLng pt2 = new LatLng(store.getLa(), store.getLong());
			// 构建 导航参数
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
			if(store.getonline_reservation_url().equals("不可预订"))
				Bookbt.setText("我们很抱歉的告诉您，该餐厅不支持在线预定功能");
			else{
				//Bookbt.setText("订餐");
				Intent intent = new Intent();
				Bundle bundle= new Bundle();
				bundle.putString("订餐网址", store.getonline_reservation_url());
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
	 *  class AddCollection，将该商户加入到收藏夹中
	 */
	class AddCollection implements Runnable{

		@Override
		public void run() {
			String action = "addCollection.action?businessId=" + store.getbusi();
			System.out.println(store.getbusi());
			original = ConnectServer.send(action);
			if(!original.equals(ConnectServer.FAIL)){	// 如果请求成功
				result = ConvertTool.convertToMessage(original);	
			}else
				result = ConnectServer.FAIL;
			Message m = new Message();
			m.what = 0x101;	//从读取用户信息线程发送
			handler.sendMessage(m);	// 发送消息
		}
	}
	/**
	 * class CollectedBusiness，如果用户是从收藏夹页面过来的
	 * 那么该商户的collected为true，按钮应该 设置不可用
	 */
	class CollectedBusiness implements Runnable{

		@Override
		public void run() {
			if(store.isCollected() == true){
				Message m = new Message();
				m.what = 0x102;	//从读取用户信息线程发送
				handler.sendMessage(m);	// 发送消息
			}
		}
		
	}
	/**
	 * class GetBusinessPic 用于在商户信息中图片字节流为空时，为其读取照片
	 */
	class GetBusinessPic implements Runnable{

		@Override
		public void run() {
			System.out.println("——————————————————————————————————————————图片地址"+store.getS_photo_url());
			bitmap = ConnectServer.getPicture(store.getS_photo_url());
			Message m = new Message();
			m.what = 0x103;	//从读取用户信息线程发送
			handler.sendMessage(m);	// 发送消息
		}
		
	}
}