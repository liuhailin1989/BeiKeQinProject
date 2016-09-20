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

public class VideoAdapter extends BaseRecyclerViewAdapter<Video> {

	public VideoAdapter(Context context, Callback callback) {
		super(context,callback);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onBindViewHolder(BaseRecycleViewHolder holder, int position) {
		// TODO Auto-generated method stub
		if (holder != null && holder instanceof VideoRecyclerViewHolder) {
			VideoRecyclerViewHolder viewHolder = (VideoRecyclerViewHolder) holder;
			Video item = mDatas.get(position);
			if (StringUtils.isEmpty(item.getPic_large())) {
				setImageForNet(viewHolder.thumb,
						ApiHttpClient.getAbsoluteApiUrl(item.getPic()),
						R.drawable.bg_normal);
			} else {
				setImageForNet(viewHolder.thumb,
						ApiHttpClient.getAbsoluteApiUrl(item.getPic_large()),
						R.drawable.bg_normal);
			}
			viewHolder.thumbIntro.setText("更新至 "+item.getNum());
			viewHolder.title.setText(item.getTitle());
		}
	}

	@Override
	public BaseRecycleViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		// TODO Auto-generated method stub
		View itemView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.layout_recycler_item_video, parent, false);
		return new VideoRecyclerViewHolder(itemView);
	}

	public final static class VideoRecyclerViewHolder extends
			BaseRecycleViewHolder {

		public ImageView thumb;
		public TextView thumbIntro;
		public TextView title;

		public VideoRecyclerViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
			thumb = (ImageView) itemView.findViewById(R.id.iv_thumb);
			thumbIntro = (TextView) itemView.findViewById(R.id.thumb_intro);
			title = (TextView) itemView.findViewById(R.id.title);
		}
	}

}
