package com.android.backchina.fragment;

import java.lang.reflect.Type;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.adapter.NewsAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseListFragment;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.News;
import com.android.backchina.bean.base.NewsListBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class NewsHistoryFragment extends BaseListFragment<News> {


	public static final String HISTORY_NEWS = "history_news";

	public static final String NEWS_SYSTEM_TIME = "news_system_time";

	private static final String CACHE_KEY_PREFIX = "newslist_";

	public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

	public static final String BUNDLE_KEY_CHANNELITEM = "BUNDLE_KEY_CHANNELITEM";

	private int mCatlog = 0;

	private int mCurrentPage = 1;

	private Handler handler = new Handler();

	private ChannelItem currentChannelItem;

	private boolean isLoadMoreAction = false;

	protected String getCacheKeyPrefix() {
		// TODO Auto-generated method stub
		return CACHE_KEY_PREFIX + mCatlog;
	}

	private String getCacheKey() {
		return getCacheKeyPrefix() + "_" + 1;//1表示page=1
	}

	@Override
	protected void initBundle(Bundle bundle) {
		// TODO Auto-generated method stub
		mCatlog = bundle.getInt(BUNDLE_KEY_CATALOG);
		currentChannelItem = (ChannelItem) bundle
				.getSerializable(BUNDLE_KEY_CHANNELITEM);
		TLog.d("called = " + currentChannelItem.getName());
	}

	@Override
	protected void onRestartInstance(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onRestartInstance(bundle);
		TLog.d("url = " + currentChannelItem.getUrlapi());
	}

	@Override
	protected void setupViews(View root) {
		// TODO Auto-generated method stub
		super.setupViews(root);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		mCurrentPage = 1;
		isLoadMoreAction = false;
	}

	protected void setListData(final NewsListBean<News> pageBean,
			boolean isrefresh) {
		// is refresh
		if (isrefresh) {
			mAdapter.clear();
			mAdapter.addItem(pageBean.getItems());
			AppOperator.runOnThread(new Runnable() {
				@Override
				public void run() {
					CacheManager.saveObject(getActivity(), pageBean,
							getCacheKey());
				}
			});
		} else {
			mAdapter.addItem(pageBean.getItems());
		}
	}

	@Override
	protected void onShow() {
		// TODO Auto-generated method stub
		TLog.d("called");
		AppOperator.runOnThread(new Runnable() {
			@Override
			public void run() {
				TLog.d("CACHE_KEY = " + getCacheKey());
				final NewsListBean<News> bean = (NewsListBean<News>) CacheManager.readObject(getActivity(), getCacheKey());
				// if is the first loading
				AppOperator.runOnMainThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (bean == null) {
							onRequestError(EmptyLayout.NODATA);
						} else {
							setListData(bean, false);
							onRequestSuccess();
						}
					}
				});
			}
		});
	}

	@Override
	protected void onRequestData() {
		// TODO Auto-generated method stub
		mCurrentPage = 1;
	}
	
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mCurrentPage = mCurrentPage+1;
		isLoadMoreAction = true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		News item = (News) parent.getAdapter().getItem(position);
		UIHelper.enterNewsDetail(getActivity(), item,false);
	}

	@Override
	public void onTabReselect() {
		// TODO Auto-generated method stub
	}

	@Override
	protected BaseListAdapter<News> getListAdapter() {
		// TODO Auto-generated method stub
		return new NewsAdapter(this);
	}

	@Override
	protected Type getType() {
		// TODO Auto-generated method stub
		return new TypeToken<ResultBean<NewsListBean<News>>>() {
		}.getType();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}


}
