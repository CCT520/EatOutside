package com.example.search;



import com.example.aaa.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebView;

public class BookMeal extends Activity {
	WebView bookview;
	String url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_meal);
		Bundle bundle= new Bundle();
		bundle=this.getIntent().getExtras();//
        url=bundle.getString("¶©²ÍÍøÖ·");//
		bookview = (WebView)findViewById(R.id.bookwebView);
		bookview.loadUrl(url);
		finish();
	}



}
