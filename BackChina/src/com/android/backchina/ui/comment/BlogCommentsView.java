package com.android.backchina.ui.comment;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.bean.Comment;
import com.android.backchina.bean.base.BlogCommentBean;
import com.android.backchina.bean.base.CommentBean;
import com.android.backchina.utils.StringUtils;
import com.bumptech.glide.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class BlogCommentsView extends LinearLayout implements View.OnClickListener {
    private int mType;
    private TextView mTitle;
    private TextView mSeeMore;
    private LinearLayout mLayComments;

    public BlogCommentsView(Context context) {
        super(context);
        init();
    }

    public BlogCommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlogCommentsView(Context context, AttributeSet attrs, int defStyle) {
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
    
    public void init(List<BlogCommentBean> listBlogCommentBean, int type, final int commentTotal, final RequestManager imageLoader, final OnBlogCommentClickListener onCommentClickListener) {
        this.mType = type;

        mSeeMore.setVisibility(View.GONE);
        setVisibility(GONE);
        if(listBlogCommentBean == null){
        	return;
        }
        addBlogComment(listBlogCommentBean, commentTotal, imageLoader, onCommentClickListener);
    }
    
    public void refreshComments(List<BlogCommentBean> listBlogCommentBean, int type, final int commentTotal, final RequestManager imageLoader, final OnBlogCommentClickListener onCommentClickListener){
        mLayComments.removeAllViews();
        init(listBlogCommentBean,type, commentTotal, imageLoader, onCommentClickListener);
    }

    private void addBlogComment(List<BlogCommentBean> listBlogCommentBean, int commentTotal, RequestManager imageLoader, final OnBlogCommentClickListener onCommentClickListener) {
        if (listBlogCommentBean != null && listBlogCommentBean.size() > 0) {
            if (listBlogCommentBean.size() > commentTotal) {
                mSeeMore.setVisibility(VISIBLE);
                mSeeMore.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (onCommentClickListener != null) {
							onCommentClickListener.seeMoreComments(v);
						}
					}
				});
            }

            if (getVisibility() != VISIBLE) {
                setVisibility(VISIBLE);
            }
            int count = listBlogCommentBean.size() > commentTotal ? commentTotal:listBlogCommentBean.size();
            for (int i = 0 ;i < count ; i++) {
            	BlogCommentBean comment = listBlogCommentBean.get(i);
                if (comment == null ){
                    continue;
                }
                ViewGroup lay = addComment(false,i+1, comment, imageLoader, onCommentClickListener);
            }
        } else {
            setVisibility(View.GONE);
        }
    }

    public ViewGroup addComment(int position, final BlogCommentBean comment, RequestManager imageLoader, final OnBlogCommentClickListener onCommentClickListener) {
        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }
        return addComment(true, position,comment, imageLoader, onCommentClickListener);
    }

    private ViewGroup addComment(boolean first,final int position, final BlogCommentBean comment, RequestManager imageLoader, final OnBlogCommentClickListener onCommentClickListener) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup lay = (ViewGroup) inflater.inflate(R.layout.layout_comment_item, null, false);
//        imageLoader.load(comment.getAuthorPortrait()).error(R.mipmap.widget_dface)
//                .into(((ImageView) lay.findViewById(R.id.iv_avatar)));

		((TextView) lay.findViewById(R.id.tv_username)).setText(comment.getAuthor());
		
		TextView floor = (TextView) lay.findViewById(R.id.tv_floor);
		String floorText = String.format(getContext().getResources().getString(R.string.comments_floor_count),position);
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

//        if (comment.getRefer() != null) {
//            // 最多5层
//            View view = CommentsUtil.getReferLayout(inflater, comment.getRefer(), 5);
//            lay.addView(view, lay.indexOfChild(content));
//        }

        ((TextView) lay.findViewById(R.id.tv_pub_date)).setText(StringUtils.friendly_time(comment.getDateline()));

        lay.setOnClickListener(new OnClickListener() {
            @Override
			public void onClick(View v) {
				if (onCommentClickListener != null) {
					onCommentClickListener.onClick(v, position,comment);
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
//        if (mId != 0 && mType != 0)
//            CommentsActivity.show(getContext(), mId, mType);
    }
}
