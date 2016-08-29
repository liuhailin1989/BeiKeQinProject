package com.android.backchina.viewpagerfragment;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.backchina.adapter.ViewPageFragmentAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseViewPagerFragment;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.base.ChannelBean;
import com.android.backchina.fragment.NewsFragment;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.ui.ChannelActivity;
import com.android.backchina.utils.TLog;
import com.android.backchina.widget.PagerSlidingTabStrip.OnPagerChangeLis;
import com.google.gson.reflect.TypeToken;
import com.android.backchina.base.BaseListFragment;

/**
 * 新闻
 */
public class TabNewsFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }
    
    @Override
    protected void setupViews(View root) {
        // TODO Auto-generated method stub
        super.setupViews(root);
        mTabStrip.setOnPagerChange(new OnPagerChangeLis() {
            @Override
            public void onChanged(int page) {
                // TODO Auto-generated method stub
                refreshPage(page);
            }
        });
    }
    
    private void refreshPage(int index) {
        TLog.d("index = " + index);
        try {
            ((BaseListFragment) getChildFragmentManager().getFragments().get(index)).show();
        } catch (Exception e) {
            TLog.e(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
     // nothing
    }

    @Override
    protected void requestData() {
        // TODO Auto-generated method stub
        super.requestData();
        BackChinaApi.getChannelList(mHandler);
    }
    
    @Override
    protected void setListData(ChannelBean<ChannelItem> channelBean) {
        TLog.d("called");
        if(mTabsAdapter != null && channelBean != null){
            List<ChannelItem> datas = channelBean.getItems();
            for(ChannelItem item : datas){
                TLog.d("tab name =" +item.getName());
                mTabsAdapter.addTab(item.getName(), item.getName(), NewsFragment.class, getBundle(item.getId(),item));
            }
//            for(int i = 0; i < 5 ; i ++){
//                mTabsAdapter.addTab(datas.get(i).getName(), datas.get(i).getName(), NewsFragment.class, getBundle(datas.get(i).getId(),datas.get(i)));
//            }
            //
            Fragment fragment = mTabsAdapter.getItem(mViewPager.getCurrentItem());
            if (fragment != null && fragment instanceof BaseListFragment) {
                ((BaseListFragment) fragment).onTabReselect();
            }
        } 
    }
    

    /**
     * 基类会根据不同的catalog展示相应的数据
     *
     * @param catalog 要显示的数据类别
     * @return
     */
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