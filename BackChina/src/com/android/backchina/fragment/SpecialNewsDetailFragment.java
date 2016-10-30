package com.android.backchina.fragment;

import android.content.Context;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView.OnEditorActionListener;

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.bean.Comment;
import com.android.backchina.bean.SpecialNewsDetail;
import com.android.backchina.bean.base.BlogCommentBean;
import com.android.backchina.manager.SubscribeManager;
import com.android.backchina.ui.comment.BlogCommentsView;
import com.android.backchina.ui.comment.OnBlogCommentOpsListener;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.UIHelper;
import com.android.backchina.widget.CircleImageView;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SpecialNewsDetailFragment extends DetailFragment<Object> implements OnBlogCommentOpsListener{

	private ScrollView mScrollView;
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
    
    private ImageView mBtnFavorite;
    
    private ImageView mBtnShare;
    
    private LinearLayout layFontControlContainer;
    
    private SeekBar mFontSizeSeekBar;
    
    private TextView mTvFontSize;
    
    private BlogCommentBean mCurrentClickComment = null;
    
    private  boolean isFavorite = false;
    
    private float titleTextSize = 0;
    
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
        titleTextSize = getActivity().getResources().getDimension(R.dimen.common_detail_title_text_size);
        mScrollView = (ScrollView) root.findViewById(R.id.sv_detail_scrollview);
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
        mCommentsCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scrollToCommentsLocation();
			}
		});
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
        mBlogerCardSubscribe = (TextView) root.findViewById(R.id.btn_card_subscribe);
        mBlogerCardSubscribe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.notifySpecialNewsSubscribe(getActivity());
			}
		});
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
					mFontSizeSeekBar.setProgress(zoom - 80);
				}
			}
		});
        //
        mBtnFavorite = (ImageView) root.findViewById(R.id.iv_favorite);
        mBtnFavorite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isFavorite){
					iDetail.cancleFavorite();
				} else {
					iDetail.toFavorite();
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
				float resultSize = titleTextSize * (textZoom)/100;
				mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,resultSize);
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
        SpecialNewsDetail<BlogCommentBean> specialNewsDetail = (SpecialNewsDetail<BlogCommentBean>) iDetail.getData();
        if(specialNewsDetail == null){
            return;
        }
        isFavorite = specialNewsDetail.isFavorite();
        if(isFavorite){
        	mBtnFavorite.setImageResource(R.drawable.ic_favorite_selected);
        }else{
        	mBtnFavorite.setImageResource(R.drawable.ic_favorite);
        }
        setWebViewContent(specialNewsDetail.getContent());
        mTitle.setText(specialNewsDetail.getTitle());
        //
 		float resultSize = titleTextSize * (mWebView.getTextZoom())/100;
 		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,resultSize);
        mPubTime.setText(StringUtils.friendlyTime(specialNewsDetail.getDateline()));
        mAuthor.setText(specialNewsDetail.getUsername());
        mComments.setTitle("最新评论");
        mComments.init(specialNewsDetail.getBlogcomments(), 0, 20, null, this);
        if (specialNewsDetail.getComments() <= 99) {
			mCommentsCount.setBackgroundResource(R.drawable.ic_comment_count_bg);
			mCommentsCount.setText(String.valueOf(specialNewsDetail.getComments()));
		}else{
        	mCommentsCount.setBackgroundResource(R.drawable.ic_comment_count_bg_more);
            mCommentsCount.setText("99  ");
        }
        setImageFromNet(mBlogerCardAvatar, specialNewsDetail.getAvatar(),R.drawable.default_avatar);
        mBlogerCardAuthor.setText(specialNewsDetail.getUsername());
    }

    @Override
    public void onClick(View view,int position, BlogCommentBean comment) {
        // TODO Auto-generated method stub
        
    }
    
	@Override
	public void seeMoreComments(View view) {
		// TODO Auto-generated method stub
		
	}
    
    private void handleSendComment() {
    	int cid = 0;
    	int position = 0;
		if (mCurrentClickComment != null) {
			cid = mCurrentClickComment.getCid();
			position = 0;
		}
        iDetail.toSendComment(mCommentEditView.getText().toString(),cid,position);
        mCurrentClickComment = null;
    }

	@Override
	public void toFavoriteSucess() {
		// TODO Auto-generated method stub
		if(mBtnFavorite != null){
			mBtnFavorite.setImageResource(R.drawable.ic_favorite_selected);
		}
		isFavorite = true;
	}
	
	@Override
	public void toCancleFavoriteSucess() {
		// TODO Auto-generated method stub
		if(mBtnFavorite != null){
			mBtnFavorite.setImageResource(R.drawable.ic_favorite);
		}
		isFavorite = false;
	}

	@Override
	public void toShareSucess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toSendCommentSucess() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void scrollToCommentsLocation() {
		// TODO Auto-generated method stub
		mScrollView.smoothScrollTo(0, (int)mComments.getY());
	}

	@Override
	public void setUserAvatarImage(ImageView imageView, String imageUrl,
			int placeholder) {
		// TODO Auto-generated method stub
		setImageFromNet(imageView, imageUrl, placeholder);
	}
}
