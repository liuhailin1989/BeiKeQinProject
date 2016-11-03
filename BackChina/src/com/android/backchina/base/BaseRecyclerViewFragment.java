package com.android.backchina.base;

import java.util.Date;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

import com.android.backchina.AppOperator;
import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseRecyclerViewAdapter;
import com.android.backchina.base.adapter.BaseRecyclerViewAdapter.OnItemClickListener;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.widget.xrecyclerview.ProgressStyle;
import com.android.backchina.widget.xrecyclerview.XRecyclerView;
import com.android.backchina.widget.xrecyclerview.XRecyclerView.LoadingListener;

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
