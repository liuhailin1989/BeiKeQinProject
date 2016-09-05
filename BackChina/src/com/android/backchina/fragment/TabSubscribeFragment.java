package com.android.backchina.fragment;

import java.lang.reflect.Type;
import java.util.Date;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.R;
import com.android.backchina.adapter.SubscribeAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseFragment;
import com.android.backchina.base.adapter.BaseListAdapter.Callback;
import com.android.backchina.bean.Subscribe;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class TabSubscribeFragment extends BaseFragment<Subscribe> implements OnTabReselectListener,Callback,OnItemClickListener{

    private ListView mListView;
    
    private View mHeadView;
    
    private View mFooterView;
    
    private LinearLayout mHeadViewAddSubscribe;
    
    private SubscribeAdapter mAdapter;
    
    protected TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {
        
        @Override
        public void onSuccess(int code, Header[] headers, String responseString) {
            // TODO Auto-generated method stub
            Type type = new TypeToken<ResultListBean<Subscribe>>() {}.getType();
            ResultListBean<Subscribe> resultListBean = AppContext.createGson().fromJson(responseString, type);
            if(resultListBean != null && resultListBean.getItems() != null){
                setDataList(resultListBean);
            }
        }
        
        @Override
        public void onFailure(int code, Header[] headers, String responseString, Throwable throwable) {
            // TODO Auto-generated method stub
            TLog.d("called");
        }
    };
    
    private String getCacheKey(){
        return "subscribe_list";
    }
    
    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_subscribe_layout;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        // TODO Auto-generated method stub
        super.initBundle(bundle);
    }
    
    @Override
    protected void setupViews(View root) {
        // TODO Auto-generated method stub
        super.setupViews(root);
        
        mListView = (ListView) root.findViewById(R.id.lv_subscribe);
        
        mHeadView = View.inflate(getActivity(), R.layout.layout_subscribe_header, null);
        mFooterView = View.inflate(getActivity(), R.layout.layout_subscribe_footer, null);
        mHeadViewAddSubscribe = (LinearLayout) mHeadView.findViewById(R.id.ll_subscribe_header_container);
        mHeadViewAddSubscribe.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.enterSubscribeActivity(getActivity());
            }
        });
        mListView.setOnItemClickListener(this);
        mListView.addHeaderView(mHeadView);
        mListView.addFooterView(mFooterView);
    }
    
    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        super.initData();
        mAdapter = new SubscribeAdapter(this);
        mListView.setAdapter(mAdapter);
        requestData();
    }

    
    private void requestData(){
        BackChinaApi.getHotSubscribeList(mHandler);
    }
    
    private void setDataList(final ResultListBean<Subscribe> resultListBean){
        mAdapter.clear();
        mAdapter.addItem(resultListBean.getItems());
        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                CacheManager.saveObject(getActivity(), resultListBean, getCacheKey());
            }
        });
    }
    
    @Override
    public Date getSystemTime() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onTabReselect() {
        // TODO Auto-generated method stub
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Subscribe currentSubscribe = (Subscribe) parent.getAdapter().getItem(position);//减去header view count
		UIHelper.enterSubscribeDetailActivity(getActivity(), currentSubscribe);
	}
    
}
