package com.news.soft.backchina.base;

import java.util.ArrayList;
import java.util.List;

import com.news.soft.backchina.bean.Entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseListViewAdapter<T extends Entity> extends BaseAdapter{

	protected List<T> mDataList = new ArrayList<T>();
	
	private LayoutInflater mInflater;
	
	public LayoutInflater getLayoutInflater(Context context){
		if(mInflater == null){
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		return mInflater;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	public void setData(List<T> datas){
		mDataList = datas;
		notifyDataSetChanged();
	}
	
	public void addData(List<T> datas){
		if(mDataList != null && datas != null){
			mDataList.addAll(datas);
		}
		notifyDataSetChanged();
	}
	
	public void addItem(T obj){
		if(mDataList != null){
			mDataList.add(obj);
		}
		notifyDataSetChanged();
	}
	
	public void addItem(int index,T obj){
		if(mDataList != null){
			mDataList.add(index,obj);
		}
		notifyDataSetChanged();
	}
	
	public void removeItem(T obj){
		if(mDataList != null){
			mDataList.remove(obj);
		}
		notifyDataSetChanged();
	}
	
	public void clear(){
		if(mDataList != null){
			mDataList.clear();
		}
		notifyDataSetChanged();
	}
	
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getRealView(position,contentView,parent);
	}

	public View getRealView(int position, View contentView, ViewGroup viewGroup){
		return null;
	}
}
