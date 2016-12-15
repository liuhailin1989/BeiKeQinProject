package com.news.soft.backchina.ui;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.AppOperator;
import com.news.soft.backchina.R;
import com.news.soft.backchina.adapter.SubscribeDetailAdapter;
import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.base.BaseActivity;
import com.news.soft.backchina.bean.Blog;
import com.news.soft.backchina.bean.BlogDetail;
import com.news.soft.backchina.bean.StatusBean;
import com.news.soft.backchina.bean.Subscribe;
import com.news.soft.backchina.bean.SubscribeDetail;
import com.news.soft.backchina.bean.base.ActivitiesBean;
import com.news.soft.backchina.bean.base.ResultListBean;
import com.news.soft.backchina.cache.CacheManager;
import com.news.soft.backchina.ui.empty.EmptyLayout;
import com.news.soft.backchina.utils.ToastUtils;
import com.news.soft.backchina.utils.UIHelper;
import com.news.soft.backchina.widget.CircleImageView;
import com.news.soft.backchina.widget.XListView;
import com.news.soft.backchina.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class BlogSpaceActivity extends BaseActivity implements OnItemClickListener,IXListViewListener{

	public final static String BUNDLE_KEY_BLOG_DETAIL = "BUNDLE_KEY_BLOG_DETAIL";

	private CircleImageView mLogo;

	private TextView mName;

	private TextView mBtnSubscribe;

	private XListView mListView;
	
	private EmptyLayout mEmptyLayout;

	private BlogDetail mCurrentBlogDetail;

	protected TextHttpResponseHandler mHandler;

	private SubscribeDetailAdapter mAdapter;

	private Context mContext;
	
	private int mCurrentPage = 1;
	
	private boolean isLoadMoreAction = false;

	public static void show(Context context, BlogDetail blogDetail) {
		Intent intent = new Intent(context, BlogSpaceActivity.class);
		intent.putExtra(BUNDLE_KEY_BLOG_DETAIL, blogDetail);
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
		mCurrentBlogDetail = (BlogDetail) bundle
				.getSerializable(BUNDLE_KEY_BLOG_DETAIL);
	}

	private void setupViews() {
		mLogo = (CircleImageView) findViewById(R.id.iv_logo);
		mName = (TextView) findViewById(R.id.tv_name);
		mBtnSubscribe = (TextView) findViewById(R.id.btn_subscribe);
		mBtnSubscribe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BackChinaApi.subscribeBlog(mCurrentBlogDetail.getAuthorid(), new TextHttpResponseHandler() {
					
					@Override
					public void onSuccess(int code, Header[] headers, String response) {
						// TODO Auto-generated method stub
						handleSubscribeResponse(headers,response);
					}
					
					@Override
					public void onFailure(int code, Header[] headers, String response, Throwable throwable) {
						// TODO Auto-generated method stub
						ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
					}
				});
			}
		});
		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		//
		 mEmptyLayout = (EmptyLayout) findViewById(R.id.error_layout);
	}
	
    private void handleSubscribeResponse(Header[] headers,String response) {
    	Type type = new TypeToken<ActivitiesBean<Subscribe>>() {
        }.getType();
        ActivitiesBean<Subscribe> activitiesBean = AppContext.createGson().fromJson(response, type);
        Subscribe subscribe = activitiesBean.getActivities();
		if (subscribe.getStatus() == null) {
			if (subscribe.getFavid() != null) {
				ToastUtils.show(getContext(), R.string.toast_subscribe_sucessed);
				UIHelper.notifySubscribeDataChanged(this);
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

	private void initData() {
		mCurrentPage = 1;
		isLoadMoreAction = false;
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
		setImageFromNet(mLogo, mCurrentBlogDetail.getAvatar());//
		mName.setText(mCurrentBlogDetail.getUsername());
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
						}
					}
				});
			}
			
		});
		
	}
	
    private String getCacheKey(){
    	return "subscribe_detail_" + mCurrentBlogDetail.getAuthorid();
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
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		SubscribeDetail subscribeDetail = mAdapter.getItem(position);
		UIHelper.enterSpecialNewsDetailActivity(this,subscribeDetail);
	}

	private void requestData() {
		mCurrentPage = 1;
//		BackChinaApi.getSubscribeDetail(mCurrentBlog.getCaturlapi(),mCurrentPage, mHandler);
		BackChinaApi.getBlogerList(mCurrentBlogDetail.getAuthorid(),mCurrentPage, mHandler);
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
		BackChinaApi.getBlogerList(mCurrentBlogDetail.getAuthorid(),mCurrentPage, mHandler);
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
	
}
