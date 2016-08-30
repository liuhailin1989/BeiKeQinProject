package com.android.backchina.fragment;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.backchina.R;
import com.android.backchina.bean.Comment;
import com.android.backchina.bean.NewsDetail;
import com.android.backchina.ui.comment.CommentsView;
import com.android.backchina.ui.comment.OnCommentClickListener;
import com.android.backchina.utils.StringUtils;

public class NewsDetailFragment<T> extends DetailFragment<Object> implements OnCommentClickListener{

    private TextView mTitle;
    private TextView mPubTime;
    private TextView mFrom;
    
    private EditText mEditInput;
    
    private CommentsView mComments;
    
    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_news_detail_layout;
    }
    
    @Override
    protected void setupViews(View root) {
        // TODO Auto-generated method stub
        super.setupViews(root);
        
        mTitle = (TextView) root.findViewById(R.id.tv_title);
        mPubTime = (TextView) root.findViewById(R.id.tv_pub_date);
        mFrom = (TextView) root.findViewById(R.id.tv_from);
        
        mComments = (CommentsView) root.findViewById(R.id.lay_comment_view);
        
        mEditInput = (EditText) root.findViewById(R.id.et_put);
        
        mEditInput.setOnEditorActionListener(new OnEditorActionListener() {
            
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    handleSendComment();
                    return true;
                }
                return false;
            }
        });
    }
    
    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        super.initData();
        NewsDetail newsDetail = (NewsDetail) iDetail.getData();
        if(newsDetail == null){
            return;
        }
        setWebViewContent(newsDetail.getContent());
        mTitle.setText(newsDetail.getTitle());
        mPubTime.setText(StringUtils.friendlyTime(newsDetail.getDateline()));
        mFrom.setText(newsDetail.getFrom());
        
        mComments.setTitle("最新评论");
        mComments.init(newsDetail.getCommurlapi(), 0, newsDetail.getComments(), null, this);
    }

    @Override
    public void onClick(View view, Comment comment) {
        // TODO Auto-generated method stub
        
    }
    
    private void handleSendComment() {
        iDetail.toSendComment(mEditInput.getText().toString());
    }
}
