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
	private User user;	//声明一个存储Intent传送过来的User对象
	private final String SUCCESS = "success";				// 声明一个表示请求成功的字符串
	
	private Button save;			//设置保存更改后的用户信息的按钮
	private Button changePic;		// 设置更改头像的按钮
	private Button back;			// 返回按钮
	private Handler handler;	//声明一个Handler
	private Bitmap bitmap;		// 声明用户头像的Bitmap
	private ImageView pic;		// 声明表示用户头像的ImageView
	private ProgressDialog pdialog;		// 进度框
	
	private String username;	//声明一个表示用户名的字符串
	private String dateString;	//声明一个表示生日的字符串
	private String gender;		// 声明一个表示性别的字符串
	private String original;	// 声明一个存储Server返回的原始数据的字符串
	private String result;		// 声明一个original经过提取的结果字符串
	private String picPath = null;	// 新选择图片的path
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 	// 用来做日期转换
	
	// 新的 页面属性
	private Button namebtn;		// 昵称的button
	private Button genderbtn;	// 姓名button
	private Button birthbtn;	// 生日button
	
	// 用来设置DatePickerDialog的三个变量
	private int year;
    private int month;
    private int day;
    
	@SuppressLint("HandlerLeak")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page9);
		
		save = (Button) findViewById(R.id.save);				// 获得保存的按钮
		pic = (ImageView) findViewById(R.id.pic);				// 获得用户头像对象
		changePic = (Button) findViewById(R.id.changePic);		// 获得更改头像的按钮
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
		
		// 处理intent内信息
		Intent intent = getIntent();	//获得Intent对象
		Bundle bundle = intent.getExtras();	//获取传递的数据包
		user = (User) bundle.getSerializable("user");	// 获得User对象
		// 将bitmap读取出来并显示在ImageView上
		byte[] bis = intent.getByteArrayExtra("bitmap");
		bitmap=BitmapFactory.decodeByteArray(bis, 0, bis.length);
		pic.setImageBitmap(bitmap);
		
		
		namebtn.setText(user.getUserName());
		// 为昵称按钮添加事件，弹出输入对话框
		namebtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(UserInfoUpdate.this); 
				final View textEntryView = factory.inflate(R.layout.inputname, null);
				final EditText editTextName = (EditText) textEntryView.findViewById(R.id.nike); 
				AlertDialog.Builder ad = new AlertDialog.Builder(UserInfoUpdate.this);
				ad.setTitle("输入用户名：");
				ad.setIcon(android.R.drawable.ic_dialog_info);
				ad.setView(textEntryView);
				ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						namebtn.setText(editTextName.getText().toString());
					}
				});
				ad.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
				adbuilder.setTitle("选择性别：");
				adbuilder.setIcon(android.R.drawable.ic_dialog_info);
				adbuilder.setView(buttonView);
				final AlertDialog ad = adbuilder.create();
				ad.show();
				nan.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						genderbtn.setText("男");
						ad.dismiss();
					}
				});
				nv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						genderbtn.setText("女");
						ad.dismiss();
					}
				});
				
			}
		});
		
		birthbtn.setText(sdf.format(user.getBirth()));
		// 为设置日期添加事件，弹出日期选择
		initDatePickerDialog();
		
		// 为保存按钮添加事件，链接网络
	    save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 传输图片！
				if(picPath!=null&&picPath.length()>0)
				{
					UploadFileTask uploadFileTask=new UploadFileTask(UserInfoUpdate.this);
					uploadFileTask.execute(picPath);
				}
				// 获得更改后的事件
				username = namebtn.getText().toString();
				gender = genderbtn.getText().toString();
				dateString = birthbtn.getText().toString();
				new Thread(new UpdateUser()).start();
			}
		});
	    // 为修改图片按钮增加事件，能够弹出选择图片
	    changePic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				//回调图片类使用的
				startActivityForResult(intent, RESULT_CANCELED);
				
			}
		});
	    
	    handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(pdialog!=null)
					pdialog.dismiss();
				if(result.equals(SUCCESS)){
					// 请求成功
					// 构建一个提示框
					new AlertDialog.Builder(UserInfoUpdate.this).setTitle("提示")//设置提示
					.setMessage("修改成功！")// 设置显示内容
					.setPositiveButton("确定", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 导航到Page2
							Intent intent = new Intent();
			            	intent.setClass(UserInfoUpdate.this,UserInfo.class);
			            	startActivity(intent);
						}
					}).show();
				}else{
					// 构建一个提示框
					new AlertDialog.Builder(UserInfoUpdate.this).setTitle("提示")//设置提示
					.setMessage("服务器请求失败。")// 设置显示内容
					.setNegativeButton("返回", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 导航到Page2
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
	// 用来执行连接数据库的线程
	class UpdateUser implements Runnable{
		@Override
		public void run() {
			if(picPath==null||picPath.length()==0)
				pdialog=ProgressDialog.show(UserInfoUpdate.this, "正在加载...", "系统正在处理您的请求");
			String action = "updateUser.action?username="+ username + "&"
					+ "gender=" + gender + "&" 
					+"dateString=" + dateString;		// 设置请求参数
			Log.i("url", action);
			original = ConnectServer.send(action);
			result = ConvertTool.convertToMessage(original);
			Message m = handler.obtainMessage();	//获取一个Message
			handler.sendMessage(m);	// 发送消息
			
		}
	}
	// 声明的OnSetListener
	private DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int myyear, int monthOfYear,
				int dayOfMonth) {
			//修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            year=myyear;
            month=monthOfYear;
            day=dayOfMonth;
            //更新日期
            updateDate();
		}
		private void updateDate() {
			//在TextView上显示日期
			birthbtn.setText(+year+"-"+(month+1)+"-"+day);
		}
	};
	// 初始化DatePickerDialog，为setdate按钮添加事件
	public void initDatePickerDialog(){
		// 初始化Calendar
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date();	// 获得当前日期
		mycalendar.setTime(mydate);	// 设置Calendar为当前日期
			
		year=mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        month=mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        day=mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
	    // 为设置时间按钮添加时间
        birthbtn.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
			//创建//datepickerdialog对象
			DatePickerDialog dpd = new DatePickerDialog(UserInfoUpdate.this, callback, year, month, day);
			dpd.show();
			}
		});
	}
	
	/**
	 * 重写回调执行
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK){
			
			/**
			 * 当选择的图片不为空的话，在获取到图片的途径  
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
					 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，这样的话，我们判断文件的后缀名
					 * 如果是图片格式的话，那么才可以   
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
		.setTitle("提示")
		.setMessage("您选择的不是有效的图片")
		.setPositiveButton("确定",
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
