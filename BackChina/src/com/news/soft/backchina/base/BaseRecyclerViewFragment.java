package com.news.soft.backchina.base;

import java.util.Date;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

import com.news.soft.backchina.AppConfig;
import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.AppOperator;
import com.news.soft.backchina.R;
import com.news.soft.backchina.base.adapter.BaseRecyclerViewAdapter;
import com.news.soft.backchina.base.adapter.BaseRecyclerViewAdapter.OnItemClickListener;
import com.news.soft.backchina.ui.empty.EmptyLayout;
import com.news.soft.backchina.widget.xrecyclerview.ProgressStyle;
import com.news.soft.backchina.widget.xrecyclerview.XRecyclerView;
import com.news.soft.backchina.widget.xrecyclerview.XRecyclerView.LoadingListener;

public abstract class BaseRecyclerViewFragment<T> extends BaseFragment<T>
		implements BaseRecyclerViewAdapter.Callback {

	protected EmptyLayout mErrorLayout;

	protected XRecyclerView mRecyclerView;

	protected BaseRecyclerViewAdapter mAdapter;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frament_base_recycler_view;
	}

	@Override
	protected void setupViews(View root) {
		// TODO Auto-generated method stub
		super.setupViews(root);
		mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
		mRecyclerView = (XRecyclerView) root
				.findViewById(R.id.base_recycler_view);
		mRecyclerView.setLayoutManager(getLayoutManager());
		mAdapter = getAdapter();
		mAdapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				// TODO Auto-generated method stub
				onVideoItemClick(view, position);
			}
		});
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.addItemDecoration(getItemDecoration());
		mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
		mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
		mRecyclerView.setLoadingListener(new LoadingListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				AppOperator.runOnMainThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						onRefreshData();
					}
				});
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				AppOperator.runOnMainThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						onLoadMoreData();
					}
				});

			}
		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
	}

	private void requestData() {
		onRequestData();
	}

	protected void onRequestData() {

	}

	public void onRefreshData() {
		// TODO Auto-generated method stub
		requestData();
	}

	public void onLoadMoreData() {
		// TODO Auto-generated method stub
	}

	public void stopLoadMore() {
		mRecyclerView.loadMoreComplete();
	}

	public void loadMoreNodata() {
		mRecyclerView.setNoMore(true);
	}
	
	public void autoRefresh() {
		mRecyclerView.scrollToPosition(0);
		mRecyclerView.setRefreshing(true);
	}

	protected void onRequestError(int type) {
		refreshComplete();
		setEmptyLayoutStatus(type);
	}

	protected void onRequestSuccess() {
		refreshComplete();
		mErrorLayout.dismiss();
	}

	public void refreshComplete() {
		mRecyclerView.refreshComplete();
	}

	protected void setEmptyLayoutStatus(int type) {
		if (mErrorLayout != null) {
			mErrorLayout.setErrorType(type);
		}
	}

    public void saveRefreshTime(String key){
    	long time = System.currentTimeMillis();
    	AppContext.setToPreferences(key, time);
    }
    
    public boolean isNeedToAutoRefresh(String key){
    	long lastTime = AppContext.getPreferences().getLong(key, 0);
    	long temp = (System.currentTimeMillis() - lastTime);
    	if(temp >= AppConfig.AUTO_REFRESH_TIME){
    		return true;
    	}else{
    		return false;
    	}
    }
    
	@Override
	public Date getSystemTime() {
		// TODO Auto-generated method stub
		return null;
	}

	protected abstract BaseRecyclerViewAdapter<T> getAdapter();

	protected abstract LayoutManager getLayoutManager();

	protected abstract ItemDecoration getItemDecoration();

	protected abstract void onVideoItemClick(View view, int position);
}
