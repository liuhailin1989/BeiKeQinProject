package com.news.soft.backchina.adapter;

import com.news.soft.backchina.R;
import com.news.soft.backchina.base.adapter.BaseListAdapter;
import com.news.soft.backchina.bean.SubscribeCat;

public class SubscribeCatAdapter extends BaseListAdapter<SubscribeCat>{

	public SubscribeCatAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, SubscribeCat item, int position) {
		// TODO Auto-generated method stub
		vh.setText(R.id.tv_name, item.getName());
	}

	@Override
	protected int getLayoutId(int position, SubscribeCat item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_subscribe_cat;
	}
}
