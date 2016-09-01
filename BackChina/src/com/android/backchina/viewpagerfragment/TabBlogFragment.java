package com.android.backchina.viewpagerfragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.backchina.AppOperator;
import com.android.backchina.adapter.ViewPageFragmentAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseListFragment;
import com.android.backchina.base.BaseViewPagerFragment;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.base.ChannelBean;
import com.android.backchina.fragment.NewsFragment;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.manager.ChannelManager;
import com.android.backchina.ui.ChannelActivity;
import com.android.backchina.utils.TLog;
import com.google.gson.reflect.TypeToken;

public class TabBlogFragment extends BaseViewPagerFragment implements OnTabReselectListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void setupViews(View root) {
        // TODO Auto-generated method stub
        super.setupViews(root);
    }
    
    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void requestData() {
        // TODO Auto-generated method stub
        super.requestData();
        final List<ChannelItem> localChannelItems = ChannelManager.getInstance().getNewsChannelItemsFromTabLocal(getActivity());
        if (mTabsAdapter != null && localChannelItems != null && localChannelItems.size() > 0) {
            AppOperator.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    for(ChannelItem item : localChannelItems){
                        TLog.d("tab name =" +item.getName());
                        mTabsAdapter.addTab(item.getName(), item.getName(), NewsFragment.class, getBundle(item.getId(),item));
                    }
                    //
                    Fragment fragment = mTabsAdapter.getItem(mViewPager.getCurrentItem());
                    if (fragment != null && fragment instanceof BaseListFragment) {
                        ((BaseListFragment) fragment).onTabReselect();
                    }
                }
            });
        } else { 
            BackChinaApi.getBlogChannelList(mHandler);
        }
    }
    
    @Override
    protected void setListData(ChannelBean<ChannelItem> channelBean) {
        // TODO Auto-generated method stub
        TLog.d("called");
        if(mTabsAdapter != null && channelBean != null){
            List<ChannelItem> datas = channelBean.getItems();
            ChannelManager.getInstance().saveNewsChannelItemToTabAll(getActivity(), datas);
            //
            List<ChannelItem> defaultLocalChannelItems = new ArrayList<ChannelItem>();
            for (int i = 0; i < 5; i++) {
                if (null != datas.get(i)) {
                    defaultLocalChannelItems.add(datas.get(i));
                }
            }
            ChannelManager.getInstance().saveNewsChannelItemToTabLocal(getActivity(), defaultLocalChannelItems);
            //
            for(ChannelItem item : defaultLocalChannelItems){
                TLog.d("tab name =" +item.getName());
                mTabsAdapter.addTab(item.getName(), item.getName(), NewsFragment.class, getBundle(item.getId(),item));
            }
            //
            Fragment fragment = mTabsAdapter.getItem(mViewPager.getCurrentItem());
            if (fragment != null && fragment instanceof BaseListFragment) {
                ((BaseListFragment) fragment).onTabReselect();
            }
        } 
    }
    
    private Bundle getBundle(int catalog,ChannelItem item) {
        Bundle bundle = new Bundle();
        bundle.putInt(NewsFragment.BUNDLE_KEY_CATALOG, catalog);
        bundle.putSerializable(NewsFragment.BUNDLE_KEY_CHANNELITEM, item);
        return bundle;
    }
    
    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(3);
    }
    
    
    @Override
    public void onTabReselect() {
        Fragment fragment = mTabsAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment != null && fragment instanceof BaseListFragment) {
            ((BaseListFragment) fragment).onTabReselect();
        }
    }
    
    @Override
    protected void enterChannelManagerActivity() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(getActivity(), ChannelActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected Type getType() {
        // TODO Auto-generated method stub
        return new TypeToken<ChannelBean<ChannelItem>>() {}.getType();
    }
    
    
}
