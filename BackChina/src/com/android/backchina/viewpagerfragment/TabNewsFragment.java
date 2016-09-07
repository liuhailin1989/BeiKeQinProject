package com.android.backchina.viewpagerfragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.adapter.ViewPageFragmentAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseViewPagerFragment;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.base.ChannelBean;
import com.android.backchina.fragment.NewsFragment;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.manager.ChannelManager;
import com.android.backchina.ui.BaseChannelActivity;
import com.android.backchina.ui.ChannelNewsActivity;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.android.backchina.widget.PagerSlidingTabStrip.OnPagerChangeLis;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.android.backchina.base.BaseListFragment;

import cz.msebera.android.httpclient.Header;

/**
 * 新闻
 */
public class TabNewsFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

	protected TextHttpResponseHandler mHandler;
    

    protected Type getType() {
        // TODO Auto-generated method stub
        return new TypeToken<ChannelBean<ChannelItem>>() {}.getType();
    }
    
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		mHandler = new TextHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				TLog.d("called");
				onRequestError(statusCode);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				// TODO Auto-generated method stub
				try {
					ChannelBean<ChannelItem> channelBean = AppContext
							.createGson().fromJson(responseString, getType());
					if (channelBean != null && channelBean.getItems() != null) {
						//
						setListData(channelBean);
						onRequestSuccess();
					}else{
						onRequestError(statusCode);
					}
				} catch (Exception e) {
					e.printStackTrace();
					onFailure(statusCode, headers, responseString, e);
				}
			}

		};
	}

    @Override
    protected void onRequestData() {
        // TODO Auto-generated method stub
        final List<ChannelItem> localChannelItems = ChannelManager.getInstance().getNewsChannelItemsFromTabLocal(getActivity());
        if (localChannelItems != null && localChannelItems.size() > 0) {
            AppOperator.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                	if(mTabsAdapter != null && mTabsAdapter.getCount() > 0){
//                		onRequestSuccess();
//                		return;
                		mTabsAdapter.removeAll();
                	}
                    for(ChannelItem item : localChannelItems){
                        TLog.d("tab name =" +item.getName());
                        mTabsAdapter.addTab(item.getName(), item.getName(), NewsFragment.class, getBundle(item.getId(),item));
                    }
                    onRequestSuccess();
                    //
                    Fragment fragment = mTabsAdapter.getItem(mViewPager.getCurrentItem());
                    if (fragment != null && fragment instanceof BaseListFragment) {
                        ((BaseListFragment) fragment).onTabReselect();
                    }
                }
            });
        } else { 
            BackChinaApi.getChannelList(mHandler);
        }
    }
    
    protected void setListData(ChannelBean<ChannelItem> channelBean) {
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
    public void onTabReselect() {
    	
    }

    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	if(resultCode == Activity.RESULT_OK){
    		Bundle bundle = data.getExtras();
    		boolean isDataChanged = bundle.getBoolean(BaseChannelActivity.BUNDLE_KEY_DATA_CHANGED);
    		if(isDataChanged){
    		requestData();
    		}else{
    			TLog.d("isDataChanged = " +isDataChanged);
    		}
    	}
    }
    
    @Override
    protected void enterChannelManagerActivity() {
        // TODO Auto-generated method stub
        UIHelper.enterChannelNewsActivity(getActivity(),this);
    }

}