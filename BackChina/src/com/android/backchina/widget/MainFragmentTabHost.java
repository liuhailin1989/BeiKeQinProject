package com.android.backchina.widget;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

import com.android.backchina.utils.TLog;

public class MainFragmentTabHost extends FragmentTabHost{

    private String mCurrentTag;
    
    private String mNoTabChangedTag;
    
    public MainFragmentTabHost(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    public MainFragmentTabHost(Context context, AttributeSet attrs) {
        super(context,attrs);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void onTabChanged(String tag) {
        // TODO Auto-generated method stub
        TLog.i("tag =" + tag +",mNoTabChangedTag =" + mNoTabChangedTag);
        if (tag.equals(mNoTabChangedTag)) {
            setCurrentTabByTag(mCurrentTag);
        } else {
            super.onTabChanged(tag);
            mCurrentTag = tag;
        }
    }
    
    public void setNoTabChangedTag(String tag) {
        this.mNoTabChangedTag = tag;
    }
    
}
