package com.example.model;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.model.LatLng;
import com.example.search.Gain;

import android.graphics.Bitmap;

public class Business implements Serializable {
	
	private String name,branch_name,address,telephone,city,regions;
	private String latitude,longitude,business_id,online_reservation_url;
	private String avg_rating,avg_price,categories,s_photo_url;
	private boolean collected = false;
	
	//private static List<Business> businesslist = new ArrayList();
	private byte[] bitmapBytes;
	public Business(){
		name="null";
		 branch_name="null";
		 address="null";
		 telephone="null";
		 city="null";
		 regions="null";
		 latitude="null";
		 longitude="null";
		 business_id="null";
		 online_reservation_url="null";
		 avg_rating="null";
		 avg_price="null";
		 categories="null";
		 s_photo_url="null";
		 bitmapBytes = null;
	}

	public Business(String storeName, String storeBranch_name, double myLatiE,
			double myLongiE, String storeCity, String storeRegion,
			String storeAddr, String storeTel, String storeavg_rating,
			String storeavg_price, String storecategories,
			String stores_photo_url, String Reserve_URL, String business_id) {
		this.name = storeName;
		this.branch_name = storeBranch_name;
		this.address = storeAddr;
		this.telephone = storeTel;
		this.city = storeCity;
		this.regions = storeRegion;
		this.latitude = String.valueOf(myLatiE);
		this.longitude = String.valueOf(myLongiE);
		//this.business = business;
		this.online_reservation_url = Reserve_URL;
		this.avg_rating = storeavg_rating;
		this.avg_price = storeavg_price;
		this.categories = storecategories;
		this.s_photo_url = stores_photo_url;
		this.business_id = business_id;
		bitmapBytes = null;
	}


	public Business(String s){
		business_id="";
		 int i=0;
		 while (s.charAt(i)!=','){
			 business_id+=s.charAt(i);
			 i++;
		 }
		 name="";
		 i=s.indexOf("name");
		 i+=7;
		 while (s.charAt(i)!='('){
			 name+=s.charAt(i);
			 i++;
		 }
		 branch_name="";
		 i=s.indexOf("branch_name");
		 i+=14;
		 while (s.charAt(i)!='"'){
			 branch_name+=s.charAt(i);
			 i++;
		 }
		 address="";
		 i=s.indexOf("address");
		 i+=10;
		 while (s.charAt(i)!='"'){
			 address+=s.charAt(i);
			 i++;
		 }
		 telephone="";
		 i=s.indexOf("telephone");
		 i+=12;
		 while (s.charAt(i)!='"'){
			 telephone+=s.charAt(i);
			 i++;
		 }
		 city="";
		 i=s.indexOf("city");
		 i+=7;
		 while (s.charAt(i)!='"'){
			 city+=s.charAt(i);
			 i++;
		 }
		 regions="";
		 i=s.indexOf("regions");
		 i+=10;
		 while (s.charAt(i)!=']'){
			 regions+=s.charAt(i);
			 i++;
		 }
		 latitude="";
		 i=s.indexOf("latitude");
		 i+=10;
		 while (s.charAt(i)!=','){
			 latitude+=s.charAt(i);
			 i++;
		 }
		 longitude="";
		 i=s.indexOf("longitude");
		 i+=11;
		 while (s.charAt(i)!=','){
			 longitude+=s.charAt(i);
			 i++;
		 }
		 online_reservation_url="";
		 i=s.indexOf("online_reservation_url");
		 i+=25;
		 if (s.charAt(i)!='"'){
		 while (s.charAt(i)!='"'){
			 online_reservation_url+=s.charAt(i);
			 i++;
		 }}
		 else online_reservation_url="不可预订";
		 avg_rating="";
		 i=s.indexOf("avg_rating");
		 i+=12;
		 while (s.charAt(i)!=','){
			 avg_rating+=s.charAt(i);
			 i++;
		 }
		 avg_price="";
		 i=s.indexOf("avg_price");
		 i+=11;
		 while (s.charAt(i)!=','){
			 avg_price+=s.charAt(i);
			 i++;
		 }
		 categories="";
		 i=s.indexOf("categories");
		 i+=14;
		 while (s.charAt(i)!='"'){
			 categories+=s.charAt(i);
			 i++;
		 }
		 s_photo_url="";
		 i=s.indexOf("s_photo_url");
		 i+=14;
		 while (s.charAt(i)!='"'){
			 s_photo_url+=s.charAt(i);
			 i++;
		 }
	}
	
	



	public static List<Business> GetInfor(String requestresult){
		List<Business> businesslist = new ArrayList();
		String test=requestresult;
		int t1=test.indexOf("business_id"); 
		int length=test.length();
		test=test.substring(t1+13,length);
		int t=0;
		while (t!=-1){
		Business business=new Business(test);
		 businesslist.add(business);
		 t=test.indexOf("business_id");
		 if (t!=-1){
		 length=test.length();
		 test=test.substring(t+13,length);
		 }
		}
		return businesslist;
	}
	public boolean isCollected() {
		return collected;
	}
	public void setCollected(boolean collected) {
		this.collected = collected;
	}
	public byte[] getBitmapBytes() {
		return bitmapBytes;
	}
	public void setBitmapBytes(byte[] bitmapBytes) {
		this.bitmapBytes = bitmapBytes;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getS_photo_url() {
		return s_photo_url;
	}
	public void setS_photo_url(String s_photo_url) {
		this.s_photo_url = s_photo_url;
	}
	public String getRating(){
		 return avg_rating;
	 }
	 public String getPrice(){
		 return avg_price;
	 }
	 public String getNa(){
		 return name;
	 }
	 public String getBran_Na(){
		 return branch_name;
	 }
	 public String getAddr(){
		 return address;
	 }
	 public String getTel(){
		 return telephone;
	 }
	 public String getCity(){
		 return city;
	 }
	 public String getRegion(){
		 return regions;
	 }
	 public String getbusi(){
		 return business_id;
	 }
	 public double getLa(){
		 return Double.valueOf(latitude);
	 }
	 public double getLong(){
		 return Double.valueOf(longitude);
	 }
	 public String getonline_reservation_url(){
		 if(online_reservation_url==null)
			 return "不可预订";
		 else
			 return online_reservation_url;
	 }
}
