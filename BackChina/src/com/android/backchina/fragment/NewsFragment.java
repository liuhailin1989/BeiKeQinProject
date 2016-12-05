package com.android.backchina.fragment;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.R.color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.R;
import com.android.backchina.adapter.NewsAdapter;
import com.android.backchina.api.ApiHttpClient;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseListFragment;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.FavoriteBean;
import com.android.backchina.bean.News;
import com.android.backchina.bean.base.NewsListBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class NewsFragment extends BaseListFragment<News> {

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
	protected boolean isNeedSearchBar() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		mCurrentPage = 1;
		isLoadMoreAction = false;
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
						//
						for(News news : resultBean.getResult().getItems()){
							if(StringUtils.isEmpty(news.getUrlapi()) && news.getUrlapi().contains(ApiHttpClient.SHOPPING_HOST)){
								news.setNewsType(News.TYPE_NEWS_SHOPPING);
							} else {
								news.setNewsType(News.TYPE_NEWS_NORMAL);
							}
						}
						//
						if (mCurrentPage == 1) {
//							showDataUpdate(resultBean.getResult().getItems());
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
	
	private void showDataUpdate(List<News> newDatas){
		if(newDatas == null){
			return;
		}
		int updateValue = 0;
		final NewsListBean<News> bean = (NewsListBean<News>) CacheManager.readObject(getActivity(), getCacheKey());
		if (bean == null) {
			updateValue = newDatas.size();
		} else {
			List<News> oldDatas = bean.getItems();
			News news = oldDatas.get(0);
			for(int i = 0; i < newDatas.size(); i++){
				News temp = newDatas.get(i);
				if(temp.getId() == news.getId()){
					updateValue = i;
					break;
				}
//				if(!isInOldData(temp, oldDatas)){
//					updateValue++;
//				}
			}
		}
		String value = getActivity().getResources().getString(R.string.header_hint_refresh_notify);
		String resultData = String.format(value, updateValue);
		showCrouton(resultData,(ViewGroup)mRoot);
	}
	
//	private boolean isInOldData(News news,List<News> oldDatas){
//		for(News oldNews : oldDatas){
//			if(news.getId() == oldNews.getId()){
//				return true;
//			}
//		}
//		return false;
//	}

	protected void setListData(final NewsListBean<News> pageBean,
			boolean isrefresh) {
		// is refresh
		List<News> newsData = pageBean.getItems();
		Collections.sort(newsData,new NewsBeanComparator());
		if (isrefresh) {
			mAdapter.clear();
			mAdapter.addItem(newsData);
			showDataUpdate(newsData);
			AppOperator.runOnThread(new Runnable() {
				@Override
				public void run() {
					CacheManager.saveObject(getActivity(), pageBean,
							getCacheKey());
				}
			});
		} else {
			mAdapter.addItem(newsData);
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
							onRefresh();
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
		BackChinaApi.getNewsList(currentChannelItem.getUrlapi(),mCurrentPage, mHandler);
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
		if(view != null && view.getId() == R.id.search_container){
			enterSearch();
			return;
		}
		Object obj = parent.getAdapter().getItem(position);
		//
		if (obj instanceof News) {
			News item = (News) obj;
			if(StringUtils.isEmpty(item.getUrlapi())){
				UIHelper.showUrlRedirect(getActivity(), item.getUrl());
			} else {
				UIHelper.enterNewsDetail(getActivity(), item, false);
			}
			TextView title = (TextView) view.findViewById(R.id.tv_title);
			updateTextColor(title, null);
			saveToReadedList(NewsFragment.HISTORY_NEWS, item.getId() + "");
		}
	}

	@Override
	public void onTabReselect() {
		// TODO Auto-generated method stub
//		 show();
		Log.d("liuhailin", "");
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

	public void autoRefreshIfNecessary(){
		if (isNeedToAutoRefresh(getCacheKeyPrefix())) {
			autoRefresh();
			saveRefreshTime(getCacheKeyPrefix());
		}
	}
	
	public class NewsBeanComparator implements Comparator<News>{

		@Override
		public int compare(News lhs, News rhs) {
			// TODO Auto-generated method stub
			try {
				if(lhs.getDateline() > rhs.getDateline()){
					return -1;
				}else if(lhs.getDateline() < rhs.getDateline()){
					return 1;
				}else{
					return 0;
				}
			}catch(Exception ex){
				return -1;
			}
		}
		
	}
}
