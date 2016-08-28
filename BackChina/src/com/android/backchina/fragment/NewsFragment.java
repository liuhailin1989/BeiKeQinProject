package com.android.backchina.fragment;

import java.lang.reflect.Type;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.android.backchina.adapter.NewsAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseListFragment;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.News;
import com.android.backchina.bean.base.PageBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.utils.StringUtils;
import com.google.gson.reflect.TypeToken;

public class NewsFragment extends BaseListFragment<News> implements
		OnTabReselectListener {

	public static final String HISTORY_NEWS = "history_news";

	public static final String NEWS_SYSTEM_TIME = "news_system_time";

	private boolean isFirst = true;

	private Handler handler = new Handler();
	
	private String mSystemTime;

	@Override
	protected void onRestartInstance(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onRestartInstance(bundle);
		mIsRefresh = false;
		mSystemTime = bundle.getString("system_time", "");
	}
	
	@Override
	protected void initWidget(View root) {
		// TODO Auto-generated method stub
		super.initWidget(root);
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		if (!StringUtils.isEmpty(mSystemTime)) {
			((NewsAdapter) mAdapter).setSystemTime(mSystemTime);
		}
	}
	
	@Override
	public void onRefreshing() {
		// TODO Auto-generated method stub
		super.onRefreshing();
	}
	
	@Override
	protected void requestData() {
		// TODO Auto-generated method stub
		super.requestData();
//		 BackChinaApi.getNewsList(mIsRefresh ? mBean.getPrevPageToken() : mBean.getNextPageToken(), mHandler);
		BackChinaApi.getNewsList("", mHandler);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
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
		 return new TypeToken<ResultBean<PageBean<News>>>() {
	        }.getType();
	}

	@Override
	protected void onRequestFinish() {
		// TODO Auto-generated method stub
		super.onRequestFinish();
	}
	
	@Override
	protected void setListData(ResultBean<PageBean<News>> resultBean) {
		// TODO Auto-generated method stub
		super.setListData(resultBean);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
}
