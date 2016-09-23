package com.android.backchina.adapter;

import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Comment;
import com.android.backchina.ui.comment.CommentsUtil;
import com.android.backchina.utils.StringUtils;

public class CommentNewsAdapter extends BaseListAdapter<Comment>{

	public CommentNewsAdapter(Callback callback) {
		super(callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void convert(ViewHolder vh, Comment item, int position) {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(item.getUsername())) {
			vh.setText(R.id.tv_username,"游客");
		} else {
			vh.setText(R.id.tv_username,item.getUsername());
		}
		//
		 vh.setText(R.id.tv_pub_date,StringUtils.friendly_time(item.getDateline()));
		//
		String floorText = String.format(mCallback.getContext().getResources().getString(R.string.comments_floor_count),item.getPosition());
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
	protected int getLayoutId(int position, Comment item) {
		// TODO Auto-generated method stub
		return R.layout.layout_list_item_comment_news;
	}

}
