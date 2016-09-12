package com.android.backchina.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseRycyclerViewAdapter extends RecyclerView.Adapter<BaseRycyclerViewAdapter.BaseRecycleViewHolder> {

	protected OnItemClickListener itemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		itemClickListener = l;
	}

	public static class BaseRecycleViewHolder extends RecyclerView.ViewHolder {

		public BaseRecycleViewHolder(View itemView) {
			super(itemView);
			// TODO Auto-generated constructor stub
		}

	}

	public interface OnItemClickListener {

		void onItemClick(View view, int position);
	}
}
