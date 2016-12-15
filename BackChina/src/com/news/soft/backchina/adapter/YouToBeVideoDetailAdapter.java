package com.news.soft.backchina.adapter;

import com.news.soft.backchina.R;
import com.news.soft.backchina.base.adapter.BaseListAdapter;
import com.news.soft.backchina.bean.VideoDetail;

public class YouToBeVideoDetailAdapter extends BaseListAdapter<VideoDetail>{

	public YouToBeVideoDetailAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, VideoDetail item, int position) {
		// TODO Auto-generated method stub
		vh.setImageForNet(R.id.iv_img, item.getImg());
		vh.setText(R.id.tv_name, item.getName());
	}

	@Override
	protected int getLayoutId(int position, VideoDetail item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_youtobe_detail;
	}

}
