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
import com.android.backchina.adapter.CommentNewsAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Comment;
import com.android.backchina.bean.NewsDetail;
import com.android.backchina.bean.StatusBean;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.bean.base.CommentBean;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.TLog;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class CommentNewsActivity extends BaseCommentsActivity<Comment>{

	public final static String BUNDLE_KEY_NEWS_DETAIL = "BUNDLE_KEY_NEWS_DETAIL";
	
	private NewsDetail mCurrentNewsDetail;
	
	private Comment currentClickComment;
	
	private TextHttpResponseHandler mHandler;
	
	private int mCurrentPage = 1;
	
	private boolean isLoadMoreAction = false;
	
	public static void show(Context context,NewsDetail newsDetail) {
        Intent intent = new Intent(context, CommentNewsActivity.class);
        intent.putExtra(BUNDLE_KEY_NEWS_DETAIL, newsDetail);
        context.startActivity(intent);
    }
	
	protected void initBundle(Bundle bundle) {
		mCurrentNewsDetail = (NewsDetail) bundle
				.getSerializable(BUNDLE_KEY_NEWS_DETAIL);
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
                        Type type = new TypeToken<CommentBean<List<Comment>>>() {
                    }.getType();

                    CommentBean<List<Comment>> resultBean = AppContext.createGson().fromJson(responseString, type);
					if (resultBean != null) {
						if (mCurrentPage == 1) {
							setListData(resultBean.getNewscomms().get(0), true);
						} else {
							if(resultBean.getNewscomms().get(0).size() == 0){
								onRequestSuccess();
								loadMoreNodata();
								mCurrentPage = mCurrentPage -1;
								return;
							} else {
								setListData(resultBean.getNewscomms().get(0),false);
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
		BackChinaApi.getComments(mCurrentNewsDetail.getCommurlapi(), mCurrentPage,mHandler);
	}
	
	@Override
	public void onLoadMore() {
		mCurrentPage = mCurrentPage + 1;
		isLoadMoreAction = true;
		BackChinaApi.getComments(mCurrentNewsDetail.getCommurlapi(),mCurrentPage, mHandler);
	}
	
	private void setListData(List<Comment> mComments,boolean isrefresh){
		if (isrefresh) {
			mAdapter.clear();
			mAdapter.addItem(mComments);
		} else {
			mAdapter.addItem(mComments);
		}
	}

	@Override
	protected BaseListAdapter<Comment> getListAdapter() {
		// TODO Auto-generated method stub
		return new CommentNewsAdapter(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		currentClickComment = (Comment) parent.getAdapter().getItem(position);
    	String result = getResources().getString(R.string.comments_hint_text);
		if (currentClickComment != null) {
			String hintString = getResources().getString(
					R.string.comments_hint_refer_text);
			result = String.format(hintString,
					currentClickComment.getPosition());
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
			position = currentClickComment.getPosition();
		}
		BackChinaApi.sendNewsComment(mCurrentNewsDetail.getId(),cid,position,"回帖",content,new TextHttpResponseHandler() {

	            @Override
	            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
	                // TODO Auto-generated method stub
	                TLog.d("called");
	                mWaitDialog.hide();
	                Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
	            }

	            @Override
	            public void onSuccess(int statusCode, Header[] headers, String responseString) {
	                // TODO Auto-generated method stub
	                TLog.d("called");
	                handleCommentResponse(headers,responseString);
	                mWaitDialog.hide();
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
