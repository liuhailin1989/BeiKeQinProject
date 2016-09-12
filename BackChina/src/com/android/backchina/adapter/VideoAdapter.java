package com.android.backchina.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.backchina.base.adapter.BaseRycyclerViewAdapter;
import com.android.backchina.bean.Video;

public class VideoAdapter extends BaseRycyclerViewAdapter {

	private Context mContext;

	private List<Video> mDataList = new ArrayList<Video>();
	
	public VideoAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

    public void setData(List<Video> list){
        mDataList = list;
    }
    
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public void onBindViewHolder(BaseRecycleViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public BaseRecycleViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public final static class VideoRecyclerViewHolder extends
			BaseRecycleViewHolder {

		public VideoRecyclerViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}
	}

}
