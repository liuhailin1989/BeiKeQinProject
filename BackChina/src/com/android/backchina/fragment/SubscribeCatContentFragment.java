package com.android.backchina.fragment;

import java.lang.reflect.Type;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.adapter.SubscribeAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseListFragment;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.StatusBean;
import com.android.backchina.bean.Subscribe;
import com.android.backchina.bean.SubscribeCat;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.interf.ISubscribeListener;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SubscribeCatContentFragment extends BaseListFragment<Subscribe> implements ISubscribeListener{

	private static final String CACHE_KEY_PREFIX = "subscribe_list_";
	
	public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";

	public static final String BUNDLE_KEY_SUBSCRIBECATITEM = "BUNDLE_KEY_SUBSCRIBECATITEM";
	
	protected TextHttpResponseHandler mHandler;

	private SubscribeCat mCurrentSubscribeCat;
	
	private int mCatlog = 0;
	
	private int mCurrentPage = 1;
	
	private boolean isLoadMoreAction = false;
	
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
		return getCacheKeyPrefix() + "_" + 1;//1表示page=1
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
		mCurrentPage = 1;
		isLoadMoreAction = false;
		((SubscribeAdapter)mAdapter).setSubscribeListener(this);
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
					ResultListBean<Subscribe> resultListBean = AppContext
							.createGson().fromJson(responseString, getType());
					if (resultListBean != null
							&& resultListBean.getItems() != null) {
						if (mCurrentPage == 1) {
							setListData(resultListBean, true);
						}else{
							setListData(resultListBean, false);
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
		mCurrentPage = 1;
		BackChinaApi.getSubscribeList(mCurrentSubscribeCat.getUrlapi(),mCurrentPage, mHandler);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
//		super.onLoadMore();
		mCurrentPage = mCurrentPage+1;
		isLoadMoreAction = true;
		BackChinaApi.getSubscribeList(mCurrentSubscribeCat.getUrlapi(),mCurrentPage, mHandler);
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

	@Override
	public void onSubscribe(Subscribe subscribe) {
		// TODO Auto-generated method stub
		BackChinaApi.subscribe(subscribe.getId(), new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int code, Header[] headers, String responseString) {
				// TODO Auto-generated method stub
				handleSubscribeResponse(headers,responseString);
			}
			
			@Override
			public void onFailure(int code, Header[] headers, String responseString, Throwable arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getContext(), "订阅失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
    private void handleSubscribeResponse(Header[] headers,String response) {
    	Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
        }.getType();
        ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson().fromJson(response, type);
        StatusBean statusBean = activitiesBean.getActivities();
        if (statusBean.getStatus() == 1) {
        	Toast.makeText(getContext(), "订阅成功", Toast.LENGTH_SHORT).show();
        	UIHelper.notifySubscribeDataChanged(getActivity());
        }else if (statusBean.getStatus() == -1) {
        	Toast.makeText(getContext(), "订阅失败", Toast.LENGTH_SHORT).show();
        }else if (statusBean.getStatus() == -2) {
        	Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
        }else{
        	Toast.makeText(getContext(), "订阅失败", Toast.LENGTH_SHORT).show();
        }
    }
}
