package com.android.backchina.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.bean.NewsDetail;
import com.android.backchina.bean.base.ResultBean;
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
    	WebSettings settings = mWebView.getSettings();
    	//支持javascript
    	settings.setJavaScriptEnabled(true); 
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
//    	url ="http://www.backchina.com/news/2016/09/08/447012.html?appxml=1&json=1";
    	url = "http://www.backchina.com/news/2016/09/08/447011.html?appxml=1&json=1";
    	BackChinaApi.getHttp(url, mTestHandler);
    }
    
//    private void handleLoginResponse(Header[] headers,String response) {
//        TLog.d("response =" + response);
//        Type type = new TypeToken<ResultListBean<BlogDetail>>() {
//        }.getType();
//        ResultListBean<BlogDetail> bean = AppContext.createGson().fromJson(response, type);
//        BlogDetail blogDetail = bean.getItems().get(0);
//        saveFile(blogDetail.getContent());
//        mWebView.loadDataWithBaseURL("", blogDetail.getContent(), "text/html", "UTF-8", "");
//    }
    
    private void handleLoginResponse(Header[] headers,String response) {
        TLog.d("response =" + response);
        Type type = new TypeToken<ResultBean<NewsDetail>>() {
        }.getType();
        ResultBean<NewsDetail> bean = AppContext.createGson().fromJson(response, type);
        NewsDetail newsDetail = bean.getResult();
        saveFile(newsDetail.getContent());
        mWebView.loadDataWithBaseURL("", newsDetail.getContent(), "text/html", "UTF-8", "");
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
    
    
	public static void saveFile(String text) {
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +File.separator +"web_content.txt";
		try {
			File saveFile = new File(filePath);
			if(saveFile.exists()){
				saveFile.delete();
			}
			if (!saveFile.exists()) {
				File dir = new File(saveFile.getParent());
				dir.mkdirs();
				saveFile.createNewFile();
			}

			FileOutputStream outputStream = new FileOutputStream(saveFile);
			outputStream.write(text.getBytes());
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
