package com.news.soft.backchina.listener;

public interface OnItemRemovedListener<T> {
	void onItemRemoved(int position, T removedItem);
}
