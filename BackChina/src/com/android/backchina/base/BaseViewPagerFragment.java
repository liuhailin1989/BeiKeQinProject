package com.android.backchina.base;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.backchina.AppOperator;
import com.android.backchina.R;
import com.android.backchina.adapter.ViewPageFragmentAdapter;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.widget.PagerSlidingTabStrip;
import com.android.backchina.widget.PagerSlidingTabStrip.OnClickTabListener;

public abstract class BaseViewPagerFragment extends BaseFragment implements OnClickTabListener{
    
    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;
    protected EmptyLayout mErrorLayout;
    protected View mRoot;

    protected ImageView mBtnChannel;
    
    @Override
    protected int getLayoutId() {
    	// TODO Auto-generated method stub
    	return R.layout.base_viewpage_fragment;
    }
    
    @Override
    protected void setupViews(View root) {
    	// TODO Auto-generated method stub
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
         mErrorLayout.setOnLayoutClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onShow();
			}
		});
         mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
                 mTabStrip, mViewPager);
         mTabStrip.setOnClickTabListener(this);
         setScreenPageLimit();
    }
    
    @Override
    protected void initData() {
    	// TODO Auto-generated method stub
    	super.initData();
    }
    
    @Override
    protected void onShow() {
    	// TODO Auto-generated method stub
    	mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
    	requestData();
    }
    
    @Override
    protected void onHide() {
    	// TODO Auto-generated method stub
		if (mErrorLayout != null) {
			mErrorLayout.dismiss();
		}
    }
    
    @Override
    public void onClickTab(View tab, int index) {
    	// TODO Auto-generated method stub
    	
    }
    
    public void requestData() {
    	AppOperator.runOnThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
            	onRequestData();
            }
        });
    }
    
    protected void setScreenPageLimit() {
    	 mViewPager.setOffscreenPageLimit(3);
    }
    
    protected void onRequestData() {

    }
    
	protected void onRequestError(int code) {
		mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
	}
    
    protected void onRequestSuccess() {
    	mErrorLayout.dismiss();
    }
    
    protected abstract void enterChannelManagerActivity();
}