package com.android.backchina.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.backchina.AppConfig;
import com.android.backchina.R;
import com.android.backchina.bean.Comment;
import com.android.backchina.bean.NewsDetail;
import com.android.backchina.bean.SubscribeCat;
import com.android.backchina.ui.comment.CommentsView;
import com.android.backchina.ui.comment.OnCommentClickListener;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;

public class NewsDetailFragment<T> extends DetailFragment<Object> implements OnCommentClickListener{

    private TextView mTitle;
    private TextView mPubTime;
    private TextView mFrom;
    
    private TextView mInput;
    
    private CommentsView mComments;
    
    private LinearLayout layCommit;
    
    private LinearLayout layFontControlContainer;
    
    private RelativeLayout layRealComentEdit;
    
    private EditText mCommentEditView;
    
    private TextView mCommentsCount;
    
    private Button mBtnSend;
    
    private ImageView mBtnFontSize;
    
    private ImageView mBtnShare;
    
    private SeekBar mFontSizeSeekBar;
    
    private TextView mTvFontSize;
    
    private Comment mCurrentClickComment = null;
    
	public static NewsDetailFragment newInstance() {
		NewsDetailFragment fragment = new NewsDetailFragment();
		return fragment;
	}
	
    
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
        
        layCommit = (LinearLayout) root.findViewById(R.id.lay_commit_edit);
        layCommit.setVisibility(View.VISIBLE);
        //
        layFontControlContainer = (LinearLayout) root.findViewById(R.id.lay_font_control_container);
        layFontControlContainer.setVisibility(View.GONE);
        
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
        
        mInput = (TextView) root.findViewById(R.id.tv_put);
        
        mInput.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	String hintString = getResources().getString(R.string.comments_hint_text);
                handleInputComment(hintString);
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
        
        mBtnFontSize = (ImageView) root.findViewById(R.id.iv_set_font_size);
        mBtnFontSize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(layFontControlContainer.getVisibility() == View.VISIBLE){
					layFontControlContainer.setVisibility(View.GONE);
				}else{
					layFontControlContainer.setVisibility(View.VISIBLE);
					String fontsizeString = getActivity().getResources().getString(R.string.comments_webview_font_size);
					int zoom = mWebView.getTextZoom();
					String result = String.format(fontsizeString, zoom)+"%";
					mTvFontSize.setText(result);
				}
			}
		});
        
        mBtnShare = (ImageView) root.findViewById(R.id.iv_share);
        mBtnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iDetail.toShare();
			}
		});
        
        mFontSizeSeekBar = (SeekBar) root.findViewById(R.id.font_size_seekbar);
        mFontSizeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int value = seekBar.getProgress();
				int textZoom = value + 80;
				mWebView.setTextZoom(textZoom);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				String fontsizeString = getActivity().getResources().getString(R.string.comments_webview_font_size);
				String result = String.format(fontsizeString, progress + 80)+"%";
				mTvFontSize.setText(result);
			}
		});
        mTvFontSize = (TextView) root.findViewById(R.id.tv_fontsize);
    }
    
	private void handleInputComment(String hintString) {
		layCommit.setVisibility(View.GONE);
        layRealComentEdit.setVisibility(View.VISIBLE);
        mCommentEditView.setHint(hintString);
        mCommentEditView.setFocusable(true);  
        mCommentEditView.setFocusableInTouchMode(true);  
        mCommentEditView.requestFocus();
        showSoftInput(mCommentEditView);
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
        NewsDetail newsDetail = (NewsDetail) iDetail.getData();
        if(newsDetail == null){
            return;
        }
        setWebViewContent(newsDetail.getContent());
        mTitle.setText(newsDetail.getTitle());
        mPubTime.setText(StringUtils.friendlyTime(newsDetail.getDateline()));
        mFrom.setText(newsDetail.getFrom());
        
        mComments.setTitle("最新评论");
        mComments.init(newsDetail.getCommurlapi(), 0, AppConfig.CONF_DETAIL_COMMENTS_MAX_COUNT, getImgLoader(), this);
        
        mCommentsCount.setText(String.valueOf(newsDetail.getComments()));
    }

    @Override
    public void onClick(View view, Comment comment) {
        // TODO Auto-generated method stub
    	mCurrentClickComment = comment;
    	String result = getResources().getString(R.string.comments_hint_text);
		if (mCurrentClickComment != null) {
			String hintString = getResources().getString(
					R.string.comments_hint_refer_text);
			result = String.format(hintString,
					mCurrentClickComment.getPosition());
		}
    	handleInputComment(result);
    }
    
	@Override
	public void seeMoreComments(View view) {
		// TODO Auto-generated method stub
		iDetail.toSeeMoreComments();
	}
    
    private void handleSendComment() {
    	int cid = 0;
    	int position = 0;
		if (mCurrentClickComment != null) {
			cid = mCurrentClickComment.getCid();
			position = mCurrentClickComment.getPosition();
		}
        iDetail.toSendComment(mCommentEditView.getText().toString(), cid, position);
        mCurrentClickComment = null;
    }

	@Override
	public void toFavoriteSucess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toShareSucess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toSendCommentSucess() {
		// TODO Auto-generated method stub
		NewsDetail newsDetail = (NewsDetail) iDetail.getData();
		mComments.refreshComments(newsDetail.getCommurlapi(), 0, AppConfig.CONF_DETAIL_COMMENTS_MAX_COUNT, getImgLoader(), this);
	}
}
