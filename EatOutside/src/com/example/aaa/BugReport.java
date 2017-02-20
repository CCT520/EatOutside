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
	
	private final String success = "�ύ�ɹ�";
	private Button submit;	// ����һ���ύbug��ť�Ķ���
	private EditText contentET;	// ����һ��bug���ݵ�EditText
	private String content;		//����һ����bug���ݵ�String
	private String original;	//�������Է�����ԭʼ����
	private String result;		// ��ʾ������ַ���
	private Handler handler; //����һ��Handler
	
	private int flag =0;	//�ڶ��μ��Ժ���bug����EditText����������
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page7);
		findViewById(R.id.back).setOnClickListener(this); 
		findViewById(R.id.main).setOnClickListener(this); 
		findViewById(R.id.person).setOnClickListener(this); 
		// ΪEditText���õ���¼���������ݣ�
		contentET = (EditText) findViewById(R.id.bugsubmit);
		contentET.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(0==flag){
					contentET.setText("");	// ����ǵ�һ�ε㣬�������
					flag++;
				}
				else
					return;
			}
		});
		// ����bug�ύ��ť�¼�
		submit = (Button) findViewById(R.id.support);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {	
				content = contentET.getText().toString();	// �õ�bug����
				if(content.equals("")){
					// ����һ����ʾ��
					new AlertDialog.Builder(BugReport.this).setTitle("��ʾ")//������ʾ
					.setMessage("Bug���ݲ���Ϊ��")// ������ʾ����
					.setNegativeButton("����", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					}).show();
					return;
				}
				new Thread(new BugSubmit()).start();		// �����߳�
			}
		});
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if(result.equals(ConnectServer.SUCCESS)){
					// ����һ����ʾ��
					new AlertDialog.Builder(BugReport.this).setTitle("�ɹ�")//������ʾ
					.setMessage(success+",��л����֧�֣�")// ������ʾ����
					.setNegativeButton("����", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
						}
					}).show();
					contentET.setText("");		//����EditTextΪ��
				}else{
					//Toast.makeText(Page7.this, fail, Toast.LENGTH_SHORT).show();//��ʾ�ύ�ɹ�
					// ����һ����ʾ��
					new AlertDialog.Builder(BugReport.this).setTitle("ʧ��")//������ʾ
					.setMessage(ConnectServer.FAIL)// ������ʾ����
					.setNegativeButton("����", new DialogInterface.OnClickListener(){
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
			
			Message m = handler.obtainMessage();	//��ȡһ��Message
			handler.sendMessage(m);	// ������Ϣ
		}
	}
}
	
