package com.android.backchina.fragment;

import java.lang.reflect.Type;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.R;
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
import com.android.backchina.interf.SubscribeCatCallback;
import com.android.backchina.manager.SubscribeManager;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.ToastUtils;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SubscribeCatContentFragment extends BaseListFragment<Subscribe> implements ISubscribeListener,SubscribeCatCallback{

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
	protected boolean isNeedSearchBar() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().getBackChinaMode().setSubscribeCatCallBack(this);
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
		List<Subscribe> resultData = resultListBean.getItems();
		for(Subscribe subscribe : resultData){
			Subscribe querrySubscribe = null;
			if(AppContext.getInstance().isLogin()){
				querrySubscribe = SubscribeManager.getInstance().getSubscribeFromTabOnlineById(getActivity(),"" + subscribe.getId(),SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID);
			} else {
				querrySubscribe = SubscribeManager.getInstance().getSubscribeFromTabLocalById(getActivity(),"" + subscribe.getId(),SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID);
			}
			if(querrySubscribe != null){
				subscribe.setFavid(querrySubscribe.getFavid());
			}
		}
		if (isrefresh) {
			mAdapter.clear();
			mAdapter.addItem(resultData);
			AppOperator.runOnThread(new Runnable() {
				@Override
				public void run() {
					CacheManager.saveObject(getActivity(), resultListBean,
							getCacheKey());
				}
			});
		}else{
			mAdapter.addItem(resultData);
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
	public void onSubscribe(View v, final Subscribe subscribe) {
		// TODO Auto-generated method stub
		if (AppContext.getInstance().isLogin()) {
			if (StringUtils.isEmpty(subscribe.getFavid())) {
				BackChinaApi.subscribe(subscribe.getId(),
						new TextHttpResponseHandler() {

							@Override
							public void onSuccess(int code, Header[] headers,
									String responseString) {
								// TODO Auto-generated method stub
								handleSubscribeResponse(headers, responseString);
							}

							@Override
							public void onFailure(int code, Header[] headers,
									String responseString, Throwable arg3) {
								// TODO Auto-generated method stub
								ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
							}
						});
			}else{
				BackChinaApi.cancelSubscribe(subscribe.getFavid(),
						new TextHttpResponseHandler() {

							@Override
							public void onSuccess(int code,
									Header[] headers, String responseString) {
								// TODO Auto-generated method stub
								handleCancelSubscribeResponse(subscribe,headers,
										responseString);
							}

							@Override
							public void onFailure(int code,
									Header[] headers,
									String responseString, Throwable arg3) {
								// TODO Auto-generated method stub
								ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_failed);
							}
						});
			}
		} else {
			if(StringUtils.isEmpty(subscribe.getFavid())){
				subscribe.setFavid("local_" + subscribe.getId());
				subscribe.setIdtype(SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID);
//				changeSubscribeBtnStatus(v,true);
				SubscribeManager.getInstance().saveSubscribeToTabLocal(
						getActivity(), subscribe);
				ToastUtils.show(getContext(), R.string.toast_subscribe_sucessed);
				UIHelper.notifySubscribeDataChanged(getActivity());
			}else{
//				changeSubscribeBtnStatus(v,false);
				SubscribeManager.getInstance().deleteSubscribeToTabLocal(getActivity(), subscribe);
				ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_sucessed);
				UIHelper.notifySubscribeDataChanged(getActivity());
			}
		}
	}
	
	private void changeSubscribeBtnStatus(View v,boolean isSubscribed){
		if(v instanceof TextView){
			if (isSubscribed) {
				((TextView) v).setText(getActivity().getResources().getString(R.string.btn_subscribe_cancle_text));
			}else{
				((TextView)v).setText(getActivity().getResources().getString(R.string.btn_subscribe_text));
			}
		}
	}
    private void handleSubscribeResponse(Header[] headers,String response) {
    	Type type = new TypeToken<ActivitiesBean<Subscribe>>() {
        }.getType();
        ActivitiesBean<Subscribe> activitiesBean = AppContext.createGson().fromJson(response, type);
        Subscribe subscribe = activitiesBean.getActivities();
		if (subscribe.getStatus() == null) {
			if (subscribe.getFavid() != null) {
				ToastUtils.show(getContext(), R.string.toast_subscribe_sucessed);
				SubscribeManager.getInstance().saveSubscribeToTabOnline(getActivity(), subscribe);
				UIHelper.notifySubscribeDataChanged(getActivity());
			}else{
				ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
			}
		} else {
			if (subscribe.getStatus().contains("repeat")) {
				ToastUtils.show(getContext(), R.string.toast_subscribed);
			} else if (subscribe.getStatus().equals("-1")) {
				ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
			} else if (subscribe.getStatus().equals("-2")) {
				ToastUtils.show(getContext(), R.string.toast_need_login);
			} else {
				ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
			}
		}
    }
    

	private void handleCancelSubscribeResponse(Subscribe subscribe,Header[] headers, String response) {
		Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
		}.getType();
		ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson()
				.fromJson(response, type);
		StatusBean statusBean = activitiesBean.getActivities();
		if (statusBean.getStatus().equals("1")) {
			ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_sucessed);
			SubscribeManager.getInstance().deleteSubscribeTabOnline(getActivity(), ""+subscribe.getId(),SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID);
			UIHelper.notifySubscribeDataChanged(getActivity());
		} else if (statusBean.getStatus().equals("-1")) {
			ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_failed);
		} else if (statusBean.getStatus().equals("-2")) {
			ToastUtils.show(getContext(), R.string.toast_need_login);
		} else {
			ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_failed);
		}
	}

	@Override
	public void OnSubscribeCatDataStatusChanged() {
		// TODO Auto-generated method stub
		if(getActivity() == null){
			return;
		}
		if(mAdapter != null && mAdapter.getCount() > 0){
			List<Subscribe> resultData = mAdapter.getDatas();
			for(Subscribe subscribe : resultData){
				Subscribe querrySubscribe = null;
				if(AppContext.getInstance().isLogin()){
					querrySubscribe = SubscribeManager.getInstance().getSubscribeFromTabOnlineById(getActivity(),"" + subscribe.getId(),SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID);
				} else {
					querrySubscribe = SubscribeManager.getInstance().getSubscribeFromTabLocalById(getActivity(),"" + subscribe.getId(),SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID);
				}
				if(querrySubscribe != null){
					subscribe.setFavid(querrySubscribe.getFavid());
				}else{
					subscribe.setFavid("");
				}
				mAdapter.notifyDataSetChanged();
			}
			return;
		}
	}
}
