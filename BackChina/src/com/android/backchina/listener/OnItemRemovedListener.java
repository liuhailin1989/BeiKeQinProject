package com.android.backchina.listener;

public interface OnItemRemovedListener<T> {
	void onItemRemoved(int position, T removedItem);
}
