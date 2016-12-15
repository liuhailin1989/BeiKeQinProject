package com.news.soft.backchina.ui;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.AppOperator;
import com.news.soft.backchina.BackChinaMode;
import com.news.soft.backchina.R;
import com.news.soft.backchina.adapter.SubscribeAdapter;
import com.news.soft.backchina.adapter.SubscribeDetailAdapter;
import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.base.BaseActivity;
import com.news.soft.backchina.bean.Blog;
import com.news.soft.backchina.bean.StatusBean;
import com.news.soft.backchina.bean.Subscribe;
import com.news.soft.backchina.bean.SubscribeDetail;
import com.news.soft.backchina.bean.base.ActivitiesBean;
import com.news.soft.backchina.bean.base.ResultListBean;
import com.news.soft.backchina.cache.CacheManager;
import com.news.soft.backchina.fragment.TabSubscribeFragment;
import com.news.soft.backchina.interf.ISpecialNewsSubscribeListener;
import com.news.soft.backchina.manager.SubscribeManager;
import com.news.soft.backchina.ui.empty.EmptyLayout;
import com.news.soft.backchina.utils.StringUtils;
import com.news.soft.backchina.utils.ToastUtils;
import com.news.soft.backchina.utils.UIHelper;
import com.news.soft.backchina.widget.CircleImageView;
import com.news.soft.backchina.widget.XListView;
import com.news.soft.backchina.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SubscribeDetailActivity extends BaseActivity implements OnItemClickListener,IXListViewListener,ISpecialNewsSubscribeListener{

	public final static String BUNDLE_KEY_SUBSCRIBE = "BUNDLE_KEY_SUBSCRIBE";
	
	private ImageView mBtnBack;
	
	private CircleImageView mLogo;

	private TextView mName;

	private TextView mBtnSubscribe;

	private XListView mListView;

	private EmptyLayout mEmptyLayout;
	
	private Subscribe mCurrentSubscribe;

	protected TextHttpResponseHandler mHandler;

	private SubscribeDetailAdapter mAdapter;

	private Context mContext;
	
	private int mCurrentPage = 1;
	
	private boolean isLoadMoreAction = false;
	
	 private boolean isSubscribed;

	public static void show(Context context, Subscribe subscribe) {
		Intent intent = new Intent(context, SubscribeDetailActivity.class);
		intent.putExtra(BUNDLE_KEY_SUBSCRIBE, subscribe);
		context.startActivity(intent); 
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initBundle(getIntent().getExtras());
		setContentView(R.layout.activity_subscribe_detail_layout);
		mContext = this;
		setupViews();
		initData();
	}

	protected void initBundle(Bundle bundle) {
		mCurrentSubscribe = (Subscribe) bundle
				.getSerializable(BUNDLE_KEY_SUBSCRIBE);
		Subscribe subscribe = null;
		 if (AppContext.getInstance().isLogin()) {
			 subscribe = SubscribeManager.getInstance().getSubscribeFromTabOnlineById(this, ""+mCurrentSubscribe.getId(),mCurrentSubscribe.getIdtype());
		 }else{
			 subscribe = SubscribeManager.getInstance().getSubscribeFromTabLocalById(this, ""+mCurrentSubscribe.getId(),mCurrentSubscribe.getIdtype());
		 }
		 isSubscribed = (subscribe != null) ? true : false;
//		 if(isSubscribed){
//			 mCurrentSubscribe.setFavid(subscribe.getFavid());
//		 }
	}

	private void setupViews() {
		mBtnBack = (ImageView) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mLogo = (CircleImageView) findViewById(R.id.iv_logo);
		mName = (TextView) findViewById(R.id.tv_name);
		mBtnSubscribe = (TextView) findViewById(R.id.btn_subscribe);
		checkSubscribeBtnStatus(mCurrentSubscribe);
		mBtnSubscribe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isSubscribed){
					cancleSubscribeFunction();
				} else {
					subscribeFunction();
				}
			}
		});
		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		
		mEmptyLayout = (EmptyLayout) findViewById(R.id.error_layout);
	}

	private void checkSubscribeBtnStatus(Subscribe subscribe) {
		if (subscribe == null || StringUtils.isEmpty(subscribe.getFavid())) {
			mBtnSubscribe.setText(mContext.getResources().getString(R.string.btn_subscribe_text));
			isSubscribed = false;
		} else {
			mBtnSubscribe.setText(mContext.getResources().getString(R.string.btn_subscribe_cancle_text));
			isSubscribed = true;
		}
	}

	private void subscribeFunction() {
		if (SubscribeManager.getInstance().isSearchIdType(mCurrentSubscribe)) {
			if (AppContext.getInstance().isLogin()) {
				BackChinaApi.subscribe(mCurrentSubscribe.getId(),
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
			} else {
				mCurrentSubscribe
						.setFavid("local_" + mCurrentSubscribe.getId());
				mCurrentSubscribe
						.setIdtype(SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID);
				checkSubscribeBtnStatus(mCurrentSubscribe);
				SubscribeManager.getInstance().saveSubscribeToTabLocal(
						mContext, mCurrentSubscribe);
				ToastUtils.show(getContext(), R.string.toast_subscribe_sucessed);
				UIHelper.notifySubscribeDataChanged(mContext);
			}
		}else if (SubscribeManager.getInstance().isUIdType(mCurrentSubscribe)) {
			BackChinaApi.subscribeBlog(""+mCurrentSubscribe.getId(),
					new TextHttpResponseHandler() {

						@Override
						public void onSuccess(int code, Header[] headers,
								String response) {
							// TODO Auto-generated method stub
							handleSubscribeResponse(headers, response);
						}

						@Override
						public void onFailure(int code, Header[] headers,
								String response, Throwable throwable) {
							// TODO Auto-generated method stub
							ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
						}
					});
		}
	}
	
	private void cancleSubscribeFunction(){
		if (SubscribeManager.getInstance().isSearchIdType(mCurrentSubscribe)) {
			if (AppContext.getInstance().isLogin()) {
				BackChinaApi.cancelSubscribe(mCurrentSubscribe.getFavid(),
						new TextHttpResponseHandler() {

							@Override
							public void onSuccess(int code, Header[] headers,
									String responseString) {
								// TODO Auto-generated method stub
								handleCancelSubscribeResponse(
										mCurrentSubscribe, headers,
										responseString);
							}

							@Override
							public void onFailure(int code, Header[] headers,
									String responseString, Throwable arg3) {
								// TODO Auto-generated method stub
								ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_failed);
							}
						});
			} else {
				SubscribeManager.getInstance().deleteSubscribeToTabLocal(this,
						mCurrentSubscribe);
				checkSubscribeBtnStatus(null);
				ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_sucessed);
				UIHelper.notifySubscribeDataChanged(this);
			}
		}else if (SubscribeManager.getInstance().isUIdType(mCurrentSubscribe)) {
			if (AppContext.getInstance().isLogin()) {
				BackChinaApi.cancleSubscribeBlog(mCurrentSubscribe.getId(),
						new TextHttpResponseHandler() {

							@Override
							public void onSuccess(int code, Header[] headers,
									String responseString) {
								// TODO Auto-generated method stub
								handleCancelSubscribeResponse(
										mCurrentSubscribe, headers,
										responseString);
							}

							@Override
							public void onFailure(int code, Header[] headers,
									String responseString, Throwable arg3) {
								// TODO Auto-generated method stub
								ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_failed);
							}
						});
			}else{
				//
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
				checkSubscribeBtnStatus(subscribe);
				SubscribeManager.getInstance().saveSubscribeToTabOnline(this, subscribe);
				UIHelper.notifySubscribeDataChanged(this);
			}else{
				ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
			}
		} else {
			if(subscribe.getStatus().equals("1")){
				ToastUtils.show(getContext(), R.string.toast_subscribe_sucessed);
				checkSubscribeBtnStatus(mCurrentSubscribe);
				UIHelper.notifySubscribeDataChanged(this);
			}else if (subscribe.getStatus().contains("repeat")) {
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
			checkSubscribeBtnStatus(null);
			SubscribeManager.getInstance().deleteSubscribeTabOnline(this, ""+subscribe.getId(),subscribe.getIdtype());
			UIHelper.notifySubscribeDataChanged(this);
		} else if (statusBean.getStatus().equals("-1")) {
			ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_failed);
		} else if (statusBean.getStatus().equals("-2")) {
			ToastUtils.show(getContext(), R.string.toast_need_login);
		} else {
			ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_failed);
		}
	}
    
	private void initData() {
		mCurrentPage = 1;
		isLoadMoreAction = false;
		AppContext.getInstance().getBackChinaMode().setSpecialNewsSubscribeCallBack(this);
		mHandler = new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				if (isLoadMoreAction) {
					loadMoreNodata();
				} else {
					onRequestError(EmptyLayout.NODATA);
				}
				isLoadMoreAction = false;
				refreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				boolean isrefresh = mCurrentPage==1?true:false;
				if (!handleData(responseString,isrefresh)) {
					if(isLoadMoreAction){
						loadMoreNodata();
					}else{
					onRequestError(EmptyLayout.NODATA);
					}
				} else {
					//
					onRequestSuccess();
					stopLoadMore();
				}
				isLoadMoreAction = false;
			}
		};
		setImageFromNet(mLogo, mCurrentSubscribe.getLogo(),R.drawable.default_avatar);//
		mName.setText(mCurrentSubscribe.getTitle());
		mAdapter = new SubscribeDetailAdapter(this);
		mListView.setAdapter(mAdapter);
		loadData();
	}

	private void loadData() {
		AppOperator.runOnThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ResultListBean<SubscribeDetail> bean = (ResultListBean<SubscribeDetail>) CacheManager.readObject(mContext, getCacheKey());
				AppOperator.runOnMainThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (bean == null || bean.getItems().size() <= 0) {
							requestData();
						} else {
							setData(bean);
							setEmptyLayoutStatus(EmptyLayout.HIDE_LAYOUT);
						}
					}
				});
			}
			
		});
		
	}
	
    private String getCacheKey(){
    	return "subscribe_detail_" + mCurrentSubscribe.getId();
    }
	
	private Type getType() {
		return new TypeToken<ResultListBean<SubscribeDetail>>() {
		}.getType();
	}

    private void setData(ResultListBean<SubscribeDetail> bean){
    	mAdapter.addItem(bean.getItems());
    }
    
	private boolean handleData(String response,boolean isrefresh) {
		final ResultListBean<SubscribeDetail> bean = AppContext.createGson().fromJson(response, getType());
		List<SubscribeDetail> dataList = bean.getItems();
		if(dataList != null && dataList.size() > 0){
			if (isrefresh) {
				mAdapter.clear();
				mAdapter.addItem(dataList);
				AppOperator.runOnThread(new Runnable() {
					@Override
					public void run() {
						CacheManager.saveObject(mContext, bean, getCacheKey());
					}
				});
				return true;
			}else{
				mAdapter.addItem(dataList);
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		AppContext.getInstance().getBackChinaMode().setSpecialNewsSubscribeCallBack(null);
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		SubscribeDetail subscribeDetail = (SubscribeDetail) parent.getAdapter().getItem(position);
		if(SubscribeManager.getInstance().isUIdType(mCurrentSubscribe)){
			Blog blog = new Blog();
			blog.setId(subscribeDetail.getId());
			blog.setTitle(subscribeDetail.getTitle());
			blog.setUrl(subscribeDetail.getUrl());
			blog.setUrlapi(subscribeDetail.getUrlapi());
			UIHelper.enterBlogDetail(this, blog,false);
		} else {
			UIHelper.enterSpecialNewsDetailActivity(this, subscribeDetail);
		}
	}

	private void requestData() {
		mCurrentPage = 1;
		BackChinaApi.getSubscribeDetail(mCurrentSubscribe.getUrlapi(),mCurrentPage, mHandler);
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		requestData();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mCurrentPage = mCurrentPage+1;
		isLoadMoreAction = true;
		BackChinaApi.getSubscribeDetail(mCurrentSubscribe.getUrlapi(),mCurrentPage, mHandler);
	}
	
	
    public void refreshComplete(){
    	mListView.stopRefresh();
    }
    
	private void loadMoreNodata(){
		mListView.completeLoadMore();
	}
	
	private void stopLoadMore(){
		mListView.stopLoadMore();
	}
	
	protected void onRequestError(int type) {
		refreshComplete();
		setEmptyLayoutStatus(type);
	}
    
    protected void onRequestSuccess() {
    	refreshComplete();
    	mEmptyLayout.dismiss();
    }
    
    protected void setEmptyLayoutStatus(int type){
    	if (mEmptyLayout != null) {
    		mEmptyLayout.setErrorType(type);
		}
    }

	@Override
	public void onSubscribe() {
		// TODO Auto-generated method stub
		subscribeFunction();
	}
}
