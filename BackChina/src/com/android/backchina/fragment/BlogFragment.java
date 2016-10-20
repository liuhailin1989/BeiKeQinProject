package com.android.backchina.fragment;

import java.lang.reflect.Type;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.R;
import com.android.backchina.adapter.BlogAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseListFragment;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Blog;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class BlogFragment extends BaseListFragment<Blog> implements
		OnTabReselectListener {

	public static final String BUNDLE_KEY_CHANNELITEM = "BUNDLE_KEY_CHANNELITEM";
	public static final String BUNDLE_KEY_CATTITLE = "BUNDLE_KEY_CATTITLE";

	private static final String CACHE_KEY_PREFIX = "blog_list_";

	private ChannelItem currentChannelItem;

	private String catTitle = "";

	private int mCurrentPage = 1;
	private boolean isLoadMoreAction = false;

	protected TextHttpResponseHandler mHandler;

	protected String getCacheKeyPrefix() {
		// TODO Auto-generated method stub
		return CACHE_KEY_PREFIX + catTitle;
	}

	private String getCacheKey() {
		return getCacheKeyPrefix() + "_" + 1;//1表示page=1
	}

	@Override
	protected void initBundle(Bundle bundle) {
		// TODO Auto-generated method stub
		catTitle = bundle.getString(BUNDLE_KEY_CATTITLE);
		currentChannelItem = (ChannelItem) bundle
				.getSerializable(BUNDLE_KEY_CHANNELITEM);
		if (currentChannelItem != null) {
			TLog.d("called = " + currentChannelItem.getName());
		}
	}

	@Override
	protected void onRestartInstance(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onRestartInstance(bundle);
		TLog.d("url = " + currentChannelItem.getUrlapi());
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
					ResultListBean<Blog> resultBean = AppContext.createGson()
							.fromJson(responseString, getType());
					if (resultBean != null && resultBean.getItems() != null) {
						if (mCurrentPage == 1) {
							showDataUpdate(resultBean.getItems());
							setListData(resultBean, true);
						}else{
							setListData(resultBean, false);
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

	private void showDataUpdate(List<Blog> newDatas){
		if(newDatas == null){
			return;
		}
		int updateValue = 0;
		final ResultListBean<Blog> bean = (ResultListBean<Blog>) CacheManager.readObject(getActivity(), getCacheKey());
		if (bean == null) {
			updateValue = newDatas.size();
		} else {
			List<Blog> oldDatas = bean.getItems();
			Blog blog = oldDatas.get(0);
			for(int i = 0; i < newDatas.size(); i++){
				Blog temp = newDatas.get(i);
				if(temp.getId() == blog.getId()){
					updateValue = i;
					break;
				}
			}
		}
		String value = getActivity().getResources().getString(R.string.header_hint_refresh_notify);
		String resultData = String.format(value, updateValue);
		showCrouton(resultData,(ViewGroup)mRoot);
	}
	protected void setListData(final ResultListBean<Blog> resultBean,
			boolean isrefresh) {
		// is refresh
		if (isrefresh) {
			mAdapter.clear();
			mAdapter.addItem(resultBean.getItems());
			AppOperator.runOnThread(new Runnable() {
				@Override
				public void run() {
					CacheManager.saveObject(getActivity(), resultBean,
							getCacheKey());
				}
			});
		} else {
			mAdapter.addItem(resultBean.getItems());
		}
	}

	@Override
	protected void onRequestData() {
		// TODO Auto-generated method stub
		mCurrentPage = 1;
		BackChinaApi.getBlogList(currentChannelItem.getUrlapi(),mCurrentPage, mHandler);
	}
	
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
//		super.onLoadMore();
		mCurrentPage = mCurrentPage+1;
		isLoadMoreAction = true;
		BackChinaApi.getBlogList(currentChannelItem.getUrlapi(),mCurrentPage, mHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Blog item = (Blog) parent.getAdapter().getItem(position);
		 UIHelper.enterBlogDetail(getActivity(), item,false);
	}

	@Override
	protected void onShow() {
		if (currentChannelItem != null) {
			TLog.d("called = " + currentChannelItem.getName());
		}
		AppOperator.runOnThread(new Runnable() {
			@Override
			public void run() {
				TLog.d("CACHE_KEY = " + getCacheKey());
				final ResultListBean<Blog> resultBean = (ResultListBean<Blog>) CacheManager.readObject(getActivity(), getCacheKey());
				// if is the first loading
				AppOperator.runOnMainThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (resultBean == null) {
							onRefresh();
						} else {
							setListData(resultBean, false);
							onRequestSuccess();
						}
					}
				});
			}
		});
	}

	@Override
	public void onTabReselect() {
		// TODO Auto-generated method stub
		// show();
	}

	@Override
	protected BaseListAdapter<Blog> getListAdapter() {
		// TODO Auto-generated method stub
		return new BlogAdapter(this);
	}

	@Override
	protected Type getType() {
		// TODO Auto-generated method stub
		return new TypeToken<ResultListBean<Blog>>() {
		}.getType();
	}

}
