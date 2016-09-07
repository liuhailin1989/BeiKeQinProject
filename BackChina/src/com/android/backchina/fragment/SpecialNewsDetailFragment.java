package com.android.backchina.fragment;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.backchina.R;
import com.android.backchina.bean.Comment;
import com.android.backchina.bean.SpecialNewsDetail;
import com.android.backchina.ui.comment.CommentsView;
import com.android.backchina.ui.comment.OnCommentClickListener;
import com.android.backchina.utils.StringUtils;

public class SpecialNewsDetailFragment extends DetailFragment<Object> implements OnCommentClickListener{

    private TextView mTitle;
    private TextView mPubTime;
    private TextView mAuthor;
    
    private TextView mInput;
    
    private CommentsView mComments;
    
    private LinearLayout layCommit;
    
    private RelativeLayout layRealComentEdit;
    
    private EditText mCommentEditView;
    
    private TextView mCommentsCount;
    
    private Button mBtnSend;
    
    private ImageView mBlogerCardAvatar;
    
    private TextView mBlogerCardAuthor;
    
    private TextView mBlogerCardSubscribe;
    
	public static SpecialNewsDetailFragment newInstance() {
		SpecialNewsDetailFragment fragment = new SpecialNewsDetailFragment();
		return fragment;
	}
    
    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_blog_detail_layout;
    }
    
    @Override
    protected void setupViews(View root) {
        // TODO Auto-generated method stub
        super.setupViews(root);
        
        mTitle = (TextView) root.findViewById(R.id.tv_title);
        mPubTime = (TextView) root.findViewById(R.id.tv_pub_date);
        mAuthor = (TextView) root.findViewById(R.id.tv_author);
        
        mComments = (CommentsView) root.findViewById(R.id.lay_comment_view);
        
        layCommit = (LinearLayout) root.findViewById(R.id.lay_commit_edit);
        layCommit.setVisibility(View.VISIBLE);
        mCommentsCount = (TextView) root.findViewById(R.id.tv_commit_count);
        
        layRealComentEdit = (RelativeLayout) root.findViewById(R.id.lay_real_comment_edit);
        layRealComentEdit.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hideCommentView();
            }
        });
        layRealComentEdit.setVisibility(View.GONE);
        
        mCommentEditView = (EditText) root.findViewById(R.id.et_comment);
        mCommentEditView.setOnEditorActionListener(new OnEditorActionListener() {
            
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        
        //
        mBlogerCardAvatar = (ImageView) root.findViewById(R.id.iv_card_avatar);
        mBlogerCardAuthor = (TextView) root.findViewById(R.id.tv_card_author);
        mInput = (TextView) root.findViewById(R.id.tv_put);
        
        mInput.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                layCommit.setVisibility(View.GONE);
                layRealComentEdit.setVisibility(View.VISIBLE);
                mCommentEditView.setFocusable(true);  
                mCommentEditView.setFocusableInTouchMode(true);  
                mCommentEditView.requestFocus();
                showSoftInput(mCommentEditView);
            }
        });
        
        mBtnSend = (Button) root.findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				handleSendComment();
				hideCommentView();
			}
		});
    }
    
    public void showSoftInput(View input) {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
    }
    
    public void hideCommentView() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mCommentEditView.getWindowToken(), 0);  
        layRealComentEdit.setVisibility(View.GONE);
        layCommit.setVisibility(View.VISIBLE);
    }
    
    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        super.initData();
        SpecialNewsDetail specialNewsDetail = (SpecialNewsDetail) iDetail.getData();
        if(specialNewsDetail == null){
            return;
        }
        setWebViewContent(specialNewsDetail.getContent());
        mTitle.setText(specialNewsDetail.getTitle());
        mPubTime.setText(StringUtils.friendlyTime(specialNewsDetail.getDateline()));
        mAuthor.setText(specialNewsDetail.getUsername());
        mComments.setTitle("最新评论");
        mComments.init(specialNewsDetail.getCommurlapi(), 0, specialNewsDetail.getComments(), null, this);
        mCommentsCount.setText(String.valueOf(specialNewsDetail.getComments()));
        setImageFromNet(mBlogerCardAvatar, specialNewsDetail.getAvatar(),R.drawable.default_avatar);
        mBlogerCardAuthor.setText(specialNewsDetail.getUsername());
    }

    @Override
    public void onClick(View view, Comment comment) {
        // TODO Auto-generated method stub
        
    }
    
    private void handleSendComment() {
        iDetail.toSendComment(mCommentEditView.getText().toString());
    }
}