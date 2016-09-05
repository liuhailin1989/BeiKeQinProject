package com.android.backchina.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.backchina.R;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.fragment.DetailFragment;
import com.android.backchina.fragment.NewsDetailFragment;
import com.android.backchina.interf.IContractDetail;
import com.android.backchina.ui.empty.EmptyLayout;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public abstract class BaseDetailActivity extends BaseActivity implements IContractDetail{

    private EmptyLayout mEmptyLayout;
    
    private ImageView btnBack;
    
    private TextView tvCommentCount;
    
    protected TextHttpResponseHandler mHandler;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initBundle(getIntent().getExtras());
        setContentView(R.layout.activity_detail_layout);
        setupViews();
        initData();
	}
	
	protected void initBundle(Bundle bundle){
		 
	 }
	
	protected void setupViews(){
        mEmptyLayout = (EmptyLayout) findViewById(R.id.lay_error);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        tvCommentCount = (TextView) findViewById(R.id.tv_comment_count);
        int commentCount = getCommentCount();
        tvCommentCount.setText(String.valueOf(commentCount));
	}
	
	protected void initData() {
    	mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                throwable.printStackTrace();
                onRequestDataFailed(EmptyLayout.NETWORK_ERROR);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (!handleData(responseString)) {
                    onRequestDataFailed(EmptyLayout.NODATA);
                }else{
                	handleView();
                	onRequestDataSuccess();
                }
            }
    	};
        requestData();
    }
    
    public void requestData(){
        if (mEmptyLayout != null) {
        	mEmptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        }
    	onRequestData();
    }
    
    public void onRequestDataSuccess(){
        if (mEmptyLayout != null) {
        	mEmptyLayout.dismiss();
        }
    }
    
    public void onRequestDataFailed(int type){
        if (mEmptyLayout != null) {
        	mEmptyLayout.setErrorType(type);
        }
    }
    
    public int getCommentCount(){
    	return 0;
    }
    
    public boolean handleData(String responseString){
    	return false;
    }
    private void handleView() {
        try {
        	Fragment fragment = getDataViewFragment();
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.lay_container, fragment);
            trans.commitAllowingStateLoss();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Fragment getDataViewFragment(){
        return null;
    }
    
    public abstract void onRequestData();
}
