package com.example.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Gain {
	List<Business> infolist = new ArrayList();
	class Business{
	 private String name,branch_name,address,telephone,city,regions;
	 private String latitude,longitude,business,online_reservation_url;
	 private String avg_rating,avg_price,categories,s_photo_url;
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
		 business="null";
		 online_reservation_url="null";
		 avg_rating="null";
		 avg_price="null";
		 categories="null";
		 s_photo_url="null";
		 bitmapBytes = null;
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
		 return business;
	 }
	 public double getLa(){
		 return Double.valueOf(latitude);
	 }
	 public double getLong(){
		 return Double.valueOf(longitude);
	 }
	 public String getonline_reservation_url(){
		 return online_reservation_url;
	 }
	 
	 public  Business(String s){
		 business="";
		// int i=s.indexOf("business_id");
		 int i=0;
		 while (s.charAt(i)!=','){
			 business+=s.charAt(i);
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
		 else online_reservation_url="≤ªø…‘§∂©";

	 }
	 public void outputText(){
		 System.out.println(business);
		 System.out.println(name);
		 System.out.println(branch_name);
		 System.out.println(address);
		 System.out.println(telephone);
		 System.out.println(city);
		 System.out.println(regions);
		 System.out.println(latitude);
		 System.out.println(longitude);
		 System.out.println(online_reservation_url);
	 }
	}
	public void GetInfor(String requestresult){
		String test=requestresult;
		int t1=test.indexOf("business_id"); 
		int length=test.length();
		test=test.substring(t1+13,length);
		Gain a = new Gain();
		int t=0;

		while (t!=-1){
		Business Business=a.new Business(test);
		 infolist.add(Business);
		 Business.outputText();
		 System.out.println("-------------------------------");
		 t=test.indexOf("business_id");
		 if (t!=-1){
		 length=test.length();
		 test=test.substring(t+13,length);
		 }
		}
		
	}
}

