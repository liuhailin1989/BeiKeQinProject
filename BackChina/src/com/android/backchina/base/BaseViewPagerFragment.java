package com.android.backchina.base;

import java.lang.reflect.Type;

import com.android.backchina.AppOperator;
import com.android.backchina.BackChinaApplication;
import com.android.backchina.R;
import com.android.backchina.adapter.ViewPageFragmentAdapter;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.base.ChannelBean;
import com.android.backchina.bean.base.PageBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.TLog;
import com.android.backchina.widget.PagerSlidingTabStrip;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public abstract class BaseViewPagerFragment extends BaseFragment {
    
    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;
    protected View mRoot;

    protected ImageView mBtnChannel;
    
    protected TextHttpResponseHandler mHandler;
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRoot == null) {
            View root = inflater.inflate(R.layout.base_viewpage_fragment, null);

            mTabStrip = (PagerSlidingTabStrip) root
                    .findViewById(R.id.pager_tabstrip);

            mViewPager = (ViewPager) root.findViewById(R.id.pager);

            mBtnChannel = (ImageView) root.findViewById(R.id.iv_btn_add_tab);
            
            mBtnChannel.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    enterChannelManagerActivity();
                }
            });
            
            mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);

            mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
                    mTabStrip, mViewPager);
            setScreenPageLimit();
//            onSetupTabAdapter(mTabsAdapter);
            mRoot = root;
            setupViews(mRoot);
            initData();
        }else{
        	 ViewGroup parent = (ViewGroup) mRoot.getParent();
             if (null != parent) {
                 parent.removeView(mRoot);
             }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt("position");
            mViewPager.setCurrentItem(pos, true);
        }
    }
    
    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        super.initData();
        mHandler = new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                TLog.d("called");
                onRequestError(statusCode);
                onRequestFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // TODO Auto-generated method stub
                try {
                    ChannelBean<ChannelItem> channelBean = BackChinaApplication.createGson().fromJson(responseString, getType());
                    if (channelBean != null && channelBean.getItems() != null) {
                        //
                        onRequestSuccess(1);
                        setListData(channelBean);
                    }
                    onRequestFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }
            
        };
        AppOperator.runOnThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                requestData();
            }
        });
    }
    
    protected void requestData() {
        onRequestStart();
    }
    
    protected void setListData(ChannelBean<ChannelItem> channelBean) {
        
    }
    
    
    protected void onRequestStart() {

    }
    
    protected void onRequestSuccess(int code) {

    }
    
    protected void onRequestError(int code) {
        
    }
    
    protected void onRequestFinish() {
        
    }

    protected void setScreenPageLimit() {
    }
    
    protected abstract Type getType();

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);
    
    protected abstract void enterChannelManagerActivity();
}