package com.android.backchina.ui;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.backchina.AppContext;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Blog;
import com.android.backchina.bean.BlogDetail;
import com.android.backchina.bean.Comment;
import com.android.backchina.bean.base.BlogCommentBean;
import com.android.backchina.bean.base.CommentBean;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.ui.empty.EmptyLayout;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class CommentBlogActivity extends BaseCommentsActivity<BlogCommentBean>{

	public final static String BUNDLE_KEY_BLOG = "BUNDLE_KEY_BLOG";
	
	private Blog mCurrentBlog;
    
	private BlogCommentBean currentClickComment;
	
	private TextHttpResponseHandler mHandler;
	
	private int mCurrentPage = 1;
	
	private boolean isLoadMoreAction = false;
	
	public static void show(Context context,Blog blog) {
        Intent intent = new Intent(context, CommentBlogActivity.class);
        intent.putExtra(BUNDLE_KEY_BLOG, blog);
        context.startActivity(intent);
    }
	
	
	protected void initBundle(Bundle bundle) {
		mCurrentBlog = (Blog) bundle.getSerializable(BUNDLE_KEY_BLOG);
	}
	
	@Override
	protected void setupViews() {
		super.setupViews();
		setTitle("评论列表");
	}

	@Override
	protected void initData() {
		mCurrentPage = 1;
		isLoadMoreAction = false;
		//
		mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				if (isLoadMoreAction) {
					loadMoreNodata();
				} else {
					onRequestError(EmptyLayout.NODATA);
				}
				isLoadMoreAction = false;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                        Type type = new TypeToken<ResultListBean<BlogDetail<BlogCommentBean>>>() {
                    }.getType();
                        ResultListBean<BlogDetail<BlogCommentBean>> bean = AppContext.createGson().fromJson(responseString, type);
                        BlogDetail blogDetail = bean.getItems().get(0);
					if (blogDetail != null) {
						if (mCurrentPage == 1) {
//							setListData(blogDetail.getBlogcomments(), true);
						} else {
							if(blogDetail.getBlogcomments().size() == 0){
								onRequestSuccess();
								loadMoreNodata();
								mCurrentPage = mCurrentPage -1;
								return;
							} else {
//								setListData(blogDetail.getBlogcomments(),false);
							}
						}
						onRequestSuccess();
						stopLoadMore();
					}else{
                    onFailure(statusCode, headers, responseString, null);
					}
					isLoadMoreAction = false;
                } catch (Exception e) {
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        };
        super.initData();
	}
	
	@Override
	protected void onRequestData() {
		mCurrentPage = 1;
		 BackChinaApi.getBlogDetail(mCurrentBlog.getUrlapi(), mHandler);
	}
	
	@Override
	public void onLoadMore() {
//		mCurrentPage = mCurrentPage + 1;
//		isLoadMoreAction = true;
//		BackChinaApi.getBlogDetail(mCurrentBlog.getUrlapi(), mHandler);
	}
	
	@Override
	protected BaseListAdapter<BlogCommentBean> getListAdapter() {
		// TODO Auto-generated method stub
		return null;
	}
}
