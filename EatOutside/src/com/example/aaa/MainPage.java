package com.example.aaa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.model.Business;
import com.example.tool.ConnectServer;
import com.example.tool.ConvertTool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;

import android.widget.SimpleAdapter.ViewBinder;
public class MainPage extends Activity implements OnClickListener{
	private List<Business> commendList = new ArrayList<Business>();
	List<Map<String,Object>> listItems=
			new ArrayList<Map<String,Object>>();
	private SimpleAdapter simpleAdapter;
	private ListView list;				// 列表
	private Handler handler;
	private int[] imageIds=new int[]
			{R.drawable.five_friend,R.drawable.huangmenji,R.drawable.kaoroufan};
	private String[] restaurants=new String[]
			{"五个好朋友","黄焖鸡米饭","张姐烤肉饭"};
	private String[] distances=new String[]
			{"300m","500m","600m"};
	private String[] averages=new String[]
			{"20","20","10"};
	private String[] kinds=new String[]
			{"中餐","中餐","中餐"};
	private ImageButton SearchNearBt;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page1);
		
		// 拿信息--精彩推介列表
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		Business b1 = (Business) bundle.getSerializable("b1");
		Business b2 = (Business) bundle.getSerializable("b2");
		Business b3 = (Business) bundle.getSerializable("b3");
		commendList.add(b1);
		commendList.add(b2);
		commendList.add(b3);
		
		
		// find button
		findViewById(R.id.eat).setOnClickListener(this); 
		findViewById(R.id.ib_mylocation).setOnClickListener(this); 
		
		new Thread(new GetCommendList()).start();

		handler = new Handler(){

			@SuppressLint("HandlerLeak")
			@Override
			public void handleMessage(Message msg) {
				list=(ListView) findViewById(R.id.mylist);
				list.setAdapter(simpleAdapter);
				list.setOnItemClickListener(new MyListClickListener());
				super.handleMessage(msg);
			}
			
		};
	}
	public void onClick(View view) {  
	      switch (view.getId()) {  
	      case R.id.eat:
	    	Intent intent3 = new Intent();
			intent3.setClass(MainPage.this, Search.class);
			startActivity(intent3);
			break;  
	      case R.id.ib_mylocation:
		    	Intent intent = new Intent();
				intent.setClass(MainPage.this, NearSearch.class);
				startActivity(intent);
				break;  
		 default:
			 break;
	      }
	}
	class MyListClickListener implements AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent=new Intent();
			intent.setClass(MainPage.this, BusinessInfo.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("business", commendList.get(position));
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	
	/**
	 * Class GetCommendList 把list中的商户的图片给加载出来然后显示到屏幕上
	 */
	class GetCommendList implements Runnable{

		@Override
		public void run() {
			Bitmap bitmap;
			for(int i = 0; i < commendList.size();i ++){
				System.out.println("++++++++++++++++++++MainPage商户ID："+commendList.get(i).getbusi());
				bitmap = ConnectServer.getPicture(commendList.get(i).getS_photo_url());
				// 将图片的字节流存入Business对象中
				commendList.get(i).setBitmapBytes(ConvertTool.getBytes(bitmap));
			}
			for(int i=0;i<commendList.size();i++){
				Map<String,Object> listItem=
						new HashMap<String,Object>();
				listItem.put("restaurantname",commendList.get(i).getNa());
				listItem.put("distance", commendList.get(i).getBran_Na());
				// 从Business对象中得到bitmap的byte字节流，然后转换成图片
				listItem.put("imagerestaurant", ConvertTool.getBitmap(commendList.get(i).getBitmapBytes()));
				listItem.put("average", commendList.get(i).getPrice());
				listItem.put("kind", commendList.get(i).getCategories());
				listItems.add(listItem);
			}
			//创建一个SimpleAdapter
			simpleAdapter=new SimpleAdapter
					(MainPage.this, listItems, R.layout.list_item, 
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
		
	}
}
