package com.android.backchina.ui;

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

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.R;
import com.android.backchina.adapter.SubscribeDetailAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.bean.StatusBean;
import com.android.backchina.bean.Subscribe;
import com.android.backchina.bean.SubscribeDetail;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.UIHelper;
import com.android.backchina.widget.CircleImageView;
import com.android.backchina.widget.XListView;
import com.android.backchina.widget.XListView.IXListViewListener;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SubscribeDetailActivity extends BaseActivity implements OnItemClickListener,IXListViewListener{

	public final static String BUNDLE_KEY_SUBSCRIBE = "BUNDLE_KEY_SUBSCRIBE";

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
	}

	private void setupViews() {
		mLogo = (CircleImageView) findViewById(R.id.iv_logo);
		mName = (TextView) findViewById(R.id.tv_name);
		mBtnSubscribe = (TextView) findViewById(R.id.btn_subscribe);
		mBtnSubscribe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
								Toast.makeText(getContext(), "订阅失败",
										Toast.LENGTH_SHORT).show();
							}
						});
			}
		});
		mListView = (XListView) findViewById(R.id.list_view);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		
		mEmptyLayout = (EmptyLayout) findViewById(R.id.error_layout);
	}

    private void handleSubscribeResponse(Header[] headers,String response) {
    	Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
        }.getType();
        ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson().fromJson(response, type);
        StatusBean statusBean = activitiesBean.getActivities();
        if (statusBean.getStatus().equals("1")) {
        	Toast.makeText(getContext(), "订阅成功", Toast.LENGTH_SHORT).show();
        	UIHelper.notifySubscribeDataChanged(this);
        }else if (statusBean.getStatus().equals("-1")) {
        	Toast.makeText(getContext(), "订阅失败", Toast.LENGTH_SHORT).show();
        }else if (statusBean.getStatus().equals("-2")) {
        	Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
        }else{
        	Toast.makeText(getContext(), "订阅失败", Toast.LENGTH_SHORT).show();
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
}
