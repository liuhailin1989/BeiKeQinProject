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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;

import com.android.backchina.AppConfig;
import com.android.backchina.R;
import com.android.backchina.bean.BlogDetail;
import com.android.backchina.bean.base.BlogCommentBean;
import com.android.backchina.ui.comment.BlogCommentsView;
import com.android.backchina.ui.comment.OnBlogCommentClickListener;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.UIHelper;
import com.android.backchina.widget.CircleImageView;

public class BlogDetailFragment<T> extends DetailFragment<Object> implements OnBlogCommentClickListener{

    private TextView mTitle;
    private TextView mPubTime;
    private TextView mAuthor;
    
    private TextView mInput;
    
    private BlogCommentsView mComments;
    
    private LinearLayout layCommit;
    
    private RelativeLayout layRealComentEdit;
    
    private EditText mCommentEditView;
    
    private TextView mCommentsCount;
    
    private Button mBtnSend;
    
    private CircleImageView mBlogerCardAvatar;
    
    private TextView mBlogerCardAuthor;
    
    private TextView mBlogerCardSubscribe;
    
    private ImageView mBtnFontSize;
    
    private ImageView mBtnShare;
    
    private LinearLayout layFontControlContainer;
    
    private SeekBar mFontSizeSeekBar;
    
    private TextView mTvFontSize;
    
    private BlogCommentBean mCurrentClickComment = null;
    
    private int mCurrentReferPosition = 0;
    
	public static BlogDetailFragment newInstance() {
		BlogDetailFragment fragment = new BlogDetailFragment();
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
        
        mComments = (BlogCommentsView) root.findViewById(R.id.lay_comment_view);
        
        layCommit = (LinearLayout) root.findViewById(R.id.lay_commit_edit);
        layCommit.setVisibility(View.VISIBLE);
        //
        layFontControlContainer = (LinearLayout) root.findViewById(R.id.lay_font_control_container);
        layFontControlContainer.setVisibility(View.GONE);
        //
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
        mBlogerCardAvatar = (CircleImageView) root.findViewById(R.id.iv_card_avatar);
        mBlogerCardAuthor = (TextView) root.findViewById(R.id.tv_card_author);
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
    	mCurrentReferPosition = 0;
        super.initData();
        BlogDetail<BlogCommentBean> blogDetail = (BlogDetail<BlogCommentBean>) iDetail.getData();
        if(blogDetail == null){
            return;
        }
        setWebViewContent(blogDetail.getContent());
        mTitle.setText(blogDetail.getTitle());
        mPubTime.setText(StringUtils.friendlyTime(blogDetail.getDateline()));
        mAuthor.setText(blogDetail.getUsername());
        mComments.setTitle("最新评论");
        mComments.init(blogDetail.getBlogcomments(), 0, AppConfig.CONF_DETAIL_COMMENTS_MAX_COUNT, getImgLoader(), this);
        mCommentsCount.setText(String.valueOf(blogDetail.getComments()));
        setImageFromNet(mBlogerCardAvatar, blogDetail.getAvatar(),R.drawable.default_avatar);
        mBlogerCardAuthor.setText(blogDetail.getUsername());
    }

    @Override
    public void onClick(View view, int position, BlogCommentBean comment) {
        // TODO Auto-generated method stub
    	mCurrentClickComment = comment;
    	mCurrentReferPosition = position;
    	String result = getResources().getString(R.string.comments_hint_text);
		if (mCurrentClickComment != null) {
			String hintString = getResources().getString(
					R.string.comments_hint_blog_refer_text);
			result = String.format(hintString,
					mCurrentClickComment.getAuthor());
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
			position = mCurrentReferPosition;
		}
        iDetail.toSendComment(mCommentEditView.getText().toString(),cid,position);
        mCurrentClickComment = null;
        mCurrentReferPosition = 0;
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
		BlogDetail<BlogCommentBean> blogDetail = (BlogDetail<BlogCommentBean>) iDetail.getData();
		mComments.refreshComments(blogDetail.getBlogcomments(), 0, AppConfig.CONF_DETAIL_COMMENTS_MAX_COUNT, getImgLoader(), this);
	}
}
