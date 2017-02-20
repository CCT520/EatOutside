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
	
	private List<String> collections;	// �ղؼ��̼�ID����
	private List<Business> businesses = new LinkedList<Business>();	// �ղص��̼Ҽ���
	private String original;	// ����һ���洢Server���ص�ԭʼ���ݵ��ַ���
	private String result;		// ����һ��original������ȡ�Ľ���ַ���
	private ProgressDialog pdialog;		// ���ȿ�
	private ListView list;				// �б�
	private Handler handler;			// ����������Ϣ��Handler
	private int position;
	private Bitmap bitmap;		// ����һ���̻�ͼƬ��bitmap
	private List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
	private SimpleAdapter simpleAdapter;
	private Button back;	//���ذ�ť
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page5);
		//��ʾ���ȿ��½��߳�
		pdialog = ProgressDialog.show(CollectionPage.this, "���ڼ���...", "ϵͳ���ڴ�����������");
		
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// ����Handler
		handler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
			public void handleMessage(Message msg) {
				Log.i("msg", "" + msg.what);
				if(result.equals(ConnectServer.FAIL)){
					// ����һ����ʾ��
					new AlertDialog.Builder(CollectionPage.this).setTitle("��ʾ")//������ʾ
					.setMessage(ConnectServer.FAIL)// ������ʾ����
					.setNegativeButton("����", new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							}
						}).show();
					pdialog.dismiss();
						return;
					}
					if(msg.what == 0x101){	//����GetCollection
						list=(ListView) findViewById(R.id.mylist);
						list.setAdapter(simpleAdapter);
						list.setOnItemClickListener(new MyListClickListener());
						list.setOnItemLongClickListener((android.widget.AdapterView.OnItemLongClickListener) new OnItemLongClickListener());
						pdialog.dismiss();
					}
					if(msg.what == 0x102){	// ����DelCollection
						pdialog.dismiss();
						// ����һ����ʾ��
						new AlertDialog.Builder(CollectionPage.this).setTitle("��ʾ")//������ʾ
						.setMessage("ɾ���ɹ���")// ������ʾ����
						.setNegativeButton("����", new DialogInterface.OnClickListener(){
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
	 * ��ת���̻�����ҳ�棬����Ӧ��Business���󴫹�ȥ
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
	// ɾ���ղؼ�ĳ�̻�
	class OnItemLongClickListener implements AdapterView.OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			position=arg2;
			// ����һ����ʾ��
			new AlertDialog.Builder(CollectionPage.this).setTitle("�ɹ�")//������ʾ
			.setMessage("ȷ��ɾ����")// ������ʾ����
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					listItems.remove(position);
					simpleAdapter.notifyDataSetChanged();
					//��ʾ���ȿ��½��߳�
					pdialog = ProgressDialog.show(CollectionPage.this, "���ڼ���...", "ϵͳ���ڴ�����������");
					new Thread(new DelCollection(position)).start();
				}
			})
			.setNegativeButton("����", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing
				}
			}).show();
			return true;
		}
	}
	/**
	 * ������ȡ�ղص��̻�ID���̻���Ϣ
	 */
	class GetCollection implements Runnable{

		@Override
		public void run() {
			Business business;
			String action = "returnCollection.action";
			original = ConnectServer.send(action);
			// �������ɹ�
			if(!original.equals(ConnectServer.FAIL)){
				// ת��Ϊ�̻�ID����
				collections = ConvertTool.convertToCollectionList(original);
				for(int i=0; i<collections.size(); i++){
					//�Լ��ϵ��������ô��ڵ���������Business����
					business = new Business(get(collections.get(i)));
					bitmap = ConnectServer.getPicture(business.getS_photo_url());
					// ��ͼƬ���ֽ�������Business������
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
					// ��Business�����еõ�bitmap��byte�ֽ�����Ȼ��ת����ͼƬ
					listItem.put("imagerestaurant", ConvertTool.getBitmap(businesses.get(i).getBitmapBytes()));
					listItem.put("average", businesses.get(i).getPrice());
					listItem.put("kind", businesses.get(i).getCategories());
					listItems.add(listItem);
				}
			}
			//����һ��SimpleAdapter
			simpleAdapter=new SimpleAdapter
					(CollectionPage.this, listItems, R.layout.list_item, 
							new String[]{"restaurantname","distance",
							"imagerestaurant","average","kind"}, 
							new int[]{R.id.restaurant,R.id.distance,
							R.id.image_restaurant,R.id.average,R.id.kind});
			simpleAdapter.setViewBinder(new ViewBinder() {  
	            
	            public boolean setViewValue(View view, Object data,  
	                    String textRepresentation) {  
	                //�ж��Ƿ�Ϊ����Ҫ����Ķ���  
	                if(view instanceof ImageView  && data instanceof Bitmap){  
	                    ImageView iv = (ImageView) view;  
	                    iv.setImageBitmap((Bitmap) data);  
	                    return true;  
	                }else  
	                return false;  
	            }  
	        });  
			Message m = new Message();
			m.what = 0x101;	//�Ӷ�ȡ�û���Ϣ�̷߳���
			handler.sendMessage(m);	// ������Ϣ
			
			
		}
		public String get(String businessId){
			// �Ӵ��ڵ�����ȡ���̻��ַ�������ת����Business����
	        String singleUrl = "http://api.dianping.com/v1/business/get_single_business?";
	        Map<String, String> param = new HashMap<String, String>();
	        param.put("business_id", businessId);
	        return DemoApiTool.requestApi(singleUrl, DemoApiTool.appKey, DemoApiTool.secret, param);
	       
		}
	}// end of GetCollection
	
	/**
	 * class delColletionɾ��ĳ���ղأ����־û�
	 */
	class DelCollection implements Runnable{

		int pos;		// ��Ҫɾ���̻���list�е�λ�ã����ⲿ����
		
		public DelCollection(int pos) {
			this.pos = pos;
		}
		
		@Override
		public void run() {
			String action = "delCollection.action?businessId=" + collections.get(pos);
			original = ConnectServer.send(action);
			if(!original.equals(ConnectServer.FAIL)){	// �������ɹ�
				result = ConvertTool.convertToMessage(original);	
				if(result.equals(ConnectServer.SUCCESS)){
					// ������ص���Ϣ��message���ɹ���success��
					collections.remove(pos);
					businesses.remove(pos);
				}else
					result = ConnectServer.FAIL;
			}else
				result = ConnectServer.FAIL;
			Message m = new Message();
			m.what = 0x102;	//�Ӷ�ȡ�û���Ϣ�̷߳���
			handler.sendMessage(m);	// ������Ϣ
		}
		
	}
	
}
	
