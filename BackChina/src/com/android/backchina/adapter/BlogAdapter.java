package com.android.backchina.adapter;

import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Blog;
import com.android.backchina.utils.StringUtils;

public class BlogAdapter extends BaseListAdapter<Blog>{

	public BlogAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, Blog item, int position) {
		// TODO Auto-generated method stub
		vh.setText(R.id.tv_title, item.getTitle());
		vh.setImageForNet(R.id.iv_thumb, item.getAvatar());
		vh.setText(R.id.tv_time,StringUtils.friendly_time(item.getDateline()));
		vh.setText(R.id.tv_origin,item.getUsername());
	}

	@Override
	protected int getLayoutId(int position, Blog item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_blog;
	}

}
