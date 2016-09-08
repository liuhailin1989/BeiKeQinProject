package com.android.backchina.fragment;

import java.lang.reflect.Type;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.R;
import com.android.backchina.adapter.NewsAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseListFragment;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.City;
import com.android.backchina.bean.News;
import com.android.backchina.bean.base.NewsListBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.ui.CityListActivity;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class NewsLocalFragment  extends BaseListFragment<News> {

	public static final String HISTORY_NEWS = "history_news";

	public static final String NEWS_SYSTEM_TIME = "news_system_time";

	private static final String CACHE_KEY_PREFIX = "newslist_";

	public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

	public static final String BUNDLE_KEY_CHANNELITEM = "BUNDLE_KEY_CHANNELITEM";

	private int mCatlog = 0;

	private int mCurrentPage = 1;

	private Handler handler = new Handler();

	private ChannelItem currentChannelItem;

	protected TextHttpResponseHandler mHandler;
	
	private Button mBtnSelectCity;
	
	private TextView mTvCityName;
	
	private City mCurrentCity;
	
	private boolean isLoadMoreAction = false;

	protected String getCacheKeyPrefix() {
		// TODO Auto-generated method stub
		return CACHE_KEY_PREFIX + mCatlog;
	}

	private String getCacheKey() {
		return getCacheKeyPrefix() + "_" + 1;//1表示page=1,只缓存一页数据
	}
	
	private String getCurrentCityCacheKey() {
		return getCacheKeyPrefix() + "_current_city";
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
		View headerView = root.inflate(getActivity(), R.layout.layout_news_local_header_view, null);
		mBtnSelectCity = (Button) headerView.findViewById(R.id.btn_select_city);
		mBtnSelectCity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			UIHelper.enterCityListActivity(getActivity(),getParentFragment());
			}
		});
		mTvCityName = (TextView) headerView.findViewById(R.id.tv_city_name);
		mListView.addHeaderView(headerView);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		mCurrentPage = 1;
		isLoadMoreAction = false;
		mCurrentCity =  (City) CacheManager.readObject(getActivity(), getCurrentCityCacheKey());
		if(mCurrentCity != null){
			mTvCityName.setText(mCurrentCity.getTitle());
		}else{
			mTvCityName.setText("无");
		}
		mHandler = new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				TLog.d("called");
				if (isLoadMoreAction) {
					loadMoreNodata();
				} else {
					onRequestError(EmptyLayout.NODATA);
				}
				isLoadMoreAction = false;
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				TLog.d("called");
				try {
					ResultBean<NewsListBean<News>> resultBean = AppContext
							.createGson().fromJson(responseString, getType());
					if (resultBean != null
							&& resultBean.getResult().getItems() != null) {
						if (mCurrentPage == 1) {
							setListData(resultBean.getResult(), true);
						}else{
							setListData(resultBean.getResult(), false);
						}
						onRequestSuccess();
						stopLoadMore();
					} else {
						if(isLoadMoreAction){
							loadMoreNodata();
						}else{
						onRequestError(EmptyLayout.NODATA);
						}
					}
					isLoadMoreAction = false;
				} catch (Exception e) {
					e.printStackTrace();
					onFailure(statusCode, headers, responseString, e);
				}
			}
		};
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
						if (bean == null || mCurrentCity == null) {
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
		mCurrentPage = 1;
		// TODO Auto-generated method stub
		//http://www.21uscity.com/zone/160/?appxml=1&json=1
		BackChinaApi.getNewsList(mCurrentCity.getUrlapi(),1, mHandler);
//		BackChinaApi.getNewsList("http://www.21uscity.com/zone/160/?appxml=1&json=1", mHandler);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
//		super.onLoadMore();
		mCurrentPage = mCurrentPage+1;
		isLoadMoreAction = true;
		BackChinaApi.getNewsList(currentChannelItem.getUrlapi(),mCurrentPage, mHandler);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		News item = (News) parent.getAdapter().getItem(position);
		UIHelper.enterNewsDetail(getActivity(), item);
	}

	@Override
	public void onTabReselect() {
		// TODO Auto-generated method stub
		// show();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode == Activity.RESULT_OK){
			Bundle bundle = data.getExtras();
			City city = (City) bundle.getSerializable(CityListActivity.BUNDLE_KEY_SELECT_CITY);
			if(city != null){
				mCurrentCity = city;
				if (mTvCityName != null) {
					mTvCityName.setText(mCurrentCity.getTitle());
				}
				mAdapter.clear();
				AppOperator.runOnThread(new Runnable() {
					@Override
					public void run() {
						CacheManager.saveObject(getActivity(), mCurrentCity,
								getCurrentCityCacheKey());
					}
				});
				onRefresh();
			}
		}
	}
}
