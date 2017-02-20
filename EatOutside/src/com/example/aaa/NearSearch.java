package com.example.aaa;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviPara;
import com.example.aaa.BusinessInfo;
import com.example.aaa.CollectionPage;
import com.example.aaa.DemoApplication;
import com.example.aaa.R;
import com.example.model.Business;
import com.example.search.LocationOfPhoto;
import com.example.tool.DemoApiTool;





import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class NearSearch extends Activity {
	
	// 定位相关
	    private DemoApplication DemoApp;
		LocationClient mLocClient;
		public MyLocationListenner myListener = new MyLocationListenner();
		//private LocationMode mCurrentMode;
		BitmapDescriptor mCurrentMarker;
		private LocationMode mCurrentMode;
		MapView mMapView;
		BaiduMap mBaiduMap;

		// UI相关
		OnCheckedChangeListener radioButtonListener;
		Button requestLocButton;
		TextView ResultText;
		boolean isFirstLoc = true;// 是否首次定位
		public String Infor="";
		TextView textview;
		static String lati;
		static String longi;
		//这些数据是用户请求大众点评Api的参数
		private String MyLati;
		private String MyLongi;
		private String MyCity;
		private String MyCategory;
		private String MyRadius;
		private String MyHas_coupon;
		private String MyHas_deal;
		private String MyHas_online_reservation;	
		
		private String Reserve_URL;
		//导航的终点与起点
		private double MyLatiS;
		private double MyLongiS;
		private double MyLatiE;
		private double MyLongiE;
		
		//这些是获得的商户相关数据信息
		private String StoreName;
		private String StoreBranch_name;
		private String StoreCity;
		private String StoreRegion;
		private String StoreAddr;
		private String StoreTel;
		private String Storebusid;
		private String Storeavg_rating,Storeavg_price,Storecategories,Stores_photo_url;
		
		private Marker marker[];
		private TextView locationName;
		private TextView locationBranch_name;
		private TextView locationCity;
		private TextView locationRegion;
		private TextView locationAddr;
		private TextView locationTel;
		private Button NaviBt;
		private Button BookBt;
		
		private PopupWindow infoPopupWindow;
		
		BitmapDescriptor bitmapDescriptor;	
		BitmapDescriptor bdGround;
		private LocationOfPhoto locations[] = new LocationOfPhoto[10];
		//private Gain gain = new Gain(); 
		private Business business = new Business(); 
		
		private List<Business> list;
		
		public class RequestAPILickListener implements OnClickListener
	    {

	        private Activity activity;

	        public RequestAPILickListener(Activity activity)
	        {
	            this.activity = activity;
	        }

	        public void Request()
	        {
	        	
	        	

	            String apiUrl = "http://api.dianping.com/v1/business/find_businesses";
//	            String appKey = ((EditText) activity.findViewById(R.id.TextKey)).getText().toString();
//	            String secret = ((EditText) activity.findViewById(R.id.TextSecret)).getText().toString();
	            String appKey="7862826130";
	            String secret="d4f3b664e24f4fcb9759303ec6275fe6";
	            Map<String, String> paramMap = new HashMap<String, String>();
	            //paramMap.put("city", "北京");//
	            System.out.println("经度"+MyLongiS+"纬度"+MyLatiS);
	            paramMap.put("latitude", String.valueOf(MyLatiS));
	            paramMap.put("longitude", String.valueOf(MyLongiS));
	            paramMap.put("category", "美食");//
	            paramMap.put("limit", "10");//
	            paramMap.put("radius", "5000");
	            paramMap.put("offset_type", "1");//
	            paramMap.put("out_offset_type","1");
	            paramMap.put("sort", "1");    //
	            paramMap.put("platform", "2");
	            paramMap.put("has_online_reservation", "1");
	            	
	            String requestResult = DemoApiTool.requestApi(apiUrl, appKey, secret, paramMap);
	            System.out.println(requestResult);
	            System.out.println(requestResult.length());
	            list = Business.GetInfor(requestResult);
	            for(int i=0;i<10;i++){
	            locations[i] = new LocationOfPhoto(list.get(i).getNa(), list.get(i).getBran_Na(), list.get(i).getLa(), 
	            		list.get(i).getLong(), list.get(i).getCity(), list.get(i).getRegion(), 
	            		list.get(i).getAddr(), list.get(i).getTel(), list.get(i).getonline_reservation_url(), 
	            		list.get(i).getRating(), list.get(i).getPrice(), list.get(i).getCategories(),list.get(i).getS_photo_url(),list.get(i).getbusi());
	            
	            System.out.println(list.get(i).getNa()+list.get(i).getBran_Na()+list.get(i).getLa()+
	            		list.get(i).getLong()+list.get(i).getCity()+list.get(i).getRegion()+
	            		list.get(i).getAddr()+list.get(i).getTel()+list.get(i).getonline_reservation_url()+"商店Id"+list.get(i).getbusi());
	            System.out.println(locations[i].getLocation().latitude);
	            System.out.println(locations[i].getLocation().latitude);
	            System.out.println("--------------------------");
	            }
	            System.out.println("链表的长度内容"+list.size());
	    		initOverlay(locations);
	        }
	        
	        @Override
	        public void onClick(View v)
	        {
	        	
	        }
	    }

		
		
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_search_near_resturant);
		DemoApp = (DemoApplication) getApplication();

		mMapView = (MapView) findViewById(R.id.bmapView);
		ResultText = (TextView)findViewById(R.id.textView1);
		mBaiduMap = mMapView.getMap();
		
		mCurrentMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_geo);
		mBaiduMap
				.setMyLocationConfigeration(new MyLocationConfiguration(
						mCurrentMode, true, mCurrentMarker));
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		

		Button button = (Button) findViewById(R.id.button1);
		//button.setOnClickListener(new RequestAPILickListener(this));
		new Thread() {

			@Override
			public void run() {
				
				mLocClient.registerLocationListener(myListener);
				LocationClientOption option = new LocationClientOption();
				option.setOpenGps(true);// 打开gps
				option.setCoorType("bd09ll"); // 设置坐标类型
				option.setScanSpan(1000);
				mLocClient.setLocOption(option);
				mLocClient.start();
				try {
		        Thread.sleep(2000);                 //由类名调用
		      } catch (InterruptedException e) {
		        System.out.println(e);
				}  
				System.out.println("到底怎么就没有经纬度呢？？？"+MyLatiS+MyLongiS);
				RequestAPILickListener RequestAPI = new RequestAPILickListener(NearSearch.this);
				RequestAPI.Request();
			super.run();
			}

		}.start();
		
		//Request();//开始想大众点评请求数据！！！！！！
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        
        //开始在地图上添加标注准备工作
        locationName = (TextView)findViewById(R.id.tv_locationname);
		locationBranch_name = (TextView)findViewById(R.id.tv_locationbranch_name);
		locationCity = (TextView)findViewById(R.id.tv_locationcity);
		locationRegion = (TextView)findViewById(R.id.tv_locationregion);
		locationAddr = (TextView)findViewById(R.id.tv_locationaddr);
		locationTel = (TextView)findViewById(R.id.tv_locationtel);
		
		LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View popupWindow = layoutInflater.inflate(R.layout.popupwindow_showlocations, null);
		infoPopupWindow = new PopupWindow(popupWindow, 600, 800); 
		infoPopupWindow.setFocusable(true);
		infoPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		locationName = (TextView)popupWindow.findViewById(R.id.tv_locationname);
		locationBranch_name = (TextView)popupWindow.findViewById(R.id.tv_locationbranch_name);
		locationCity = (TextView)popupWindow.findViewById(R.id.tv_locationcity);
		locationRegion = (TextView)popupWindow.findViewById(R.id.tv_locationregion);
		locationAddr = (TextView)popupWindow.findViewById(R.id.tv_locationaddr);
		locationTel = (TextView)popupWindow.findViewById(R.id.tv_locationtel);
		//NaviBt = (Button)popupWindow.findViewById(R.id.button);
		BookBt = (Button)popupWindow.findViewById(R.id.book);

		
		BookBt.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.out.println("终点起点啊啊啊啊"+MyLatiS+MyLongiS);
			    Business transbusi = new Business(StoreName,StoreBranch_name,MyLatiE,
			    		MyLongiE,StoreCity,StoreRegion,StoreAddr,StoreTel,Storeavg_rating,
			    		Storeavg_price,Storecategories,Stores_photo_url,Reserve_URL,
			    		Storebusid);
				Intent intent=new Intent();
				System.out.println("+++++++++++++++++商户ID："+transbusi.getbusi());
				intent.setClass(NearSearch.this, BusinessInfo.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("business", transbusi);
				bundle.putDouble("经度起点", MyLatiS);
				bundle.putDouble("纬度起点", MyLongiS);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
			
		});
		
		
		mBaiduMap = mMapView.getMap();
		bitmapDescriptor  = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
		bdGround  = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
		
		//在地图上添加标注
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker m) {
				for(int index = 0; index < locations.length; index++){
					if(m == marker[index]){
						locationName.setText("地点： "+locations[index].getName());
						locationBranch_name.setText("分店名： " +locations[index].getBranch_name());
						locationCity.setText("城市： " +locations[index].getCity());
						locationRegion.setText("地区： " +locations[index].getRegion());
						locationAddr.setText("地址： " +locations[index].getAddr());
						locationTel.setText("电话： " +locations[index].getTel());
						StoreName = locations[index].getName();
						StoreBranch_name = locations[index].getBranch_name();
						StoreCity = locations[index].getRegion();
						StoreRegion = locations[index].getRegion();
						StoreAddr = locations[index].getAddr();
						StoreTel = locations[index].getTel();
						MyLatiE = locations[index].getLocation().latitude;
						MyLongiE = locations[index].getLocation().longitude;
						Reserve_URL = locations[index].getreserve_url();
						Storeavg_rating = locations[index].getAvg_rating();
						Storeavg_price = locations[index].getAvg_price();
						Storecategories = locations[index].getRegion();
						Stores_photo_url = locations[index].getS_photo_url();
						Storebusid = locations[index].getBusid();
						Toast.makeText(getApplicationContext(), locations[index].getName()+"\n", Toast.LENGTH_LONG).show();
						infoPopupWindow.showAtLocation(mMapView, Gravity.CENTER, 0, 0);
						
					}
				}
				return true;
			}
		});
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			MyLatiS=location.getLatitude();
			MyLongiS=location.getLongitude();
			DemoApp.Myla = MyLatiS;
			DemoApp.Mylo = MyLongiS;
			MyLati = String.valueOf(location.getLatitude());
			MyLongi = String.valueOf(location.getLongitude());
			DemoApp.editor.putString("我的经度", String.valueOf(MyLongi));
			DemoApp.editor.putString("我的纬度", String.valueOf(MyLati));
			DemoApp.editor.commit();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
		finish();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void initOverlay(LocationOfPhoto locations[]){
		int count = locations.length;
		LatLng latLngs;
		LatLngBounds bounds = null;
		double min_latitude = 0, min_longitude = 0,
				max_latitude = 0, max_longitude = 0;
		
		for(int i = 0; i < count-1; i++){
			if(locations[i].getLocation().latitude <= locations[i+1].getLocation().latitude){
				min_latitude = locations[i].getLocation().latitude;
				max_latitude = locations[i+1].getLocation().latitude;
			}
			else {
				min_latitude = locations[i+1].getLocation().latitude;
				max_latitude = locations[i].getLocation().latitude;
			}
			if(locations[i].getLocation().longitude <= locations[i+1].getLocation().longitude){
				min_longitude = locations[i].getLocation().longitude;
				max_longitude = locations[i+1].getLocation().longitude;
			}
			else {
				min_longitude = locations[i+1].getLocation().longitude;
				max_longitude = locations[i].getLocation().longitude;
			}
		}
		marker = new Marker[count];
		for(int i = 0; i < count; i++){
			latLngs = locations[i].getLocation();
			OverlayOptions overlayOptions_marker = new MarkerOptions().position(latLngs).icon(bitmapDescriptor);
			marker[i] = (Marker)(mBaiduMap.addOverlay(overlayOptions_marker));
		}
		LatLng southwest = new LatLng(min_latitude, min_longitude);
		LatLng northeast = new LatLng(max_latitude, max_longitude);
		LatLng northwest = new LatLng(max_latitude, min_longitude);
		LatLng southeast = new LatLng(min_latitude, max_longitude);

		bounds = new LatLngBounds.Builder().include(northeast).include(southwest).include(southeast).include(northwest).build();
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
		mBaiduMap.animateMapStatus(mapStatusUpdate,1000);
		MapStatusUpdate mapStatusUpdate_zoom = MapStatusUpdateFactory.zoomTo(10);
		mBaiduMap.setMapStatus(mapStatusUpdate_zoom);
	}

}