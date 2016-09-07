package com.android.backchina.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.backchina.R;
import com.android.backchina.base.BaseFragment;
import com.android.backchina.interf.IContractDetail;
import com.android.backchina.widget.BackChinaWebView;

public abstract class DetailFragment<T> extends BaseFragment<Object>{
    
    IContractDetail iDetail;
    
    BackChinaWebView mWebView;
    
    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        iDetail = (IContractDetail) context;
        
        super.onAttach(context);
    }
    
    @Override
    protected void setupViews(View root) {
        // TODO Auto-generated method stub
        super.setupViews(root);
        initWebView();
    }
    
    private void initWebView(){
        BackChinaWebView webView = new BackChinaWebView(getActivity());
        ((ViewGroup)mRoot.findViewById(R.id.lay_webview_container)).addView(webView);
        mWebView = webView;
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
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
    }
    
    
    void setWebViewContent(String content) {
        mWebView.loadDetailDataAsync(content, new Runnable() {
            @Override
            public void run() {
                if(iDetail != null){
                    iDetail.hideLoading();
                }
            }
        });
    }
}
