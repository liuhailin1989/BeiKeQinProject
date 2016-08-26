package com.android.backchina.viewpagerfragment;

import android.os.Bundle;

import com.android.backchina.R;
import com.android.backchina.adapter.ViewPageFragmentAdapter;
import com.android.backchina.base.BaseViewPagerFragment;
import com.android.backchina.fragment.NewsFragment;

public class TabBlogFragment extends BaseViewPagerFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        // TODO Auto-generated method stub
        String[] title = getResources().getStringArray(
                R.array.blog_viewpage_arrays);
        adapter.addTab(title[0], "blog", NewsFragment.class,null);
        
    }

    @Override
    protected void enterChannelManagerActivity() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    
}
