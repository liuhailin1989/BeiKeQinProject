package com.android.backchina.adapter;

import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.News;

public class RelatedNewsAdapter extends BaseListAdapter<News>{

	public RelatedNewsAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, News item, int position) {
		// TODO Auto-generated method stub
		vh.setText(R.id.tv_title, item.getTitle());
		vh.setImageForNet(R.id.iv_thumb, item.getPic());
	}

	@Override
	protected int getLayoutId(int position, News item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_related_news;
	}

}
