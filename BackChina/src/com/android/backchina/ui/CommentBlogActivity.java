package com.android.backchina.ui;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.adapter.CommentBlogAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Blog;
import com.android.backchina.bean.BlogDetail;
import com.android.backchina.bean.Comment;
import com.android.backchina.bean.StatusBean;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.bean.base.BlogCommentBean;
import com.android.backchina.bean.base.CommentBean;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.TLog;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class CommentBlogActivity extends BaseCommentsActivity<BlogCommentBean>{

	public final static String BUNDLE_KEY_BLOG = "BUNDLE_KEY_BLOG";
	
	private Blog mCurrentBlog;
    
	private BlogCommentBean currentClickComment;
	
	private TextHttpResponseHandler mHandler;
	
	private int mCurrentPage = 1;
	
	private int mReferFloor = 0;
	
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
		mListView.setAutoLoadEnable(false);
	}

	@Override
	protected void initData() {
		mReferFloor = 0;
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
							setListData(blogDetail.getBlogcomments(), true);
						} else {
							if(blogDetail.getBlogcomments().size() == 0){
								onRequestSuccess();
								loadMoreNodata();
								mCurrentPage = mCurrentPage -1;
								return;
							} else {
								setListData(blogDetail.getBlogcomments(),false);
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
		return new CommentBlogAdapter(this);
	}
	
	private void setListData(List<BlogCommentBean> mComments,boolean isrefresh){
		if (isrefresh) {
			mAdapter.clear();
			mAdapter.addItem(mComments);
		} else {
			mAdapter.addItem(mComments);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		mReferFloor = position + 1;
		currentClickComment = (BlogCommentBean) parent.getAdapter().getItem(position);
    	String result = getResources().getString(R.string.comments_hint_text);
		if (currentClickComment != null) {
			String hintString = getResources().getString(
					R.string.comments_hint_refer_text);
			result = String.format(hintString,position + 1);
		}
		handleInputComment(result);
	}
	
	@Override
	protected void onHandleSendComment(String content) {
		// TODO Auto-generated method stub
		mWaitDialog.show();
		int cid = 0;
		int position = 0;
		if (currentClickComment != null) {
			cid = currentClickComment.getCid();
			position = mReferFloor;
		}
		BackChinaApi.sendBlogComment(mCurrentBlog.getId(),cid,position,content,new TextHttpResponseHandler() {

	            @Override
	            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
	                // TODO Auto-generated method stub
	                TLog.d("called");
	                mWaitDialog.hide();
	                Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
	                currentClickComment = null;
	            }

	            @Override
	            public void onSuccess(int statusCode, Header[] headers, String responseString) {
	                // TODO Auto-generated method stub
	                TLog.d("called");
	                handleCommentResponse(headers,responseString);
	                mWaitDialog.hide();
	                currentClickComment = null;
	            }
	        });
	}
	
	private void handleCommentResponse(Header[] headers,String response) {
    	Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
        }.getType();
        ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson().fromJson(response, type);
        StatusBean statusBean = activitiesBean.getActivities();
        if (statusBean.getStatus().equals("1")) {
        	Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
        	onRequestData();
        }else if (statusBean.getStatus().equals("-1")) {
        	Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
        }else if (statusBean.getStatus().equals("-2")) {
        	Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
        }else{
        	Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
        }
    }
}
