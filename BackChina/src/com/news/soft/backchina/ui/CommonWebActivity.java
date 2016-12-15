package com.news.soft.backchina.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.news.soft.backchina.R;
import com.news.soft.backchina.base.BaseActivity;
import com.news.soft.backchina.ui.empty.EmptyLayout;
import com.news.soft.backchina.utils.UIHelper;

public class CommonWebActivity extends BaseActivity{

	public final static String BUNDLE_KEY_COMMON_URL = "BUNDLE_KEY_COMMON_URL";
	
	private ImageView mBtnBack;
	
	private String mUrl;
	
	private WebView mWebView;
	
	private Context mContext;
	
	private EmptyLayout mEmptyLayout;
	
    public static void show(Context context,String url) {
        Intent intent = new Intent(context, CommonWebActivity.class);
        intent.putExtra(BUNDLE_KEY_COMMON_URL, url);
        context.startActivity(intent);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initBundle(getIntent().getExtras());
		setContentView(R.layout.activity_common_web_layout);
		mContext = this;
		setupViews();
		initData();
	}
	
	protected void initBundle(Bundle bundle) {
		mUrl = bundle.getString(BUNDLE_KEY_COMMON_URL);
	}
	
	private void setupViews(){
		mBtnBack = (ImageView) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mWebView = (WebView) findViewById(R.id.web_view);
		mEmptyLayout = (EmptyLayout) findViewById(R.id.error_layout);
	}
	
	private void initData(){
		setEmptyLayoutStatus(EmptyLayout.NETWORK_LOADING);
		//
		WebSettings settings = mWebView.getSettings();
		settings.setDefaultTextEncodingName("UTF-8");
        settings.setDefaultFontSize(14);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        
        //
        setWebViewContent(mUrl, new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setEmptyLayoutStatus(EmptyLayout.HIDE_LAYOUT);
			}
		});
	}
	
    void setWebViewContent(String url,Runnable callback) {
    	mWebView.setWebViewClient(new CommonWebClient(callback));
    	mWebView.loadUrl(url);
    }
    
    protected void setEmptyLayoutStatus(int type){
    	if (mEmptyLayout != null) {
    		mEmptyLayout.setErrorType(type);
		}
    }
    
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }
    
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	 if (mWebView != null) {
             mWebView.destroy();
             mWebView = null;
         }
    }
    
    private class CommonWebClient extends WebViewClient implements Runnable {
        private Runnable mFinishCallback;
        private boolean mDone = false;

        CommonWebClient(Runnable finishCallback) {
            super();
            mFinishCallback = finishCallback;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mDone = false;
            // 当webview加载2秒后强制回馈完成
            view.postDelayed(this, 2800);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            run();
        }

        @Override
        public synchronized void run() {
            if (!mDone) {
                mDone = true;
                if (mFinishCallback != null) {
                mFinishCallback.run();
                }
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	UIHelper.enterCommonWebActivity(mContext, url);
        	return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // TODO
            handler.cancel();
        }
    }
}
