package com.news.soft.backchina.ui.comment;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.R;
import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.bean.Comment;
import com.news.soft.backchina.bean.News;
import com.news.soft.backchina.bean.base.BlogCommentBean;
import com.news.soft.backchina.bean.base.CommentBean;
import com.news.soft.backchina.ui.CommentNewsActivity;
import com.news.soft.backchina.utils.StringUtils;
import com.news.soft.backchina.utils.TLog;
import com.bumptech.glide.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class CommentsView extends LinearLayout implements View.OnClickListener {
    private int mType;
    private TextView mTitle;
    private TextView mSeeMore;
    private LinearLayout mLayComments;

    public CommentsView(Context context) {
        super(context);
        init();
    }

    public CommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommentsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_detail_comment, this, true);

        mTitle = (TextView) findViewById(R.id.tv_comment);
        mLayComments = (LinearLayout) findViewById(R.id.lay_comment_container);
        mSeeMore = (TextView) findViewById(R.id.tv_more_comment);
    }

    public void setTitle(String title) {
        if (!StringUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
    }

    public void init(String url, int type, final int commentTotal, final RequestManager imageLoader, final OnCommentOpsListener onCommentClickListener) {
        this.mType = type;

        mSeeMore.setVisibility(View.GONE);
        setVisibility(GONE);
        if(url == null){
        	return;
        }
        BackChinaApi.getComments(url, 1,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (throwable != null)
                    throwable.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                        Type type = new TypeToken<CommentBean<List<Comment>>>() {
                    }.getType();

                    CommentBean<List<Comment>> resultBean = AppContext.createGson().fromJson(responseString, type);
                    if (resultBean != null) {
                        addComment(resultBean.getNewscomms().get(0), commentTotal, imageLoader, onCommentClickListener);
                        return;
                    }
                    onFailure(statusCode, headers, responseString, null);
                } catch (Exception e) {
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        });
    }
    
    public void refreshComments(String url,int type,final int commentTotal,final RequestManager imageLoader, final OnCommentOpsListener onCommentOpsListener){
        this.mType = type;
        mLayComments.removeAllViews();
        mSeeMore.setVisibility(View.GONE);
        setVisibility(GONE);
        if(url == null){
        	return;
        }
        BackChinaApi.getComments(url,1, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (throwable != null)
                    throwable.printStackTrace();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                        Type type = new TypeToken<CommentBean<List<Comment>>>() {
                    }.getType();

                    CommentBean<List<Comment>> resultBean = AppContext.createGson().fromJson(responseString, type);
                    if (resultBean != null) {
                        addComment(resultBean.getNewscomms().get(0), commentTotal, imageLoader, onCommentOpsListener);
                        return;
                    }
                    onFailure(statusCode, headers, responseString, null);
                } catch (Exception e) {
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        });
    }
    
    private void addComment(List<Comment> comments, int commentTotal, RequestManager imageLoader, final OnCommentOpsListener onCommentOpsListener) {
        if (comments != null && comments.size() > 0) {
            if (comments.size() > commentTotal) {
                mSeeMore.setVisibility(VISIBLE);
                mSeeMore.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (onCommentOpsListener != null) {
							onCommentOpsListener.seeMoreComments(v);
						}
					}
				});
            }

            if (getVisibility() != VISIBLE) {
                setVisibility(VISIBLE);
            }
            int count = comments.size() > commentTotal ? commentTotal:comments.size();
            for (int i = 0 ;i < count ; i++) {
            	Comment comment = comments.get(i);
                if (comment == null || comment.getId() == 0){
                    continue;
                }
                ViewGroup lay = addComment(false, comment, imageLoader, onCommentOpsListener);
            }
            if (onCommentOpsListener != null) {
            	if(mType == News.TYPE_NEWS_LOCAL){
            		onCommentOpsListener.refreshCommentsCount(comments.size());
				} else {
					onCommentOpsListener.refreshCommentsCount(comments.get(0).getPosition() - 1);
				}
            }
        } else {
            setVisibility(View.GONE);
            if (onCommentOpsListener != null) {
            	onCommentOpsListener.refreshCommentsCount(0);
            }
        }
    }

    public ViewGroup addComment(final Comment comment, RequestManager imageLoader, final OnCommentOpsListener onCommentClickListener) {
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
        return addComment(true, comment, imageLoader, onCommentClickListener);
    }

    private ViewGroup addComment(boolean first, final Comment comment, RequestManager imageLoader, final OnCommentOpsListener onCommentOpsListener) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup lay = (ViewGroup) inflater.inflate(R.layout.layout_comment_item, null, false);
//        imageLoader.load(comment.getAuthorPortrait()).error(R.mipmap.widget_dface)
//                .into(((ImageView) lay.findViewById(R.id.iv_avatar)));

		if (StringUtils.isEmpty(comment.getUsername())) {
			((TextView) lay.findViewById(R.id.tv_username)).setText("游客");
		} else {
			((TextView) lay.findViewById(R.id.tv_username)).setText(comment
					.getUsername());
		}
		
		TextView floor = (TextView) lay.findViewById(R.id.tv_floor);
		String floorText = String.format(getContext().getResources().getString(R.string.comments_floor_count),comment.getPosition());
		floor.setText(floorText);

		TextView refer = (TextView) lay.findViewById(R.id.tv_refer);
		String msg = comment.getMessage();
		
		String referMsg = CommentsUtil.getReferComments(msg);
		if(referMsg != null){
			refer.setVisibility(View.VISIBLE);
			refer.setText(referMsg);
		}else{
			refer.setVisibility(View.GONE);
		}
		
		TextView content = ((TextView) lay.findViewById(R.id.tv_message));
		String commentsMsg = CommentsUtil.getCommentsMessages(msg);
        content.setText(commentsMsg);
//        CommentsUtil.formatHtml(getResources(), content, comment.getMessage());

//        if (comment.getRefer() != null) {
//            // 最多5层
//            View view = CommentsUtil.getReferLayout(inflater, comment.getRefer(), 5);
//            lay.addView(view, lay.indexOfChild(content));
//        }

        ((TextView) lay.findViewById(R.id.tv_pub_date)).setText(StringUtils.friendly_time(comment.getDateline()));

        lay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				if (onCommentOpsListener != null) {
					onCommentOpsListener.onClick(v, comment);
				}
            }
        });

        if (first){
            mLayComments.addView(lay, 0);
        }
        else{
            mLayComments.addView(lay);
        }
        return lay;
    }

    @Override
    public void onClick(View v) {

    	
    }
}
