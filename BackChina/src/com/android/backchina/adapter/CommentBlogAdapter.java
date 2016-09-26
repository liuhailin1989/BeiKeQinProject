package com.android.backchina.adapter;

import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Comment;
import com.android.backchina.bean.base.BlogCommentBean;
import com.android.backchina.ui.comment.CommentsUtil;
import com.android.backchina.utils.StringUtils;

public class CommentBlogAdapter extends BaseListAdapter<BlogCommentBean>{

	public CommentBlogAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, BlogCommentBean item, int position) {
		// TODO Auto-generated method stub
			vh.setText(R.id.tv_username,item.getAuthor());
		//
		 vh.setText(R.id.tv_pub_date,StringUtils.friendly_time(item.getDateline()));
		//
		String floorText = String.format(mCallback.getContext().getResources().getString(R.string.comments_floor_count),position + 1);
		vh.setText(R.id.tv_floor,floorText);
		//
		String msg = item.getMessage();
		
		String referMsg = CommentsUtil.getReferComments(msg);
		if(referMsg != null){
			vh.setVisibility(R.id.tv_refer);
			vh.setText(R.id.tv_refer,referMsg);
		}else{
			vh.setGone(R.id.tv_refer);
		}
		//
		String commentsMsg = CommentsUtil.getCommentsMessages(msg);
		vh.setText(R.id.tv_message,commentsMsg);
	}

	@Override
	protected int getLayoutId(int position, BlogCommentBean item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_comment_news;
	}

}
