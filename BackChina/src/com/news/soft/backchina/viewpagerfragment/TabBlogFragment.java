package com.news.soft.backchina.viewpagerfragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.news.soft.backchina.AppConfig;
import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.AppOperator;
import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.base.BaseViewPagerFragment;
import com.news.soft.backchina.bean.ChannelItem;
import com.news.soft.backchina.bean.base.ChannelBean;
import com.news.soft.backchina.fragment.BlogFragment;
import com.news.soft.backchina.interf.OnTabReselectListener;
import com.news.soft.backchina.manager.ChannelManager;
import com.news.soft.backchina.ui.BaseChannelActivity;
import com.news.soft.backchina.utils.TLog;
import com.news.soft.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class TabBlogFragment extends BaseViewPagerFragment implements
		OnTabReselectListener {

	protected TextHttpResponseHandler mHandler;

	private boolean isChannelDataChanged = false;

	protected Type getType() {
		// TODO Auto-generated method stub
		return new TypeToken<ChannelBean<ChannelItem>>() {
		}.getType();
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
					} else {
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
		final List<ChannelItem> localChannelItems = ChannelManager
				.getInstance().getBlogChannelItemsFromTabLocal(getActivity());
		if (localChannelItems != null && localChannelItems.size() > 0) {
			AppOperator.runOnMainThread(new Runnable() {
				@Override
				public void run() {
					if (mTabsAdapter != null && mTabsAdapter.getCount() > 0) {
						if (isChannelDataChanged) {
							mTabsAdapter.removeAll();
						} else {
							onRequestSuccess();
							return;
						}

					}
					// TODO Auto-generated method stub
					for (ChannelItem item : localChannelItems) {
						mTabsAdapter.addTab(item.getName(), item.getName(),
								BlogFragment.class,
								getBundle(item.getName(), item));
					}
					onRequestSuccess();
				}
			});
		} else {
			BackChinaApi.getBlogChannelList(mHandler);
		}
	}

	protected void setListData(ChannelBean<ChannelItem> channelBean) {
		// TODO Auto-generated method stub
		TLog.d("called");
		if (mTabsAdapter != null && channelBean != null) {
			List<ChannelItem> datas = channelBean.getItems();
			ChannelManager.getInstance().saveBlogChannelItemToTabAll(
					getActivity(), datas);
			//
			List<ChannelItem> defaultLocalChannelItems = new ArrayList<ChannelItem>();
			for (int i = 0; i < AppConfig.SHOW_TAB_ITEM_NUM; i++) {
				if (null != datas.get(i)) {
					defaultLocalChannelItems.add(datas.get(i));
				}
			}
			ChannelManager.getInstance().saveBlogChannelItemToTabLocal(
					getActivity(), defaultLocalChannelItems);
			//
			for (ChannelItem item : defaultLocalChannelItems) {
				TLog.d("tab name =" + item.getName());
				mTabsAdapter.addTab(item.getName(), item.getName(),
						BlogFragment.class, getBundle(item.getName(), item));
			}
		}
	}

	private Bundle getBundle(String catatitle, ChannelItem item) {
		Bundle bundle = new Bundle();
		bundle.putString(BlogFragment.BUNDLE_KEY_CATTITLE, catatitle);
		bundle.putSerializable(BlogFragment.BUNDLE_KEY_CHANNELITEM, item);
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
			if (fragment != null && fragment instanceof BlogFragment) {
				if (currentItemIndex == index) {
					((BlogFragment) fragment).autoRefreshIfNecessary();
				}
			}
		}
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == BaseChannelActivity.RESULT_CODE_OK) {
			Bundle bundle = data.getExtras();
			isChannelDataChanged = bundle
					.getBoolean(BaseChannelActivity.BUNDLE_KEY_DATA_CHANGED);
			if (isChannelDataChanged) {
				requestData();
			} else {
				TLog.d("isChannelDataChanged = " + isChannelDataChanged);
			}
		}
	}

	@Override
	protected void enterChannelManagerActivity() {
		// TODO Auto-generated method stub
		UIHelper.enterChannelBlogActivity(getActivity(), this);
	}

}
