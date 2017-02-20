package com.example.aaa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.search.SearchNearResturant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Activity implements OnClickListener{
	
	private String[] options=new String[]{"��ϵ","Ԥ��","�˾�","����","�Ż�",
			"�Ź�","����"};
	private String[] styleofcookings=new String[]{"����","����","���","³��",
			"����","����"};
	private String[] reservations=new String[]{"��","��"};
	private String[] prices=new String[]{"0~50Ԫ","50~100Ԫ","100~300Ԫ","300~500Ԫ","500Ԫ����"};
	private String[] distances=new String[]{"0~1Km","1~5Km","5~10Km","10Km����"};
	private String[] discounts=new String[]{"��","��"};
	private String[] grouppurchases=new String[]{"��","��"};
	private String[] scores=new String[]{"1-2","2-3","3-4","4-5"};
	private String[][] choices={styleofcookings,reservations,prices,distances,discounts,grouppurchases,scores};
	private String[] temps={" "," "," "," "," "," "," "};
	private int[] choicenum={6,2,5,4,2,2,4};
	private int[] search_items=new int[]{6,6,6,6,6,6,6,6};//�û�ѡ���������Ŀ
	private int[] caseitem=new int[]{0,1,2,3,4,5,6};
	private int m=0;
	private int k=0;
	 Handler update_progress_bar = new Handler(){
		 public void handleMessage(Message msg) {
	            // TODO Auto-generated method stub
	            //super.handleMessage(msg);
	            //��ʾ������
			 if(msg.arg1<=6){
		ListView list=(ListView) findViewById(R.id.listView1);
         View view=list.getChildAt(msg.arg1%7);
         view.setBackgroundColor(Color.rgb(237, 213, 44));
         for(int i=1;i<=6;i++){
        	 View view1=list.getChildAt((msg.arg1+i)%7);
        	 view1.setBackgroundColor(Color.rgb(240, 240, 240));
         }
			 }
	 }
	 };
	Runnable update_thread=new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg=update_progress_bar.obtainMessage();
			msg.arg1=m;
			update_progress_bar.sendMessage(msg);
			update_progress_bar.removeCallbacks(update_thread);
		}
		
	};
	 Handler update_listview_bar=new Handler(){
		 public void handleMessage(Message Msg){
			 if(m<7){
			 temps[m-1]=choices[m-1][Msg.arg1];
			 }
			 List<Map<String,Object>> listItems=
						new ArrayList<Map<String,Object>>();
				for(int i=0;i<7;i++){
					Map<String,Object> listItem=
							new HashMap<String,Object>();
					listItem.put("textView1",temps[i]);
					listItems.add(listItem);
				}
				SimpleAdapter simpleAdapter=new SimpleAdapter
						(Search.this, listItems, R.layout.listitem_search,
								new String[]{"textView1"}, 
								new int[]{R.id.textView1});
				simpleAdapter.notifyDataSetChanged();
				ListView list=(ListView) findViewById(R.id.listView3);
				list.setAdapter(simpleAdapter);
		 }
	 };
	 Runnable update_listview_thread=new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message Msg=update_listview_bar.obtainMessage();
			Msg.arg1=k;
			update_listview_bar.sendMessage(Msg);
			update_listview_bar.removeCallbacks(update_listview_thread);
		}
		 
	 };
	void chooseoption(int n){
		//���ڡ���ϵ��Ԥ�����˾�������listview��ת��ÿ��С���listview
		m=n;
		if(m<=6){
		List<Map<String,Object>> listItems=
				new ArrayList<Map<String,Object>>();
		for(int i=0;i<choicenum[m];i++){
			Map<String,Object> listItem=
					new HashMap<String,Object>();
			listItem.put("textView1",choices[m][i]);
			listItems.add(listItem);
		}
		SimpleAdapter simpleAdapter=new SimpleAdapter
				(Search.this, listItems, R.layout.listitem_search,
						new String[]{"textView1"}, 
						new int[]{R.id.textView1});
		simpleAdapter.notifyDataSetChanged();
		ListView list=(ListView) findViewById(R.id.listView2);
		list.setAdapter(simpleAdapter);
		list.setOnItemClickListener(new MyListClickListener1());}

	}
	void chooseoption1(int n){
			chooseoption(m+1);
			update_progress_bar.post(update_thread);
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		//findViewById(R.id.button8).setOnClickListener(this);
		//findViewById(R.id.button9).setOnClickListener(this);
		findViewById(R.id.search).setOnClickListener(this);
		List<Map<String,Object>> listItems=
				new ArrayList<Map<String,Object>>();
		for(int i=0;i<7;i++){
			Map<String,Object> listItem=
					new HashMap<String,Object>();
			listItem.put("textView1",options[i]);
			listItems.add(listItem);
			}
		SimpleAdapter simpleAdapter=new SimpleAdapter
				(this, listItems, R.layout.listitem_search, 
						new String[]{"textView1"}, 
						new int[]{R.id.textView1});
		ListView list=(ListView) findViewById(R.id.listView1);
		list.setAdapter(simpleAdapter);
		list.setBackgroundColor(Color.rgb(240, 240, 240));
		list.setOnItemClickListener(new MyListClickListener());
		
		
		List<Map<String,Object>> listItems3=
				new ArrayList<Map<String,Object>>();
		for(int i=0;i<7;i++){
			Map<String,Object> listItem3=
					new HashMap<String,Object>();
			listItem3.put("textView1",temps[i]);
			listItems3.add(listItem3);
			}
		SimpleAdapter simpleAdapter3=new SimpleAdapter
				(this, listItems3, R.layout.listitem_search, 
						new String[]{"textView1"}, 
						new int[]{R.id.textView1});
		ListView list3=(ListView) findViewById(R.id.listView3);
		list3.setAdapter(simpleAdapter3);

			}
	/*public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = getLayoutInflater();
        convertView = mInflater.inflate(R.layout.listitem_search, null);
//����Ϳ���д��Ҫ�ı���ɫ�Ĵ�����
 convertView.setBackgroundColor(Color.white)//��������Ϊ��ɫ
 return convertView;

}*/
	class MyListClickListener implements AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			view.setBackgroundColor(Color.rgb(237, 213, 44));
			update_progress_bar.post(update_thread);
			switch(position){
			case 0:
				chooseoption(0);
				break;
			case 1:
				chooseoption(1);
				break;
			case 2:
				chooseoption(2);
				break;
			case 3:
				chooseoption(3);
				break;
			case 4:
				chooseoption(4);
				break;
			case 5:
				chooseoption(5);
				break;
			case 6:
				chooseoption(6);
				break;
			}
		}
	}
	class MyListClickListener1 implements AdapterView.OnItemClickListener{
		//���ݵ�һ��ѡ���ѡ� ���ѡ�����ѡ��֮�� �õ�ѡ��Ľ�� ͨ��m������һ��ѡ�
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			switch(k=position){
			case 0:
				search_items[m]=0;
				break;
			case 1:
				search_items[m]=1;
				break;
			case 2:
				search_items[m]=2;
				break;
			case 3:
				search_items[m]=3;
				break;
			case 4:
				search_items[m]=4;
				break;
			case 5:
				search_items[m]=5;
				break;
			}
			
			if(m<=6){
				chooseoption1(m);
				update_listview_bar.post(update_listview_thread);
				
			}

			else{
				temps[6]=choices[6][k];
		        List<Map<String,Object>> listItems3=
						new ArrayList<Map<String,Object>>();
				for(int i=0;i<7;i++){
					Map<String,Object> listItem3=
							new HashMap<String,Object>();
					listItem3.put("textView1",temps[i]);
					listItems3.add(listItem3);
					}
				SimpleAdapter simpleAdapter3=new SimpleAdapter
						(Search.this, listItems3, R.layout.listitem_search, 
								new String[]{"textView1"}, 
								new int[]{R.id.textView1});
				ListView list3=(ListView) findViewById(R.id.listView3);
				list3.setAdapter(simpleAdapter3);
			}
		
	}
	}
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		
		case R.id.search:
			 System.out.println("�û���ѡ����"+temps);
//				MyCategory = temps[0];
//				MyHas_online_reservation = temps[1];
//				MyRadius = temps[3];
//				MyHas_coupon = temps[4];
//				MyHas_deal = temps[5];
			    if(temps[0].equals(" "))
			    	temps[0]="����";
			    if(temps[1].equals(" "))
			    	temps[1]="��";
			    temps[4]="��";
			    if(temps[5].equals(" "))
			    	temps[5]="��";
				Intent intent=new Intent();
				Bundle bundle= new Bundle();
				bundle.putString("���", temps[0]);
				System.out.println("�ؼ���ϵ"+temps[0]+"�뾶"+temps[3]);
				System.out.println("�ؼ���ϵ"+temps[0]+temps[1]+temps[4]+temps[5]);
				bundle.putString("�뾶", temps[3]);
				bundle.putString("�Ż�", temps[4]);
				bundle.putString("�Ź�", temps[5]);
				bundle.putString("����Ԥ��", temps[1]);
				intent.putExtras(bundle);
				intent.setClass(Search.this,SearchNearResturant.class);
			    startActivity(intent);
				break;
			//System.out.println(temps[0]+temps[1]+temps[2]+temps[3]+temps[4]+temps[5]+temps[6]);

		}
	}
}
	
