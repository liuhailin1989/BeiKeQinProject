package com.android.backchina.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class FixedHeightListView extends ListView {

	public FixedHeightListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FixedHeightListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FixedHeightListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);

	}

}
