
package com.android.backchina.fragment;

import java.lang.reflect.Type;

import android.R.color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.android.backchina.adapter.NewsAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseListFragment;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.News;
import com.android.backchina.bean.base.PageBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;

public class NewsFragment extends BaseListFragment<News> {

    public static final String HISTORY_NEWS = "history_news";

    public static final String NEWS_SYSTEM_TIME = "news_system_time";

    private static final String CACHE_KEY_PREFIX = "newslist_";

    public static final String BUNDLE_KEY_CATALOG = "BUNDLE_KEY_CATALOG";
    
    public static final String BUNDLE_KEY_CHANNELITEM = "BUNDLE_KEY_CHANNELITEM";

    private int mCatlog = 0;

    private boolean isFirst = true;

    private Handler handler = new Handler();

    private String mSystemTime;
    
    private ChannelItem currentChannelItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initBundle(Bundle bundle) {
        // TODO Auto-generated method stub
        mCatlog = bundle.getInt(BUNDLE_KEY_CATALOG);
        currentChannelItem = (ChannelItem) bundle.getSerializable(BUNDLE_KEY_CHANNELITEM);
        TLog.d("called = "  + currentChannelItem.getName());
    }

    @Override
    protected String getCacheKeyPrefix() {
        // TODO Auto-generated method stub
        return CACHE_KEY_PREFIX + mCatlog;
    }

    @Override
    protected void onRestartInstance(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onRestartInstance(bundle);
        mIsRefresh = false;
        mSystemTime = bundle.getString("system_time", "");
        TLog.d("url = " + currentChannelItem.getUrlapi());
    }

    @Override
    protected void setupViews(View root) {
        // TODO Auto-generated method stub
        super.setupViews(root);
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        super.initData();
        if (!StringUtils.isEmpty(mSystemTime)) {
            ((NewsAdapter) mAdapter).setSystemTime(mSystemTime);
        }
    }

    @Override
    public void onRefreshing() {
        // TODO Auto-generated method stub
        super.onRefreshing();
    }

    @Override
    protected void requestData() {
        // TODO Auto-generated method stub
        super.requestData();
        // BackChinaApi.getNewsList(mIsRefresh ? mBean.getPrevPageToken() :
        // mBean.getNextPageToken(), mHandler);
        BackChinaApi.getNewsList(currentChannelItem.getUrlapi(), mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
//        super.onItemClick(parent, view, position, id);
        News item = mAdapter.getItem(position);
//        UIHelper.enterNewsDetail(getActivity(), item.getId(),20);
        UIHelper.enterNewsDetail(getActivity(), item);
    }

    @Override
    public void onTabReselect() {
        // TODO Auto-generated method stub
        show();
    }

    @Override
    protected BaseListAdapter<News> getListAdapter() {
        // TODO Auto-generated method stub
        return new NewsAdapter(this);
    }

    @Override
    protected Type getType() {
        // TODO Auto-generated method stub
        return new TypeToken<ResultBean<PageBean<News>>>() {
        }.getType();
    }

    @Override
    protected void onRequestFinish() {
        // TODO Auto-generated method stub
        super.onRequestFinish();
    }

    @Override
    protected void setListData(PageBean<News> pageBean) {
        // TODO Auto-generated method stub
        super.setListData(pageBean);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        TLog.d("called = "  + currentChannelItem.getName());
    }
    
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        TLog.d("called = "  + currentChannelItem.getName());
    }
    
    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        TLog.d("called = "  + currentChannelItem.getName());
    }
}
