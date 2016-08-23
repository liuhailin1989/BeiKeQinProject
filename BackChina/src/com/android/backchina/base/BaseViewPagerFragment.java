package com.android.backchina.base;

import com.android.backchina.R;
import com.android.backchina.adapter.ViewPageFragmentAdapter;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.widget.PagerSlidingTabStrip;

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
            onSetupTabAdapter(mTabsAdapter);

            mRoot = root;
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

    protected void setScreenPageLimit() {
    }

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);
    
    protected abstract void enterChannelManagerActivity();
}