package com.android.backchina.viewpagerfragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseViewPagerFragment;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.base.ChannelBean;
import com.android.backchina.fragment.BlogFragment;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.manager.ChannelManager;
import com.android.backchina.ui.ChannelActivity;
import com.android.backchina.utils.TLog;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class TabBlogFragment extends BaseViewPagerFragment implements
		OnTabReselectListener {

	protected TextHttpResponseHandler mHandler;

	protected Type getType() {
		// TODO Auto-generated method stub
		return new TypeToken<ChannelBean<ChannelItem>>() {
		}.getType();
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
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
		final List<ChannelItem> localChannelItems = ChannelManager
				.getInstance().getBlogChannelItemsFromTabLocal(getActivity());
		if (localChannelItems != null && localChannelItems.size() > 0) {
			AppOperator.runOnMainThread(new Runnable() {
				@Override
				public void run() {
		        	if(mTabsAdapter != null && mTabsAdapter.getCount() > 0){
		        		onRequestSuccess();
		        		return;
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
			for (int i = 0; i < 5; i++) {
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
	protected void enterChannelManagerActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(getActivity(), ChannelActivity.class);
		getActivity().startActivity(intent);
	}

}
