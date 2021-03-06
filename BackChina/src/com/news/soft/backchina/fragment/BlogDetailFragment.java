package com.news.soft.backchina.fragment;

import java.lang.reflect.Type;

import android.content.Context;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.news.soft.backchina.AppConfig;
import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.R;
import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.bean.BlogDetail;
import com.news.soft.backchina.bean.Subscribe;
import com.news.soft.backchina.bean.base.ActivitiesBean;
import com.news.soft.backchina.bean.base.BlogCommentBean;
import com.news.soft.backchina.manager.SubscribeManager;
import com.news.soft.backchina.ui.comment.BlogCommentsView;
import com.news.soft.backchina.ui.comment.OnBlogCommentOpsListener;
import com.news.soft.backchina.utils.StringUtils;
import com.news.soft.backchina.utils.ToastUtils;
import com.news.soft.backchina.utils.UIHelper;
import com.news.soft.backchina.widget.CircleImageView;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class BlogDetailFragment<T> extends DetailFragment<Object> implements OnBlogCommentOpsListener{

	private ScrollView mScrollView;
	private LinearLayout layDetailTitleContainer;
	private RelativeLayout layBlogerCard;
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
    
    private int mCurrentReferPosition = 0;
    
    private  boolean isFavorite = false;
    
    private float titleTextSize = 0;
    
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
        titleTextSize = getActivity().getResources().getDimension(R.dimen.common_detail_title_text_size);
        mScrollView = (ScrollView) root.findViewById(R.id.sv_detail_scrollview);
        layDetailTitleContainer = (LinearLayout) root.findViewById(R.id.ll_detail_title_container);
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
        layBlogerCard = (RelativeLayout) root.findViewById(R.id.lay_bloger_card);
        mBlogerCardAvatar = (CircleImageView) root.findViewById(R.id.iv_card_avatar);
        mBlogerCardAvatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BlogDetail<BlogCommentBean> blogDetail = (BlogDetail<BlogCommentBean>) iDetail.getData();
				UIHelper.enterBlogSpaceActivity(getActivity(), blogDetail);
			}
		});
        mBlogerCardAuthor = (TextView) root.findViewById(R.id.tv_card_author);
        mBlogerCardAuthor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BlogDetail<BlogCommentBean> blogDetail = (BlogDetail<BlogCommentBean>) iDetail.getData();
				UIHelper.enterBlogSpaceActivity(getActivity(), blogDetail);
			}
		});
        
        mBlogerCardSubscribe = (TextView) root.findViewById(R.id.btn_card_subscribe);
        mBlogerCardSubscribe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BlogDetail<BlogCommentBean> blogDetail = (BlogDetail<BlogCommentBean>) iDetail.getData();
				BackChinaApi.subscribeBlog(blogDetail.getAuthorid(),
						new TextHttpResponseHandler() {

							@Override
							public void onSuccess(int code, Header[] headers,
									String response) {
								// TODO Auto-generated method stub
								handleSubscribeResponse(headers, response);
							}

							@Override
							public void onFailure(int code, Header[] headers,
									String response, Throwable throwable) {
								// TODO Auto-generated method stub
								ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
							}
						});
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
    
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	getFocus();
    }
    //主界面获取焦点
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
    }
    
	private void handleInputComment(String hintString) {
		layCommit.setVisibility(View.GONE);
        layRealComentEdit.setVisibility(View.VISIBLE);
        mCommentEditView.setHint(hintString);
        mCommentEditView.setFocusable(true);  
        mCommentEditView.setFocusableInTouchMode(true);  
        mCommentEditView.requestFocus();
        mCommentEditView.setOnKeyListener(new OnKeyListener() {
			
   			@Override
   			public boolean onKey(View v, int keyCode, KeyEvent event) {
   				// TODO Auto-generated method stub
   				if (keyCode == KeyEvent.KEYCODE_BACK
   						&& event.getAction() == KeyEvent.ACTION_UP) {
   					hideCommentView();
   					// 使得根View重新获取焦点，以监听返回键
   					getFocus();
   					return true;
   				}
   				return false;
   			}
   		});
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
        isFavorite = blogDetail.isFavorite();
        if(isFavorite){
        	mBtnFavorite.setImageResource(R.drawable.ic_favorite_selected);
        }else{
        	mBtnFavorite.setImageResource(R.drawable.ic_favorite);
        }
        setWebViewContent(blogDetail.getContent());
        mTitle.setText(blogDetail.getTitle());
        //
		float resultSize = titleTextSize * (mWebView.getTextZoom())/100;
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,resultSize);
        mPubTime.setText(StringUtils.friendlyTime(blogDetail.getDateline()));
        mAuthor.setText(blogDetail.getUsername());
        //
        mCommentsCount.setText("0");
        mComments.setTitle("最新评论");
        mComments.init(blogDetail.getBlogcomments(), 0, AppConfig.CONF_DETAIL_COMMENTS_MAX_COUNT, getImgLoader(), this);
        
        refreshCommentsCount(blogDetail.getComments());
        setImageFromNet(mBlogerCardAvatar, blogDetail.getAvatar(),R.drawable.default_avatar);
        mBlogerCardAuthor.setText(blogDetail.getUsername());
    }

	private void refreshCommentsCount(int count) {
		//设置标题栏评论数量
        iDetail.resfreshTitleComments(count);
        
		if (count <= 99) {
			mCommentsCount.setBackgroundResource(R.drawable.ic_comment_count_bg);
			mCommentsCount.setText(String.valueOf(count));
		}else{
        	mCommentsCount.setBackgroundResource(R.drawable.ic_comment_count_bg_more);
            mCommentsCount.setText("99  ");
        }
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
		BlogDetail<BlogCommentBean> blogDetail = (BlogDetail<BlogCommentBean>) iDetail.getData();
		mComments.refreshComments(blogDetail.getBlogcomments(), 0, AppConfig.CONF_DETAIL_COMMENTS_MAX_COUNT, getImgLoader(), this);
		refreshCommentsCount(blogDetail.getComments());
		mComments.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				scrollToCommentsLocation();
			}
		}, 500);
	}
	

	@Override
	public void scrollToCommentsLocation() {
		// TODO Auto-generated method stub
		mScrollView.smoothScrollTo(0, (int)mComments.getY());
	}
	
    private void handleSubscribeResponse(Header[] headers,String response) {
    	Type type = new TypeToken<ActivitiesBean<Subscribe>>() {
        }.getType();
        ActivitiesBean<Subscribe> activitiesBean = AppContext.createGson().fromJson(response, type);
        Subscribe subscribe = activitiesBean.getActivities();
		if (StringUtils.isEmpty(subscribe.getStatus())) {
			if (subscribe.getFavid() != null) {
				ToastUtils.show(getContext(), R.string.toast_subscribe_sucessed);
				UIHelper.notifySubscribeDataChanged(getActivity());
			}else{
				ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
			}
		} else {
			if(subscribe.getStatus().equals("1")){
				ToastUtils.show(getContext(), R.string.toast_subscribe_sucessed);
				UIHelper.notifySubscribeDataChanged(getActivity());
			}else if (subscribe.getStatus().contains("repeat")) {
				ToastUtils.show(getContext(), R.string.toast_subscribed);
			} else if (subscribe.getStatus().equals("-1")) {
				ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
			} else if (subscribe.getStatus().equals("-2")) {
				ToastUtils.show(getContext(), R.string.toast_need_login);
			} else {
				ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
			}
		}
    }

	@Override
	public void setUserAvatarImage(ImageView imageView, String imageUrl, int placeholder) {
		// TODO Auto-generated method stub
		setImageFromNet(imageView, imageUrl, placeholder);
	}
}
