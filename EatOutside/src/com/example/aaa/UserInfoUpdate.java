package com.example.aaa;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.model.User;
import com.example.tool.ConnectServer;
import com.example.tool.ConvertTool;
import com.example.tool.UploadFileTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


@SuppressLint("SimpleDateFormat")
public class UserInfoUpdate extends Activity {
	private User user;	//����һ���洢Intent���͹�����User����
	private final String SUCCESS = "success";				// ����һ����ʾ����ɹ����ַ���
	
	private Button save;			//���ñ�����ĺ���û���Ϣ�İ�ť
	private Button changePic;		// ���ø���ͷ��İ�ť
	private Button back;			// ���ذ�ť
	private Handler handler;	//����һ��Handler
	private Bitmap bitmap;		// �����û�ͷ���Bitmap
	private ImageView pic;		// ������ʾ�û�ͷ���ImageView
	private ProgressDialog pdialog;		// ���ȿ�
	
	private String username;	//����һ����ʾ�û������ַ���
	private String dateString;	//����һ����ʾ���յ��ַ���
	private String gender;		// ����һ����ʾ�Ա���ַ���
	private String original;	// ����һ���洢Server���ص�ԭʼ���ݵ��ַ���
	private String result;		// ����һ��original������ȡ�Ľ���ַ���
	private String picPath = null;	// ��ѡ��ͼƬ��path
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 	// ����������ת��
	
	// �µ� ҳ������
	private Button namebtn;		// �ǳƵ�button
	private Button genderbtn;	// ����button
	private Button birthbtn;	// ����button
	
	// ��������DatePickerDialog����������
	private int year;
    private int month;
    private int day;
    
