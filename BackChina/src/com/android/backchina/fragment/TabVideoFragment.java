package com.android.backchina.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.android.backchina.R;
import com.android.backchina.base.BaseFragment;
import com.android.backchina.utils.TLog;

public class TabVideoFragment extends BaseFragment{
    
	private WebView webView;
	private View xCustomView;
	private FrameLayout video_fullView;// 全屏时视频加载view
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    
    private VideoWebChromeClient chromeClient;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	 TLog.i("called");
    	View view =  inflater.inflate(R.layout.fragment_vedio_layout, null);
    	return view;
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onViewCreated(view, savedInstanceState);
    	 TLog.i("called");
    	setupViews(view);
    }
    
    private void setResetWebViewSettings(WebView webView){
        WebSettings ws = webView.getSettings();
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        // ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕
        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);// 新加
    }
    
    private void setupViews(View view){
    	webView = (WebView) view.findViewById(R.id.webview);
        video_fullView = (FrameLayout) view.findViewById(R.id.video_fullview);
        //
        setResetWebViewSettings(webView);
        chromeClient = new VideoWebChromeClient();
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(new VideoWebViewClient());
        webView.loadUrl("http://look.appjx.cn/mobile_api.php?mod=news&id=12603");
 
    }
    
    public class VideoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	 TLog.i("called");
            view.loadUrl(url);
            return false;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            TLog.i("called");
        }
    }
    
    public class VideoWebChromeClient extends WebChromeClient {
        private View xprogressvideo;
        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            TLog.i("已经进入了。。。。。。。。");
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            webView.setVisibility(View.INVISIBLE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            video_fullView.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            video_fullView.setVisibility(View.VISIBLE);
        }
        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {
            if (xCustomView == null){// 不是全屏播放状态
                return;
            }
            TLog.i("called");
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            video_fullView.removeView(xCustomView);
            xCustomView = null;
            video_fullView.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            webView.setVisibility(View.VISIBLE);
        }
        // 视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {
            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater
                        .from(getActivity());
                xprogressvideo = inflater.inflate(
                        R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }
    }
    
    public boolean inCustomView() {
        return (xCustomView != null);
    }
    
    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
    	 TLog.i("called");
    	chromeClient.onHideCustomView();
    	getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    @Override
	public void onResume() {
        super.onResume();
        TLog.i("called");
        webView.onResume();
        webView.resumeTimers();
        /**
         * 设置为横屏
         */
        if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
        	getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    
    @Override
	public void onPause() {
        super.onPause();
        TLog.i("called");
        webView.onPause();
        webView.pauseTimers();
    }
    
    /**
     * 主要是把webview所持用的资源销毁，
     */
    @Override
	public void onDestroy() {
        super.onDestroy();
        TLog.i("called");
        video_fullView.removeAllViews();
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
        webView = null;
    }

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
