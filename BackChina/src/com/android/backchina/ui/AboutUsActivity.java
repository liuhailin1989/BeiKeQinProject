package com.android.backchina.ui;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.bean.BlogDetail;
import com.android.backchina.bean.Login;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.utils.TLog;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class AboutUsActivity extends BaseActivity{
	
	private TextView mTitle;
	
    private WebView mWebView;
    
    private TextHttpResponseHandler mTestHandler;
	
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
    }
    
    private void initData(){
    	mTestHandler = new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, String responseString) {
				// TODO Auto-generated method stub
				handleLoginResponse(headers,responseString);
			}
			
			@Override
			public void onFailure(int code, Header[] headers, String responseString, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		};
    	mTitle.setText("关于我们");
//    	mWebView.loadUrl("http://www.backchina.com/api/appxml/aboutus.php");
    	//
//    	mWebView.loadUrl("http://www.backchina.com/blog/281424/article-259378.html");
    	String url = "http://www.backchina.com/blog/281424/article-259378.html?appxml=1&json=1";
    	BackChinaApi.getHttp(url, mTestHandler);
    }
    
    private void handleLoginResponse(Header[] headers,String response) {
        TLog.d("response =" + response);
        Type type = new TypeToken<ResultListBean<BlogDetail>>() {
        }.getType();
        ResultListBean<BlogDetail> bean = AppContext.createGson().fromJson(response, type);
        BlogDetail blogDetail = bean.getItems().get(0);
        mWebView.loadDataWithBaseURL("", blogDetail.getContent(), "text/html", "UTF-8", "");
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
