package com.android.backchina.ui;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.backchina.AppContext;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.bean.Blog;
import com.android.backchina.bean.BlogDetail;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.fragment.BlogDetailFragment;
import com.android.backchina.fragment.DetailFragment;
import com.google.gson.reflect.TypeToken;

public class BlogDetailActivity extends BaseDetailActivity {

    private final static String BUNDLE_KEY_BLOG = "BUNDLE_KEY_BLOG";
    
    private Blog mCurrentBlog;
    
    private BlogDetail mBlogDetail;
    
	public static void show(Context context, Blog blog) {
		Intent intent = new Intent(context, BlogDetailActivity.class);
		intent.putExtra(BUNDLE_KEY_BLOG, blog);
		context.startActivity(intent);
	}

	@Override
	protected void initBundle(Bundle bundle) {
		// TODO Auto-generated method stub
		mCurrentBlog = (Blog) bundle.getSerializable(BUNDLE_KEY_BLOG);
	}
	
	@Override
	protected void setupViews() {
		// TODO Auto-generated method stub
		super.setupViews();
		setTitle("博文详情");
		setCommentCount(mCurrentBlog.getComments());
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
	}
	
	@Override
	public void onRequestData() {
		// TODO Auto-generated method stub
		 BackChinaApi.getNewsDetail(mCurrentBlog.getUrlapi(), mHandler);
	}
	
    private Type getType(){
        return new TypeToken<ResultListBean<BlogDetail>>() {}.getType();
    }
	
	@Override
    public boolean handleData(String responseString) {
        try {
        ResultListBean<BlogDetail> bean = AppContext.createGson().fromJson(responseString, getType());
        mBlogDetail = bean.getItems().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
	@Override
    public Fragment getDataViewFragment(){
        return BlogDetailFragment.newInstance();
    }

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return mBlogDetail;
	}

	@Override
	public void hideLoading() {
		// TODO Auto-generated method stub
		onRequestDataSuccess();
	}

	@Override
	public void toFavorite() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toShare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toSendComment(String comment) {
		// TODO Auto-generated method stub
		
	}
	
}
