package com.android.backchina.adapter;

import android.widget.TextView;

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Blog;
import com.android.backchina.fragment.BlogFragment;
import com.android.backchina.utils.StringUtils;

public class BlogAdapter extends BaseListAdapter<Blog>{

	public BlogAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, Blog item, int position) {
		// TODO Auto-generated method stub
		TextView title = vh.getView(R.id.tv_title);
		TextView userName = vh.getView(R.id.tv_user_name);
		title.setText(item.getTitle());
		userName.setText(item.getUsername());
		if (StringUtils.isEmpty(item.getPic())) {
			vh.setGone(R.id.iv_thumb);
		}else{
			vh.setVisibility(R.id.iv_thumb);
			vh.setImageForNet(R.id.iv_thumb, item.getPic());
		}
		TextView summary  = vh.getView(R.id.tv_summary);
		summary.setText(item.getSummary());
		String viewsString = mCallback.getContext().getResources().getString(R.string.blog_item_views);
		String result = String.format(viewsString, item.getComments(),item.getViews());
		vh.setText(R.id.tv_views,result);
		
		if(AppContext.isOnReadedPostList(BlogFragment.HISTORY_BLOG, item.getId()+"")){
			title.setTextColor(mCallback.getContext().getResources().getColor(R.color.count_text_color_light));
			userName.setTextColor(mCallback.getContext().getResources().getColor(R.color.count_text_color_light));
			summary.setTextColor(mCallback.getContext().getResources().getColor(R.color.count_text_color_light));
		}else{
			title.setTextColor(mCallback.getContext().getResources().getColor(R.color.blog_item_title_color));
			userName.setTextColor(mCallback.getContext().getResources().getColor(R.color.blog_item_author_text_color));
			summary.setTextColor(mCallback.getContext().getResources().getColor(R.color.blog_item_summary_text_color));
		}
	}

	@Override
	protected int getLayoutId(int position, Blog item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_blog;
	}

}
