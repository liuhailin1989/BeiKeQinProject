package com.android.backchina.adapter;

import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.SubscribeDetail;

public class SubscribeDetailAdapter extends BaseListAdapter<SubscribeDetail>{

	public SubscribeDetailAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, SubscribeDetail item, int position) {
		// TODO Auto-generated method stub
		String viewString  = mCallback.getContext().getResources().getString(R.string.subscribe_views);
		vh.setText(R.id.tv_title, item.getTitle());
		vh.setText(R.id.tv_count, String.format(viewString, item.getComments(),item.getViews()));
		vh.setText(R.id.tv_from, item.getFrom());
		vh.setText(R.id.tv_summary, item.getSummary());
	}

	@Override
	protected int getLayoutId(int position, SubscribeDetail item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_subscribe_detial;
	}

}
