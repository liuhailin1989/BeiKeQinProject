package com.android.backchina.base;

import com.android.backchina.R;
import com.android.backchina.ui.empty.EmptyLayout;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseRecyclerViewFragment<T> extends BaseFragment<T> {

	 protected EmptyLayout mErrorLayout;
	 
	 protected RecyclerView mRecyclerView;
	
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
		mRecyclerView = (RecyclerView) root.findViewById(R.id.base_recycler_view);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
	}

	private void requestData() {

	}
}
