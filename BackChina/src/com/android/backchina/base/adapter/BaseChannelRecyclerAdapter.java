package com.android.backchina.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseChannelRecyclerAdapter<T> extends RecyclerView.Adapter<BaseChannelRecyclerAdapter.BaseViewHolder> {

    protected List<T> mDatas = new ArrayList<T>();

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}
	
    public void setDatas(List<T> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }
    
    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public  void onDrag(){
        	
        };

        public  void onDragFinished(){
        	
        };

        public  void onLongPressMode(){
        	
        };

        public void onNormalMode(){
        	
        };
    }
}
