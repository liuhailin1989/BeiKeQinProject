package com.android.backchina.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.backchina.R;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.fragment.DetailFragment;
import com.android.backchina.fragment.NewsDetailFragment;
import com.android.backchina.interf.IContractDetail;
import com.android.backchina.interf.OperatorCallBack;
import com.android.backchina.ui.empty.EmptyLayout;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public abstract class BaseDetailActivity extends BaseActivity implements IContractDetail{

    private EmptyLayout mEmptyLayout;
    
    protected LinearLayout layTitleBar;
    
    private ImageView btnBack;
    
    private TextView tvTitle;
    
    protected TextView tvCommentCount;
    
    protected TextHttpResponseHandler mHandler;
    
    protected TextHttpResponseHandler mFavoriteHandler;
    
    protected TextHttpResponseHandler mCancleFavoriteHandler;
    
    protected OperatorCallBack operatorCallBack = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initBundle(getIntent().getExtras());
        setContentView(R.layout.activity_detail_layout);
        setupViews();
        initData();
	}
	
	@Override
	public void setOperatorCallBack(OperatorCallBack callback) {
		// TODO Auto-generated method stub
		operatorCallBack = callback;
	}
	
	protected void initBundle(Bundle bundle){
		 
	 }
	
	protected void setupViews(){
        mEmptyLayout = (EmptyLayout) findViewById(R.id.lay_error);
        layTitleBar = (LinearLayout) findViewById(R.id.title_bar);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvCommentCount = (TextView) findViewById(R.id.tv_comment_count);
        tvCommentCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(operatorCallBack != null){
					operatorCallBack.scrollToCommentsLocation();
				}
			}
		});
	}
	
	protected void setTitle(String title) {
		if (tvTitle != null) {
			tvTitle.setText(title);
		}
	}
	
	protected void setCommentCount(int count){
		if (tvCommentCount != null) {
			String temp = getResources().getString(R.string.detial_title_bar_count);
			String value = String.format(temp, String.valueOf(count));
			if (count > 99) {
				value = String.format(temp, "99+");
			}
			tvCommentCount.setText(value);
		}
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
    	mFavoriteHandler = new TextHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString,
					Throwable throwable) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				// TODO Auto-generated method stub
				handleFavoriteResp(responseString);
			}
        	
        };
        mCancleFavoriteHandler = new TextHttpResponseHandler(){

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString,
					Throwable throwable) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				// TODO Auto-generated method stub
				handleCancleFavoriteResp(responseString);
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
    
    protected void handleFavoriteResp(String responseString){
    	
    }
    
    protected void handleCancleFavoriteResp(String responseString){
    	
    }
    
    public Fragment getDataViewFragment(){
        return null;
    }
    
    protected void showFavoriteFailed(){
    	Toast.makeText(getContext(), "收藏失败", Toast.LENGTH_SHORT).show();
    }
    protected void showFavoriteSucessed(){
    	Toast.makeText(getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
    }
    protected void showHasFavorited(){
    	Toast.makeText(getContext(), "已收藏", Toast.LENGTH_SHORT).show();
    }
    protected void showCancleFavoriteSucessed(){
    	Toast.makeText(getContext(), "取消收藏", Toast.LENGTH_SHORT).show();
    }
    protected void showCancleFavoriteFailed(){
    	Toast.makeText(getContext(), "取消收藏失败", Toast.LENGTH_SHORT).show();
    }
	protected void needLogin() {
		Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
	}
    
    public abstract void onRequestData();
}
