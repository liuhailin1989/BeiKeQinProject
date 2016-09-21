package com.android.backchina.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class RecycleViewItemDecoration extends RecyclerView.ItemDecoration{
    
    int top = 0;
    int bottom = 0;
    int left = 0;
    int right = 0;
    
    public RecycleViewItemDecoration(int gap){
        this.top = gap;
        this.bottom = gap;
        this.left = gap;
        this.right = gap;
    }
    
    public RecycleViewItemDecoration(int top, int bottom, int left, int right){
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        // TODO Auto-generated method stub
        outRect.top = this.top;
        outRect.bottom = this.bottom;
        outRect.left = this.left;
        outRect.right = this.right;
    }
}
