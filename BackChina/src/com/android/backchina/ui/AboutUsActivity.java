package com.android.backchina.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.backchina.R;
import com.android.backchina.base.BaseActivity;

public class AboutUsActivity extends BaseActivity{
	
	private TextView mTitle;
	
    private WebView mWebView;
    
    public static void show(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_layout);
        setupViews();
        initData();
    }
    
    
    private void setupViews(){
    	mTitle = (TextView) findViewById(R.id.tv_title);
    	mWebView = (WebView) findViewById(R.id.web_view);
    	WebSettings settings = mWebView.getSettings();
    	//支持javascript
    	settings.setJavaScriptEnabled(true); 
    }
    
    private void initData(){
    	mTitle.setText("关于我们");
    	mWebView.loadUrl("http://www.backchina.com/api/appxml/aboutus.php");
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	if(mWebView != null){
    		mWebView.destroy();
    		mWebView = null;
    	}
    }
}
