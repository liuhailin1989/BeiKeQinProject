package com.android.backchina.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class BaseRecyclerView extends RecyclerView{

    public BaseRecyclerView(Context context) {
        super(context);
    }
    
    public BaseRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
