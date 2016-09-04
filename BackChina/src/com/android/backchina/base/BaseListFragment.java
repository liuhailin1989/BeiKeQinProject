package com.android.backchina.base;

import java.lang.reflect.Type;
import java.util.Date;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.TLog;
import com.android.backchina.widget.SuperRefreshLayout;

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
    
    private View mFooterView; //加载更多
    
    private ProgressBar mFooterProgressBar;//加载更多
    
    private TextView mFooterText;//加载更多
    
    
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
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        onRefreshing();
    }

    @Override
    public void onRefreshing() {
        // TODO Auto-generated method stub
    	TLog.d("called");
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
    private void requestData() {
    	TLog.d("called");
    	mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
    	onRequestData();
        setFooterType(TYPE_LOADING);
    }
    
    @Override
    protected void onHide() {
    	// TODO Auto-generated method stub
		if (mErrorLayout != null) {
			mErrorLayout.dismiss();
		}
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
    	TLog.d("called");
    }
    
    protected void onRequestData() {

    }
    
	protected void onRequestError(int code) {
		setFooterType(TYPE_NET_ERROR);
		mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
	}
    
    protected void onRequestSuccess() {
    	mRefreshLayout.onLoadComplete();
    	mErrorLayout.dismiss();
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
    
    @Override
    public Date getSystemTime() {
        return new Date();
    }
    
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
    
    protected abstract BaseListAdapter<T> getListAdapter();

    protected abstract Type getType();
}