	@SuppressLint("HandlerLeak")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page9);
		
		save = (Button) findViewById(R.id.save);				// ��ñ���İ�ť
		pic = (ImageView) findViewById(R.id.pic);				// ����û�ͷ�����
		changePic = (Button) findViewById(R.id.changePic);		// ��ø���ͷ��İ�ť
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		namebtn = (Button) findViewById(R.id.nikebutton);
		genderbtn = (Button) findViewById(R.id.genderbutton);
		birthbtn = (Button) findViewById(R.id.birthbtn);
		
		// ����intent����Ϣ
		Intent intent = getIntent();	//���Intent����
		Bundle bundle = intent.getExtras();	//��ȡ���ݵ����ݰ�
		user = (User) bundle.getSerializable("user");	// ���User����
		// ��bitmap��ȡ��������ʾ��ImageView��
		byte[] bis = intent.getByteArrayExtra("bitmap");
		bitmap=BitmapFactory.decodeByteArray(bis, 0, bis.length);
		pic.setImageBitmap(bitmap);
		
		
		namebtn.setText(user.getUserName());
		// Ϊ�ǳư�ť����¼�����������Ի���
		namebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(UserInfoUpdate.this); 
				final View textEntryView = factory.inflate(R.layout.inputname, null);
				final EditText editTextName = (EditText) textEntryView.findViewById(R.id.nike); 
				AlertDialog.Builder ad = new AlertDialog.Builder(UserInfoUpdate.this);
				ad.setTitle("�����û�����");
				ad.setIcon(android.R.drawable.ic_dialog_info);
				ad.setView(textEntryView);
				ad.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						namebtn.setText(editTextName.getText().toString());
					}
				});
				ad.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				ad.show();
			}
		});
		
		genderbtn.setText(user.getGender());
		genderbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(UserInfoUpdate.this); 
				final View buttonView = factory.inflate(R.layout.genderchose, null);
				final Button nan =  (Button) buttonView.findViewById(R.id.birthbtn);
				final Button nv = (Button) buttonView.findViewById(R.id.button2);
				AlertDialog.Builder adbuilder = new AlertDialog.Builder(UserInfoUpdate.this);
				//AlertDialog 
				adbuilder.setTitle("ѡ���Ա�");
				adbuilder.setIcon(android.R.drawable.ic_dialog_info);
				adbuilder.setView(buttonView);
				final AlertDialog ad = adbuilder.create();
				ad.show();
				nan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						genderbtn.setText("��");
						ad.dismiss();
					}
				});
				nv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						genderbtn.setText("Ů");
						ad.dismiss();
					}
				});
				
			}
		});
		
		birthbtn.setText(sdf.format(user.getBirth()));
		// Ϊ������������¼�����������ѡ��
		initDatePickerDialog();
		
		// Ϊ���水ť����¼�����������
	    save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// ����ͼƬ��
				if(picPath!=null&&picPath.length()>0)
				{
					UploadFileTask uploadFileTask=new UploadFileTask(UserInfoUpdate.this);
					uploadFileTask.execute(picPath);
				}
				// ��ø��ĺ���¼�
				username = namebtn.getText().toString();
				gender = genderbtn.getText().toString();
				dateString = birthbtn.getText().toString();
				new Thread(new UpdateUser()).start();
			}
		});
	    // Ϊ�޸�ͼƬ��ť�����¼����ܹ�����ѡ��ͼƬ
	    changePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				//�ص�ͼƬ��ʹ�õ�
				startActivityForResult(intent, RESULT_CANCELED);
				
			}
		});
	    
	    handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(pdialog!=null)
					pdialog.dismiss();
				if(result.equals(SUCCESS)){
					// ����ɹ�
					// ����һ����ʾ��
					new AlertDialog.Builder(UserInfoUpdate.this).setTitle("��ʾ")//������ʾ
					.setMessage("�޸ĳɹ���")// ������ʾ����
					.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// ������Page2
							Intent intent = new Intent();
			            	intent.setClass(UserInfoUpdate.this,UserInfo.class);
			            	startActivity(intent);
						}
					}).show();
				}else{
					// ����һ����ʾ��
					new AlertDialog.Builder(UserInfoUpdate.this).setTitle("��ʾ")//������ʾ
					.setMessage("����������ʧ�ܡ�")// ������ʾ����
					.setNegativeButton("����", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// ������Page2
							Intent intent = new Intent();
			            	intent.setClass(UserInfoUpdate.this,UserInfo.class);
			            	startActivity(intent);
						}
					}).show();
				}
				super.handleMessage(msg);
			}
	    };
	    Log.i("tag", "onCreateComplete!");
	} // end of onCreate()
	// ����ִ���������ݿ���߳�
	class UpdateUser implements Runnable{
		@Override
		public void run() {
			if(picPath==null||picPath.length()==0)
				pdialog=ProgressDialog.show(UserInfoUpdate.this, "���ڼ���...", "ϵͳ���ڴ�����������");
			String action = "updateUser.action?username="+ username + "&"
					+ "gender=" + gender + "&" 
					+"dateString=" + dateString;		// �����������
			Log.i("url", action);
			original = ConnectServer.send(action);
			result = ConvertTool.convertToMessage(original);
			Message m = handler.obtainMessage();	//��ȡһ��Message
			handler.sendMessage(m);	// ������Ϣ
			
		}
	}
	// ������OnSetListener
	private DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int myyear, int monthOfYear,
				int dayOfMonth) {
			//�޸�year��month��day�ı���ֵ���Ա��Ժ󵥻���ťʱ��DatePickerDialog����ʾ��һ���޸ĺ��ֵ
            year=myyear;
            month=monthOfYear;
            day=dayOfMonth;
            //��������
            updateDate();
		}
		private void updateDate() {
			//��TextView����ʾ����
			birthbtn.setText(+year+"-"+(month+1)+"-"+day);
		}
	};
	// ��ʼ��DatePickerDialog��Ϊsetdate��ť����¼�
	public void initDatePickerDialog(){
		// ��ʼ��Calendar
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date();	// ��õ�ǰ����
		mycalendar.setTime(mydate);	// ����CalendarΪ��ǰ����
			
		year=mycalendar.get(Calendar.YEAR); //��ȡCalendar�����е���
        month=mycalendar.get(Calendar.MONTH);//��ȡCalendar�����е���
        day=mycalendar.get(Calendar.DAY_OF_MONTH);//��ȡ����µĵڼ���
	    // Ϊ����ʱ�䰴ť���ʱ��
        birthbtn.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
			//����//datepickerdialog����
			DatePickerDialog dpd = new DatePickerDialog(UserInfoUpdate.this, callback, year, month, day);
			dpd.show();
			}
		});
	}
	
	/**
	 * ��д�ص�ִ��
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK){
			
			/**
			 * ��ѡ���ͼƬ��Ϊ�յĻ����ڻ�ȡ��ͼƬ��;��  
			 */
			Uri uri = data.getData();
			try {
				String[] pojo = {MediaStore.Images.Media.DATA};
				
				Cursor cursor = managedQuery(uri, pojo, null, null,null);
				if(cursor!=null)
				{
					ContentResolver cr = this.getContentResolver();
					int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);
					/***
					 * ���������һ���ж���Ҫ��Ϊ�˵����������ѡ�񣬱��磺ʹ�õ��������ļ��������Ļ�����ѡ����ļ��Ͳ�һ����ͼƬ�ˣ������Ļ��������ж��ļ��ĺ�׺��
					 * �����ͼƬ��ʽ�Ļ�����ô�ſ���   
					 */
					if(path.endsWith("jpg")||path.endsWith("png"))
					{
						picPath = path;
						Log.i("path", picPath);
						Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
						pic.setImageBitmap(bitmap);
					}else{
						alert();
					}
				}else{
					alert();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void alert()
    {
    	Dialog dialog = new AlertDialog.Builder(this)
		.setTitle("��ʾ")
		.setMessage("��ѡ��Ĳ�����Ч��ͼƬ")
		.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						picPath = null;
					}
				})
		.create();
		dialog.show();
    }
	
}
