package com.android.backchina.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.backchina.R;
import com.android.backchina.api.ApiHttpClient;
import com.android.backchina.base.adapter.BaseRecyclerViewAdapter;
import com.android.backchina.bean.Video;
import com.android.backchina.utils.StringUtils;

public class VideoLinearAdapter extends BaseRecyclerViewAdapter<Video> {

	public VideoLinearAdapter(Context context, Callback callback) {
		super(context, callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
		// TODO Auto-generated method stub
		if (holder != null && holder instanceof VideoLinearRecyclerViewHolder) {
			VideoLinearRecyclerViewHolder viewHolder = (VideoLinearRecyclerViewHolder) holder;
			Video item = mDatas.get(position);
			if (StringUtils.isEmpty(item.getAvatar())) {
				viewHolder.avatar.setImageResource(R.drawable.default_avatar);
			} else {
				setImageForNet(viewHolder.avatar,
						ApiHttpClient.getAbsoluteApiUrl(item.getAvatar()),
						R.drawable.default_avatar);
			}
			if (StringUtils.isEmpty(item.getUsername())) {
				viewHolder.author.setText("倍可亲");
			} else {
				viewHolder.author.setText(item.getUsername());
			}
			String fromat = mContext.getResources().getString(
					R.string.video_item_views);
			String viewsValue = String.format(fromat, item.getViews());
			viewHolder.playCount.setText(viewsValue);

			viewHolder.comments.setText(String.valueOf(item.getComments()));
		}
	}

	@Override
	public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		// TODO Auto-generated method stub
		View itemView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.layout_recycler_item_video_linear, parent, false);
		return new VideoLinearRecyclerViewHolder(itemView);
	}

	public final static class VideoLinearRecyclerViewHolder extends
			BaseRecycleViewHolder {

		public ImageView avatar;
		public TextView author;
		public TextView playCount;
		public TextView comments;
		public ImageView share;

		public VideoLinearRecyclerViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
			author = (TextView) itemView.findViewById(R.id.author);
			playCount = (TextView) itemView.findViewById(R.id.tv_play_count);
			comments = (TextView) itemView.findViewById(R.id.tv_comments);
			share = (ImageView) itemView.findViewById(R.id.iv_share);
		}
	}

}
