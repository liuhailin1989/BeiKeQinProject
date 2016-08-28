package com.android.backchina.base;

import com.android.backchina.interf.OnTabReselectListener;

public abstract class BaseGeneralListFragment<T> extends BaseListFragment<T> implements OnTabReselectListener{

    @Override
    public void onTabReselect() {
        if(mListView != null){
            mListView.setSelection(0);
            mRefreshLayout.setRefreshing(true);
            onRefreshing();
        }
    }
}
