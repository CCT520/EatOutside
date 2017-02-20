package com.example.aaa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.example.aaa.R;
import com.example.model.Business;
import com.example.tool.ConnectServer;
import com.example.tool.ConvertTool;
import com.example.tool.DemoApiTool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.SimpleAdapter.ViewBinder;
import android.view.View.OnClickListener;

public class CollectionPage extends Activity{
	
	private List<String> collections;	// 收藏夹商家ID集合
	private List<Business> businesses = new LinkedList<Business>();	// 收藏的商家集合
	private String original;	// 声明一个存储Server返回的原始数据的字符串
	private String result;		// 声明一个original经过提取的结果字符串
	private ProgressDialog pdialog;		// 进度框
	private ListView list;				// 列表
	private Handler handler;			// 声明处理消息的Handler
	private int position;
	private Bitmap bitmap;		// 声明一个商户图片的bitmap
	private List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
	private SimpleAdapter simpleAdapter;
	private Button back;	//返回按钮
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page5);
		//显示进度框，新建线程
		pdialog = ProgressDialog.show(CollectionPage.this, "正在加载...", "系统正在处理您的请求");
		
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 创建Handler
		handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
			public void handleMessage(Message msg) {
				Log.i("msg", "" + msg.what);
				if(result.equals(ConnectServer.FAIL)){
					// 构建一个提示框
					new AlertDialog.Builder(CollectionPage.this).setTitle("提示")//设置提示
					.setMessage(ConnectServer.FAIL)// 设置显示内容
					.setNegativeButton("返回", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
					pdialog.dismiss();
						return;
					}
					if(msg.what == 0x101){	//来自GetCollection
						list=(ListView) findViewById(R.id.mylist);
						list.setAdapter(simpleAdapter);
						list.setOnItemClickListener(new MyListClickListener());
						list.setOnItemLongClickListener((android.widget.AdapterView.OnItemLongClickListener) new OnItemLongClickListener());
						pdialog.dismiss();
					}
					if(msg.what == 0x102){	// 来自DelCollection
						pdialog.dismiss();
						// 构建一个提示框
						new AlertDialog.Builder(CollectionPage.this).setTitle("提示")//设置提示
						.setMessage("删除成功！")// 设置显示内容
						.setNegativeButton("返回", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								}
							}).show();
						
					}
					super.handleMessage(msg);
				}	
			};
				
		new Thread(new GetCollection()).start();
		
	}
	/**
	 * 跳转到商户详情页面，将相应的Business对象传过去
	 * @author Lyc
	 *
	 */
	class MyListClickListener implements AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent=new Intent();
			intent.setClass(CollectionPage.this, BusinessInfo.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("business", businesses.get(position));
			intent.putExtras(bundle);
			startActivity(intent);
			
		}
	}
	// 删除收藏夹某商户
	class OnItemLongClickListener implements AdapterView.OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			position=arg2;
			// 构建一个提示框
			new AlertDialog.Builder(CollectionPage.this).setTitle("成功")//设置提示
			.setMessage("确认删除？")// 设置显示内容
			.setPositiveButton("确认", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					listItems.remove(position);
					simpleAdapter.notifyDataSetChanged();
					//显示进度框，新建线程
					pdialog = ProgressDialog.show(CollectionPage.this, "正在加载...", "系统正在处理您的请求");
					new Thread(new DelCollection(position)).start();
				}
			})
			.setNegativeButton("返回", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing
				}
			}).show();
			return true;
		}
	}
	/**
	 * 用来获取收藏的商户ID和商户信息
	 */
	class GetCollection implements Runnable{

		@Override
		public void run() {
			Business business;
			String action = "returnCollection.action";
			original = ConnectServer.send(action);
			// 如果请求成功
			if(!original.equals(ConnectServer.FAIL)){
				// 转换为商户ID集合
				collections = ConvertTool.convertToCollectionList(original);
				for(int i=0; i<collections.size(); i++){
					//对集合迭代，调用大众点评，加入Business集合
					business = new Business(get(collections.get(i)));
					bitmap = ConnectServer.getPicture(business.getS_photo_url());
					// 将图片的字节流存入Business对象中
					business.setBitmapBytes(ConvertTool.getBytes(bitmap));
					business.setCollected(true);
					businesses.add(business);
				}
				result = ConnectServer.SUCCESS;
			}else
				result = ConnectServer.FAIL;
			
			if(collections!= null){
				for(int i=0;i<collections.size();i++){
					Map<String,Object> listItem=
							new HashMap<String,Object>();
					listItem.put("restaurantname",businesses.get(i).getNa());
					listItem.put("distance", businesses.get(i).getBran_Na());
					// 从Business对象中得到bitmap的byte字节流，然后转换成图片
					listItem.put("imagerestaurant", ConvertTool.getBitmap(businesses.get(i).getBitmapBytes()));
					listItem.put("average", businesses.get(i).getPrice());
					listItem.put("kind", businesses.get(i).getCategories());
					listItems.add(listItem);
				}
			}
			//创建一个SimpleAdapter
			simpleAdapter=new SimpleAdapter
					(CollectionPage.this, listItems, R.layout.list_item, 
							new String[]{"restaurantname","distance",
							"imagerestaurant","average","kind"}, 
							new int[]{R.id.restaurant,R.id.distance,
							R.id.image_restaurant,R.id.average,R.id.kind});
			simpleAdapter.setViewBinder(new ViewBinder() {  
	            
	            public boolean setViewValue(View view, Object data,  
	                    String textRepresentation) {  
	                //判断是否为我们要处理的对象  
	                if(view instanceof ImageView  && data instanceof Bitmap){  
	                    ImageView iv = (ImageView) view;  
	                    iv.setImageBitmap((Bitmap) data);  
	                    return true;  
	                }else  
	                return false;  
	            }  
	        });  
			Message m = new Message();
			m.what = 0x101;	//从读取用户信息线程发送
			handler.sendMessage(m);	// 发送消息
			
			
		}
		public String get(String businessId){
			// 从大众点评获取该商户字符串，并转换成Business对象
	        String singleUrl = "http://api.dianping.com/v1/business/get_single_business?";
	        Map<String, String> param = new HashMap<String, String>();
	        param.put("business_id", businessId);
	        return DemoApiTool.requestApi(singleUrl, DemoApiTool.appKey, DemoApiTool.secret, param);
	       
		}
	}// end of GetCollection
	
	/**
	 * class delColletion删除某项收藏，并持久化
	 */
	class DelCollection implements Runnable{

		int pos;		// 所要删除商户在list中的位置，由外部传入
		
		public DelCollection(int pos) {
			this.pos = pos;
		}
		
		@Override
		public void run() {
			String action = "delCollection.action?businessId=" + collections.get(pos);
			original = ConnectServer.send(action);
			if(!original.equals(ConnectServer.FAIL)){	// 如果请求成功
				result = ConvertTool.convertToMessage(original);	
				if(result.equals(ConnectServer.SUCCESS)){
					// 如果返回的消息（message）成功（success）
					collections.remove(pos);
					businesses.remove(pos);
				}else
					result = ConnectServer.FAIL;
			}else
				result = ConnectServer.FAIL;
			Message m = new Message();
			m.what = 0x102;	//从读取用户信息线程发送
			handler.sendMessage(m);	// 发送消息
		}
		
	}
	
}
	
