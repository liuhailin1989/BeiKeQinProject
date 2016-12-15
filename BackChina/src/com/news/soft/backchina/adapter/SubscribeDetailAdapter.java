package com.news.soft.backchina.adapter;

import com.news.soft.backchina.R;
import com.news.soft.backchina.base.adapter.BaseListAdapter;
import com.news.soft.backchina.bean.SubscribeDetail;
import com.news.soft.backchina.utils.StringUtils;

public class SubscribeDetailAdapter extends BaseListAdapter<SubscribeDetail> {

	public SubscribeDetailAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, SubscribeDetail item, int position) {
		// TODO Auto-generated method stub
		String viewString = mCallback.getContext().getResources()
				.getString(R.string.subscribe_views);
		vh.setText(R.id.tv_title, item.getTitle());
		vh.setText(R.id.tv_count,
				String.format(viewString, item.getComments(), item.getViews()));
		vh.setText(R.id.tv_from, item.getFrom());
		if (StringUtils.isEmpty(item.getPic())) {
			vh.setGone(R.id.iv_thumb);
		} else {
			vh.setVisibility(R.id.iv_thumb);
			vh.setImageForNet(R.id.iv_thumb, item.getPic());
		}
		vh.setText(R.id.tv_summary, item.getSummary());
	}

	@Override
	protected int getLayoutId(int position, SubscribeDetail item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_subscribe_detial;
	}

}
