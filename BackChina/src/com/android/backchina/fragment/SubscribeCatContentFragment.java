package com.android.backchina.fragment;

import java.lang.reflect.Type;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.adapter.SubscribeAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseListFragment;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.News;
import com.android.backchina.bean.Subscribe;
import com.android.backchina.bean.SubscribeCat;
import com.android.backchina.bean.SubscribeDetail;
import com.android.backchina.bean.base.NewsListBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SubscribeCatContentFragment extends BaseListFragment<Subscribe> {

	private static final String CACHE_KEY_PREFIX = "subscribe_list_";
	
	public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

	public static final String BUNDLE_KEY_SUBSCRIBECATITEM = "BUNDLE_KEY_SUBSCRIBECATITEM";
	
	protected TextHttpResponseHandler mHandler;

	private SubscribeCat mCurrentSubscribeCat;
	
	private int mCatlog = 0;
	
	private int mCurrentPage = 0;
	
	public static SubscribeCatContentFragment newInstance(int catlog,SubscribeCat subscribeCat) {
		SubscribeCatContentFragment fragment = new SubscribeCatContentFragment();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_KEY_CATALOG, catlog);
		args.putSerializable(BUNDLE_KEY_SUBSCRIBECATITEM, subscribeCat);
		fragment.setArguments(args);
		return fragment;
	}
	
	protected String getCacheKeyPrefix() {
		// TODO Auto-generated method stub
		return CACHE_KEY_PREFIX + mCatlog;
	}

	private String getCacheKey() {
		return getCacheKeyPrefix() + "_" + mCurrentPage;
	}
	
	@Override
	protected void initBundle(Bundle bundle) {
		// TODO Auto-generated method stub
		mCatlog = bundle.getInt(BUNDLE_KEY_CATALOG);
		mCurrentSubscribeCat = (SubscribeCat) bundle.getSerializable(BUNDLE_KEY_SUBSCRIBECATITEM);
	}
	
	@Override
	public void onTabReselect() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		super.initData();
		mHandler = new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				TLog.d("called");
				onRequestError(statusCode);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				TLog.d("called");
				try {
					ResultListBean<Subscribe> resultListBean = AppContext
							.createGson().fromJson(responseString, getType());
					if (resultListBean != null
							&& resultListBean.getItems() != null) {
						setListData(resultListBean,true);
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

	protected void setListData(final ResultListBean<Subscribe> resultListBean, boolean isrefresh) {
		if (isrefresh) {
			mAdapter.clear();
			mAdapter.addItem(resultListBean.getItems());
			AppOperator.runOnThread(new Runnable() {
				@Override
				public void run() {
					CacheManager.saveObject(getActivity(), resultListBean,
							getCacheKey());
				}
			});
		}else{
			mAdapter.addItem(resultListBean.getItems());
		}
	}

	@Override
	protected void onShow() {
		AppOperator.runOnThread(new Runnable() {
			@Override
			public void run() {
				TLog.d("CACHE_KEY = " + getCacheKey());
				final ResultListBean<Subscribe> resultListBean= (ResultListBean<Subscribe>) CacheManager.readObject(getActivity(), getCacheKey());
				// if is the first loading
				AppOperator.runOnMainThread(new Runnable() {

					@Override
					public void run() {
						if(mAdapter != null && mAdapter.getCount() > 0){
							return;
						}
						// TODO Auto-generated method stub
						if (resultListBean == null) {
							onRefresh();
						} else {
							setListData(resultListBean, false);
							onRequestSuccess();
						}
					}
				});
			}
		});
	}

	@Override
	protected void onRequestData() {
		BackChinaApi.getSubscribeList(mCurrentSubscribeCat.getUrlapi(), mHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Subscribe subscribe = (Subscribe) parent.getAdapter().getItem(position);
		UIHelper.enterSubscribeDetailActivity(getActivity(), subscribe);
	}

	@Override
	protected BaseListAdapter<Subscribe> getListAdapter() {
		// TODO Auto-generated method stub
		return new SubscribeAdapter(this);
	}

	@Override
	protected Type getType() {
		// TODO Auto-generated method stub
		return new TypeToken<ResultListBean<Subscribe>>() {
		}.getType();
	}

}
