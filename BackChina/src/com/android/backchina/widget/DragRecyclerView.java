package com.android.backchina.widget;


import android.content.Context;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import com.android.backchina.base.BaseRecyclerView;
import com.android.backchina.interf.IDragItem;
import com.android.backchina.interf.DragItemTouchHelperCallBack;

public class DragRecyclerView extends BaseRecyclerView{

    public DragRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }
}
