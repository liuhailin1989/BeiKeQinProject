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
		vh.setText(R.id.tv_user_name, item.getUsername());
		if (StringUtils.isEmpty(item.getPic())) {
			vh.setGone(R.id.iv_thumb);
		}else{
			vh.setVisibility(R.id.iv_thumb);
			vh.setImageForNet(R.id.iv_thumb, item.getPic());
		}
		vh.setText(R.id.tv_summary, item.getSummary());
		String viewsString = mCallback.getContext().getResources().getString(R.string.blog_item_views);
		String result = String.format(viewsString, item.getComments(),item.getViews());
		vh.setText(R.id.tv_views,result);
	}

	@Override
	protected int getLayoutId(int position, Blog item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_blog;
	}

}
