package com.android.backchina.base;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.backchina.AppOperator;
import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.base.NewsListBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.TLog;
import com.android.backchina.widget.SuperRefreshLayout;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public abstract class BaseListFragment<T> extends BaseFragment<T> implements SuperRefreshLayout.SuperRefreshLayoutListener, 
             OnItemClickListener, BaseListAdapter.Callback, View.OnClickListener,OnTabReselectListener {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOADING = 1;
    public static final int TYPE_NO_MORE = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_NET_ERROR = 4;
    
    //
    protected ListView mListView;
    
    protected SuperRefreshLayout mRefreshLayout;
    
    protected EmptyLayout mErrorLayout;
    
    protected BaseListAdapter<T> mAdapter;
    
    protected boolean mIsRefresh;
    
    protected TextHttpResponseHandler mHandler;
    
//    protected PageBean<T> mBean;
    
    private String mTime;
    
    private View mFooterView;
    
    private ProgressBar mFooterProgressBar;
    
    private TextView mFooterText;
    
    protected int mCurrentPage = 0;
    
    
    
    @Override
    protected void initBundle(Bundle bundle) { 
        // TODO Auto-generated method stub
        super.initBundle(bundle);
    }
    
    protected String getCacheKeyPrefix() {
        return null;
    }
    
    private String getCacheKey() {
        return getCacheKeyPrefix() + "_" + mCurrentPage;
    }
    
    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_base_list;
    }
    
    @Override
    protected void setupViews(View root) {
        // TODO Auto-generated method stub
        super.setupViews(root);
        mListView = (ListView) root.findViewById(R.id.listView);
        mRefreshLayout = (SuperRefreshLayout) root.findViewById(R.id.superRefreshLayout);
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
        mRefreshLayout.setSuperRefreshLayoutListener(this);
        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_view_footer, null);
        mFooterText = (TextView) mFooterView.findViewById(R.id.tv_footer);
        mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_footer);
        mListView.setOnItemClickListener(this);
        setFooterType(TYPE_LOADING);
        mErrorLayout.setOnLayoutClickListener(this);
        if (isNeedFooter()){
            mListView.addFooterView(mFooterView);
        }
    }
    
    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        super.initData();
        //when open this fragment,read the obj
        TLog.d("called");
        mAdapter = getListAdapter();
        mListView.setAdapter(mAdapter);

        mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            	TLog.d("called");
                onRequestError(statusCode);
                onRequestFinish();
            } 

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                TLog.d("called");
                try {
                    ResultBean<NewsListBean<T>> resultBean = AppContext.createGson().fromJson(responseString, getType());
                    if (resultBean != null  && resultBean.getResult().getItems() != null) {
                        onRequestSuccess(1);
                        setListData(resultBean.getResult());
                    } else {
                        setFooterType(TYPE_NO_MORE);
                    }
                    onRequestFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        };

//        AppOperator.runOnThread(new Runnable() {
//            @Override
//            public void run() {
//                mBean = (PageBean<T>) CacheManager.readObject(getActivity(), CACHE_NAME);
//                //if is the first loading
//                if (mBean == null) {
//                    mBean = new PageBean<T>();
//                    mBean.setItems(new ArrayList<T>());
//                    onRefreshing();
//                } else {
//                    if (mRoot != null) {
//                        mRoot.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mAdapter.addItem(mBean.getItems());
//                                mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
//                                mRefreshLayout.setVisibility(View.VISIBLE);
//                                onRefreshing();
//                            }
//                        });
//                    }
//                }
//            }
//        });
    }

    public void show(){
        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                TLog.d("CACHE_KEY = " + getCacheKey());
                final NewsListBean<T> bean = (NewsListBean<T>) CacheManager.readObject(getActivity(), getCacheKey());
                //if is the first loading
                if (bean == null) {
                    onRefreshing();
                } else {
                    mRoot.post(new Runnable() {
                        @Override 
                        public void run() {
                            setListData(bean);
                        }
                    }); 
                }
            }
        });    
    }
    
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        onRefreshing();
    }

    @Override
    public void onRefreshing() {
        // TODO Auto-generated method stub
    	TLog.d("called");
        mIsRefresh = true;
        requestData();
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
    	TLog.d("called");
        requestData();
    }
    
    /**
     * request network data
     */
    protected void requestData() {
    	TLog.d("called");
        onRequestStart();
        setFooterType(TYPE_LOADING);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        
    }
    
    protected void onRequestStart() {

    }
    
    protected void onRequestSuccess(int code) {

    }
    
    /**
     * save readed list
     *
     * @param fileName fileName
     * @param key      key
     */
    protected void saveToReadedList(String fileName, String key) {

        // 放入已读列表
        AppContext.putReadedPostList(fileName, key, "true");
    }
    

    /**
     * update textColor
     *
     * @param title   title
     * @param content content
     */
    protected void updateTextColor(TextView title, TextView content) {
        if (title != null) {
            title.setTextColor(getResources().getColor(R.color.count_text_color_light));
        }
        if (content != null) {
            content.setTextColor(getResources().getColor(R.color.count_text_color_light));
        }
    }
    
    protected void onRequestError(int code) {
        setFooterType(TYPE_NET_ERROR);
        if (mAdapter.getDatas().size() == 0)
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }
    
    protected void onRequestFinish() {
        onComplete();
    }
    
    protected void onComplete() {
        mRefreshLayout.onLoadComplete();
        mIsRefresh = false;
    }
    
    protected void setListData(final NewsListBean<T> pageBean) {
        //is refresh
        if (mIsRefresh) {
            mAdapter.clear();
            mAdapter.addItem(pageBean.getItems());
            mRefreshLayout.setCanLoadMore();
            AppOperator.runOnThread(new Runnable() {
                @Override
                public void run() {
                    CacheManager.saveObject(getActivity(), pageBean, getCacheKey());
                }
            });
        } else {
            mAdapter.addItem(pageBean.getItems());
        }
        if (pageBean.getItems().size() < 20) {
            setFooterType(TYPE_NO_MORE);
            //mRefreshLayout.setNoMoreData();
        }
        if (mAdapter.getDatas().size() > 0) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
        }
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
    }
    
    @Override
    public Date getSystemTime() {
        return new Date();
    }
    
    protected abstract BaseListAdapter<T> getListAdapter();

    protected abstract Type getType();

    protected boolean isNeedFooter() {
        return true;
    }

    protected void setFooterType(int type) {
        switch (type) {
            case TYPE_NORMAL:
            case TYPE_LOADING:
                mFooterText.setText(getResources().getString(R.string.footer_type_loading));
                mFooterProgressBar.setVisibility(View.VISIBLE);
                break;
            case TYPE_NET_ERROR:
                mFooterText.setText(getResources().getString(R.string.footer_type_net_error));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_ERROR:
                mFooterText.setText(getResources().getString(R.string.footer_type_error));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_NO_MORE:
                mFooterText.setText(getResources().getString(R.string.footer_type_not_more));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
        }
    }
}
