package com.example.aaa;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;
import android.content.SharedPreferences;



public class DemoApplication extends Application {
	    public final String Server_IP="10.125.106.85";     
	    public String state="";
		public String content="";
		public String UserName;
		public String UserPawd;
		public double lo;
		public double la;
		public double Mylo;
		public double Myla;
		public SharedPreferences preferences;
		public SharedPreferences.Editor editor;
		
	    public String getServer_IP(){   
	        return Server_IP;   
	    }      
	    
	    
	    public String getstate(){
	    	return state;
	    }
	    
	    public void setstate(String s){
	    	this.state=s;
	    }
	    
	    public String getcontent(){
	    	return content;
	    }
	    
	    public void setcontent(String c){
	    	this.content=c;
	    }
	    
	    public double getLo(){
	    	return lo;
	    }
	    
	    public void setLo(double l){
	    	this.lo=l;
	    }
	    
	    public double getLa(){
	    	return la;
	    }
	    
	    public void setLa(double l){
	    	this.la=l;
	    }
	    
	    public double getMyLo(){
	    	return Mylo;
	    }
	    
	    public void setMyLo(double l){
	    	this.Mylo=l;
	    }
	    
	    public double getMyLa(){
	    	return Myla;
	    }
	    
	    public void setMyLa(double l){
	    	this.Myla=l;
	    }
	    
	    public String getUserName(){
	    	return UserName;
	    }
	    
	    public void setUserName(String u){
	    	this.UserName=u;
	    }
	    
	    public String getUserPawd(){
	    	return UserPawd;
	    }
	    
	    public void setUserPawd(String u){
	    	this.UserPawd=u;
	    }
	    
	    public SharedPreferences getpreferences()
		{
			return preferences;
		}
		
		public void setpreferences(SharedPreferences preferences)
		{
			this.preferences=preferences;
		}
		
		public SharedPreferences.Editor geteditor()
		{
			return editor;
		}
		
		public void seteditor(SharedPreferences.Editor editor)
		{
			this.editor=editor;
		}
		
	
	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
	}

}