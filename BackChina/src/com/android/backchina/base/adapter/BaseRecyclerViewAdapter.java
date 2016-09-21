package com.android.backchina.base.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.android.backchina.utils.ImageLoader;
import com.bumptech.glide.RequestManager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseRecycleViewHolder> {


    protected List<T> mDatas;
    
	protected OnItemClickListener itemClickListener;
	
    protected Callback mCallback;
    
    protected Context mContext;
    
    public BaseRecyclerViewAdapter(Context context,Callback callback){
    	 this.mContext = context;
    	 this.mCallback = callback;
    	 this.mDatas = new ArrayList<T>();
    }
    
	public void setOnItemClickListener(OnItemClickListener l) {
		itemClickListener = l;
	}
    
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}
	
	public List<T> getmDatas() {
		return mDatas;
	}
	
	public void updateItem(int location, T item) {
        if (mDatas.isEmpty()) {
        	return;
        }
        mDatas.set(location, item);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        checkListNull();
        mDatas.add(item);
        notifyDataSetChanged();
    }

    public void addItem(int location, T item) {
        checkListNull();
        mDatas.add(location, item);
        notifyDataSetChanged();
    }

    public void addItem(List<T> items) {
        checkListNull();
        mDatas.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(int position, List<T> items) {
        checkListNull();
        mDatas.addAll(position, items);
        notifyDataSetChanged();
    }

    public void removeItem(int location) {
        if (mDatas == null || mDatas.isEmpty()) {
            return;
        }
        mDatas.remove(location);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mDatas == null || mDatas.isEmpty()) {
            return;
        }
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void checkListNull() {
        if (mDatas == null) {
            mDatas = new ArrayList<T>();
        }
    }
    
    public void setImageForNet(ImageView iv, String imgUrl, int emptyRes) {
        RequestManager loader = mCallback.getImgLoader();
        ImageLoader.loadImage(loader, iv, imgUrl, emptyRes);
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
	
    public interface Callback {
        RequestManager getImgLoader();

        Context getContext();

        Date getSystemTime();
    }
}
