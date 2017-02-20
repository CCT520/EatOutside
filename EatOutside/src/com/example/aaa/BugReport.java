package com.example.aaa;

import com.example.tool.ConnectServer;
import com.example.tool.ConvertTool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;

public class BugReport extends Activity implements OnClickListener{
	
	private final String success = "提交成功";
	private Button submit;	// 声明一个提交bug按钮的对象
	private EditText contentET;	// 声明一个bug内容的EditText
	private String content;		//声明一个存bug内容的String
	private String original;	//接收来自服务器原始数据
	private String result;		// 表示结果的字符串
	private Handler handler; //声明一个Handler
	
	private int flag =0;	//第二次及以后点击bug内容EditText都不会重置
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page7);
		findViewById(R.id.back).setOnClickListener(this); 
		findViewById(R.id.main).setOnClickListener(this); 
		findViewById(R.id.person).setOnClickListener(this); 
		// 为EditText设置点击事件（清空内容）
		contentET = (EditText) findViewById(R.id.bugsubmit);
		contentET.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(0==flag){
					contentET.setText("");	// 如果是第一次点，清除内容
					flag++;
				}
				else
					return;
			}
		});
		// 设置bug提交按钮事件
		submit = (Button) findViewById(R.id.support);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				content = contentET.getText().toString();	// 得到bug内容
				if(content.equals("")){
					// 构建一个提示框
					new AlertDialog.Builder(BugReport.this).setTitle("提示")//设置提示
					.setMessage("Bug内容不能为空")// 设置显示内容
					.setNegativeButton("返回", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					}).show();
					return;
				}
				new Thread(new BugSubmit()).start();		// 开启线程
			}
		});
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(result.equals(ConnectServer.SUCCESS)){
					// 构建一个提示框
					new AlertDialog.Builder(BugReport.this).setTitle("成功")//设置提示
					.setMessage(success+",感谢您的支持！")// 设置显示内容
					.setNegativeButton("返回", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					}).show();
					contentET.setText("");		//重置EditText为空
				}else{
					//Toast.makeText(Page7.this, fail, Toast.LENGTH_SHORT).show();//显示提交成功
					// 构建一个提示框
					new AlertDialog.Builder(BugReport.this).setTitle("失败")//设置提示
					.setMessage(ConnectServer.FAIL)// 设置显示内容
					.setNegativeButton("返回", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					}).show();
				}
			}
		};
	}
	public void onClick(View view) {  
	      switch (view.getId()) {  
	      case R.id.back:
				finish(); 
				break;
	      case R.id.main:
				Intent intent2 = new Intent();
				intent2.setClass(BugReport.this,MainPage.class);
				startActivity(intent2);  
				break;
	      case R.id.person:
				Intent intent3 = new Intent();
				intent3.setClass(BugReport.this,UserInfo.class);
				startActivity(intent3);  
		  default: 
		   		break;
		 	}
	}
	
	class BugSubmit implements Runnable{
		@Override
		public void run() {
			String action = "addBug.action?content="+content;
			original = ConnectServer.send(action);
			if(!original.equals(ConnectServer.FAIL)){
				result = ConvertTool.convertToMessage(original);
			}else
				result = ConnectServer.FAIL;
			
			Message m = handler.obtainMessage();	//获取一个Message
			handler.sendMessage(m);	// 发送消息
		}
	}
}
	
