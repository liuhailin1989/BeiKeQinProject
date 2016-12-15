package com.news.soft.backchina.interf;

public interface OnItemChangeListener<T> {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemRemoved(int position);

    void onItemInsert(int position, T data);
}
