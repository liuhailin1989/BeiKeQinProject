package com.android.backchina.ui;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.R;
import com.android.backchina.adapter.SubscribeCatAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.News;
import com.android.backchina.bean.SubscribeCat;
import com.android.backchina.bean.base.ChannelBean;
import com.android.backchina.bean.base.NewsListBean;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.fragment.DetailFragment;
import com.android.backchina.fragment.SubscribeCatContentFragment;
import com.android.backchina.ui.empty.EmptyLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SubscribeActivity extends BaseActivity implements BaseListAdapter.Callback,OnItemClickListener{
    
    private RequestManager mImgLoader;
    
	private ListView mListView;
	
	private EmptyLayout mEmptyLayout;
	
	protected TextHttpResponseHandler mHandler;
	
	private SubscribeCatAdapter mSubscribeCatAdapter;
	
	private Context mContext;
	
    public static void show(Context context) {
        Intent intent = new Intent(context, SubscribeActivity.class);
        context.startActivity(intent);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_layout);
        mContext = this;
        setupViews();
        initData();
    }
    
    private void setupViews(){
    	mListView = (ListView) findViewById(R.id.lv_subscribe_cat);
    	mEmptyLayout = (EmptyLayout) findViewById(R.id.error_layout);
    }
    
    private void initData(){
    	mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
    	mSubscribeCatAdapter = new SubscribeCatAdapter(this);
    	mListView.setAdapter(mSubscribeCatAdapter);
		mListView.setItemChecked(0, true);
    	mListView.setOnItemClickListener(this);
    	mHandler = new TextHttpResponseHandler() {

			@Override
			public void onFailure(int code, Header[] headers, String responseString,
					Throwable throwable) {
				// TODO Auto-generated method stub
				requestDataFailed(EmptyLayout.NETWORK_ERROR);
			}

			@Override
			public void onSuccess(int code, Header[] headers, String responseString) {
				// TODO Auto-generated method stub
				if(!handleData(responseString)){
					requestDataFailed(EmptyLayout.NETWORK_ERROR);
				}else{
					requestDataSuccess();
					handleView(mSubscribeCatAdapter.getItem(0));
				}
			}
    		
    	};
    	requestData();
    }
    
    private String getCacheKey(){
    	return "subscribe_cat_list";
    }
    
    private void requestData(){
    	AppOperator.runOnThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final ResultListBean<SubscribeCat> bean = (ResultListBean<SubscribeCat>) CacheManager.readObject(mContext, getCacheKey());
				AppOperator.runOnMainThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (bean == null) {
							BackChinaApi.getSubscribeCatList(mHandler);
						} else {
							setData(bean);
							requestDataSuccess();
							handleView(mSubscribeCatAdapter.getItem(0));
						}
					}
				});
			}
    		
    	});
    }
    
    private void requestDataFailed(int type){
    	mEmptyLayout.setErrorType(type);
    }
    
    private void requestDataSuccess(){
    	mEmptyLayout.dismiss();
    }
    
    private Type getType(){
    	return new TypeToken<ResultListBean<SubscribeCat>>() {}.getType();
    }
    
    private boolean handleData(String responseString){
    	final ResultListBean<SubscribeCat> resultListBean = AppContext.createGson().fromJson(responseString, getType());
    	if(resultListBean != null && resultListBean.getItems() != null){
    		List<SubscribeCat> listCat = resultListBean.getItems();
    		mSubscribeCatAdapter.clear();
    		mSubscribeCatAdapter.addItem(listCat);
    		AppOperator.runOnThread(new Runnable() {
				@Override
				public void run() {
					CacheManager.saveObject(mContext, resultListBean,
							getCacheKey());
				}
			});
    		return true;
    	}
    	return false;
    }
    
    private void setData(ResultListBean<SubscribeCat> bean){
    	mSubscribeCatAdapter.addItem(bean.getItems());
    }
    
    
    public Fragment getDataViewFragment(SubscribeCat subscribeCat){
        return SubscribeCatContentFragment.newInstance(subscribeCat.getCatid(),subscribeCat);
    }
    
    private void handleView(SubscribeCat subscribeCat) {
        try {
            Fragment fragment = getDataViewFragment(subscribeCat);
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.cat_content, fragment);
            trans.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		SubscribeCat subscribeCat = mSubscribeCatAdapter.getItem(position);
		handleView(subscribeCat);
	}

	@Override
	public RequestManager getImgLoader() {
		// TODO Auto-generated method stub
		if (mImgLoader == null) {
			mImgLoader = Glide.with(this);
		}
		return mImgLoader;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Date getSystemTime() {
		// TODO Auto-generated method stub
		return new Date();
	}
}
