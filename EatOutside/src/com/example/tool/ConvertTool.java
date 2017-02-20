package com.example.tool;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.model.User;

public class ConvertTool {
	
	
	public ConvertTool() {
		
	}

	public static String convertToUserString(String userString){
		try{
			String birthString = null;
			String userName = null;
			String gender = null;
			int i=0;
			i=userString.indexOf("birth");
			i+=8;
			birthString="";
			int loopt=0;
			while (loopt<10){
				birthString+=userString.charAt(i);
				loopt++;
				i++;
			 }
			 userName="";
			 i=userString.indexOf("userName");
			 i+=11;
			 while (userString.charAt(i)!='"'){
				 userName+=userString.charAt(i);
				 i++;
			 }
			 gender="";
			 i=userString.indexOf("gender");
			 i+=9;
			 while (userString.charAt(i)!='"'){
				 gender+=userString.charAt(i);
				 i++;
			 }
			 return userName +" "+ gender + " " + birthString;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static List<String> convertToCollectionList(String collectionString){
		String s=collectionString;
		List<String> list = new LinkedList<String>();
		int t1=s.indexOf("businessId"); 
		int length=s.length();
		s=s.substring(t1+13,length);
		int t=0;
		String tString="";
		while (t!=-1){
			while (s.charAt(t)!='"'){
				tString+=s.charAt(t);
				t++;
			 }
			//Insert add tString to StringList
			 list.add(tString);
			 tString="";
			 t=s.indexOf("businessId");
			 if (t!=-1){
			 length=s.length();
			 s=s.substring(t+13,length);
			 t=0;
			 }
		}
		return list;
	}
	
	public static String convertToMessage(String msg){
		String s=msg;
		int t=s.indexOf("message"); 
	    t+=10;
	    String tString="";
	    while (s.charAt(t)!='"'){
			tString+=s.charAt(t);
			t++;
		}
		return tString; 
	}
	
	public static User convertToUserObject(String userString){
		User user = new User();	// 声明一个User对象
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
		try {
			String[] temp = userString.split(" ");
			user.setUserName(temp[0]);
			user.setGender(temp[1]);
			user.setBirth(sdf.parse(temp[2]));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
	public static byte[] getBytes(Bitmap bitmap){  
	    //实例化字节数组输出流  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图  
	    return baos.toByteArray();//创建分配字节数组  
	}    
	
	public static Bitmap getBitmap(byte[] data){  
		if(data==null)
			return null;
		else
	      return BitmapFactory.decodeByteArray(data, 0, data.length);//从字节数组解码位图  
	}  
}
