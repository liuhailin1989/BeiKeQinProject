package com.news.soft.backchina.viewpagerfragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.View;

import com.news.soft.backchina.AppConfig;
import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.AppOperator;
import com.news.soft.backchina.adapter.ViewPageFragmentAdapter;
import com.news.soft.backchina.api.ApiHttpClient;
import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.base.BaseViewPagerFragment;
import com.news.soft.backchina.bean.ChannelItem;
import com.news.soft.backchina.bean.base.ChannelBean;
import com.news.soft.backchina.fragment.NewsFragment;
import com.news.soft.backchina.fragment.NewsLocalFragment;
import com.news.soft.backchina.interf.OnTabReselectListener;
import com.news.soft.backchina.manager.ChannelManager;
import com.news.soft.backchina.ui.BaseChannelActivity;
import com.news.soft.backchina.ui.ChannelNewsActivity;
import com.news.soft.backchina.utils.StringUtils;
import com.news.soft.backchina.utils.TLog;
import com.news.soft.backchina.utils.UIHelper;
import com.news.soft.backchina.widget.PagerSlidingTabStrip.OnPagerChangeLis;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.news.soft.backchina.base.BaseListFragment;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * 新闻
 */
public class TabNewsFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

	protected TextHttpResponseHandler mHandler;
	
	private boolean isChannelDataChanged = false;
    

    protected Type getType() {
        // TODO Auto-generated method stub
        return new TypeToken<ChannelBean<ChannelItem>>() {}.getType();
    }
    
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		isChannelDataChanged = false;
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
	
	/**
	 * 判断是否为"本地"
	 * @param item
	 * @return
	 */
	private boolean isLocal(ChannelItem item){
		if(item != null && item.getUrl().endsWith("http://www.backchina.com/special/local/")){
			return true;
		}else{
			return false;
		}
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
                		if(isChannelDataChanged){
                			mTabsAdapter.removeAll();
                			isChannelDataChanged = false;
						} else {
							onRequestSuccess();
							return;
						}
                	}
                    for(ChannelItem item : localChannelItems){
                        TLog.d("tab name =" +item.getName());
                        if(isLocal(item)){
                        	mTabsAdapter.addTab(item.getName(), item.getName(), NewsLocalFragment.class, getBundle(item.getId(),item));
                        }else{
                            mTabsAdapter.addTab(item.getName(), item.getName(), NewsFragment.class, getBundle(item.getId(),item));
                        }
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
            BackChinaApi.getNewsChannelList(mHandler);
        }
    }
    
    protected void setListData(ChannelBean<ChannelItem> channelBean) {
        TLog.d("called");
        if(mTabsAdapter != null && channelBean != null){
            List<ChannelItem> datas = channelBean.getItems();
            ChannelManager.getInstance().saveNewsChannelItemToTabAll(getActivity(), datas);
            //
            List<ChannelItem> defaultLocalChannelItems = new ArrayList<ChannelItem>();
            for (int i = 0; i < AppConfig.SHOW_TAB_ITEM_NUM; i++) {
                if (null != datas.get(i)) {
                    defaultLocalChannelItems.add(datas.get(i));
                }
            }
            ChannelManager.getInstance().saveNewsChannelItemToTabLocal(getActivity(), defaultLocalChannelItems);
            //
            for(ChannelItem item : defaultLocalChannelItems){
                TLog.d("tab name =" +item.getName());
                if(isLocal(item)){
                	mTabsAdapter.addTab(item.getName(), item.getName(), NewsLocalFragment.class, getBundle(item.getId(),item));
                }else{
                    mTabsAdapter.addTab(item.getName(), item.getName(), NewsFragment.class, getBundle(item.getId(),item));
                }
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
    public void onClickTab(View tab, int index) {
    	// TODO Auto-generated method stub
		if (mTabsAdapter != null && mViewPager != null) {
			Fragment fragment = mTabsAdapter.getItem(index);
			int currentItemIndex = mViewPager.getCurrentItem();
			if (fragment != null && fragment instanceof NewsFragment) {
				if (currentItemIndex == index) {
					((NewsFragment) fragment).autoRefreshIfNecessary();
				}
			}
		}
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	if(resultCode == BaseChannelActivity.RESULT_CODE_OK){
    		Bundle bundle = data.getExtras();
    		isChannelDataChanged = bundle.getBoolean(BaseChannelActivity.BUNDLE_KEY_DATA_CHANGED);
    		if(isChannelDataChanged){
    		requestData();
    		}else{
    			TLog.d("isDataChanged = " +isChannelDataChanged);
    		}
    	}
    }
    
    @Override
    protected void enterChannelManagerActivity() {
        // TODO Auto-generated method stub
        UIHelper.enterChannelNewsActivity(getActivity(),this);
    }

}