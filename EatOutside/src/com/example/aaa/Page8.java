package com.example.aaa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Page8 extends Activity{
	private int[] imageIds=new int[]
			{R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
	private String[] restaurants=new String[]
			{"刘志文","大菊花","李撒从"};
	private String[] distances=new String[]
			{"1","2","3"};
	private String[] averages=new String[]
			{"1","2","3"};
	private String[] kinds=new String[]
			{"茶","shit","蛋炒饭"};
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page8);
		Button btn1;
		btn1 = (Button)findViewById(R.id.back);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();  
				
			}
		});
		List<Map<String,Object>> listItems=
				new ArrayList<Map<String,Object>>();
		for(int i=0;i<restaurants.length;i++){
			Map<String,Object> listItem=
					new HashMap<String,Object>();
			listItem.put("restaurantname",restaurants[i]);
			listItem.put("distance", distances[i]);
			listItem.put("imagerestaurant", imageIds[i]);
			listItem.put("average", averages[i]);
			listItem.put("kind", kinds[i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter=new SimpleAdapter
				(this, listItems, R.layout.list_item, 
						new String[]{"restaurantname","distance",
						"imagerestaurant","average","kind"}, 
						new int[]{R.id.restaurant,R.id.distance,
						R.id.image_restaurant,R.id.average,R.id.kind});
		ListView list=(ListView) findViewById(R.id.mylist);
		list.setAdapter(simpleAdapter);
		
	}
	
}