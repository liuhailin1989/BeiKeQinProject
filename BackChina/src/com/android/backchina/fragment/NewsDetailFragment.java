package com.android.backchina.fragment;

import android.content.Context;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.backchina.AppConfig;
import com.android.backchina.R;
import com.android.backchina.adapter.RelatedNewsAdapter;
import com.android.backchina.bean.Comment;
import com.android.backchina.bean.News;
import com.android.backchina.bean.NewsDetail;
import com.android.backchina.manager.FavoriteManager;
import com.android.backchina.ui.comment.CommentsView;
import com.android.backchina.ui.comment.OnCommentOpsListener;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.UIHelper;
import com.android.backchina.widget.FixedHeightListView;

public class NewsDetailFragment<T> extends DetailFragment<Object> implements OnCommentOpsListener,OnItemClickListener{

	private ScrollView mScrollView;
	private LinearLayout layDetailTitleContainer;
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
    
    private ImageView mBtnFavorite;
    
    private ImageView mBtnShare;
    
    private SeekBar mFontSizeSeekBar;
    
    private TextView mTvFontSize;
    
    private Comment mCurrentClickComment = null;
    
    private FixedHeightListView mRelatedListView;
    
    private RelatedNewsAdapter relatedNewsAdapter;
    
    private  boolean isFavorite = false;
    
    float titleTextSize = 0;
    
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
        titleTextSize = getActivity().getResources().getDimension(R.dimen.common_detail_title_text_size);
        mScrollView = (ScrollView) root.findViewById(R.id.sv_detail_scrollview);
        layDetailTitleContainer = (LinearLayout) root.findViewById(R.id.ll_detail_title_container);
        mTitle = (TextView) root.findViewById(R.id.tv_title);
        
        mPubTime = (TextView) root.findViewById(R.id.tv_pub_date);
        
        mFrom = (TextView) root.findViewById(R.id.tv_from);
        
        mComments = (CommentsView) root.findViewById(R.id.lay_comment_view);
        
        //
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View header = inflater.inflate(R.layout.layout_related_news_header_view, null);
        mRelatedListView = (FixedHeightListView) root.findViewById(R.id.related_list_view);
        mRelatedListView.addHeaderView(header);
        relatedNewsAdapter = new RelatedNewsAdapter(this);
        mRelatedListView.setAdapter(relatedNewsAdapter);
        mRelatedListView.setOnItemClickListener(this);
        mRelatedListView.setVisibility(View.VISIBLE);
        //
        layCommit = (LinearLayout) root.findViewById(R.id.lay_commit_edit);
        layCommit.setVisibility(View.VISIBLE);
        //
        layFontControlContainer = (LinearLayout) root.findViewById(R.id.lay_font_control_container);
        layFontControlContainer.setVisibility(View.GONE);
        
        mCommentsCount = (TextView) root.findViewById(R.id.tv_commit_count);
        mCommentsCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scrollToCommentsLocation();
			}
		});
        //
        //
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
        super.initData();
        NewsDetail newsDetail = (NewsDetail) iDetail.getData();
        if(newsDetail == null){
            return;
        }
        isFavorite = newsDetail.isFavorite();
        if(isFavorite){
        	mBtnFavorite.setImageResource(R.drawable.ic_favorite_selected);
        }else{
        	mBtnFavorite.setImageResource(R.drawable.ic_favorite);
        }
        setWebViewContent(newsDetail.getContent());
        mTitle.setText(newsDetail.getTitle());
        //
		float resultSize = titleTextSize * (mWebView.getTextZoom())/100;
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX,resultSize);
        //
        mPubTime.setText(StringUtils.friendlyTime(newsDetail.getDateline()));
        mFrom.setText(newsDetail.getFrom());
        //
        mCommentsCount.setText("0");
        //
        mComments.setTitle("最新评论");
        mComments.init(newsDetail.getCommurlapi(), newsDetail.getNewsType(), AppConfig.CONF_DETAIL_COMMENTS_MAX_COUNT, getImgLoader(), this);

        //
        if(newsDetail.getRelated_b() != null && newsDetail.getRelated_b().size() > 0){
        	relatedNewsAdapter.clear();
        	relatedNewsAdapter.addItem(newsDetail.getRelated_b());
        }else{
        	mRelatedListView.setVisibility(View.GONE);
        }
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
	
	@Override
	public void refreshCommentsCount(int value) {
		// TODO Auto-generated method stub
		// 设置标题栏评论数量
		if (iDetail != null) {
			iDetail.resfreshTitleComments(value);
		}
        //
        if (value <= 99) {
			mCommentsCount.setBackgroundResource(R.drawable.ic_comment_count_bg);
			mCommentsCount.setText(String.valueOf(value));
		}else{
        	mCommentsCount.setBackgroundResource(R.drawable.ic_comment_count_bg_more);
            mCommentsCount.setText("99  ");
        }
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
		NewsDetail newsDetail = (NewsDetail) iDetail.getData();
		mComments.refreshComments(newsDetail.getCommurlapi(), newsDetail.getNewsType(), AppConfig.CONF_DETAIL_COMMENTS_MAX_COUNT, getImgLoader(), this);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (parent instanceof FixedHeightListView) {
			News item = (News) parent.getAdapter().getItem(position);
			if(StringUtils.isEmpty(item.getUrlapi())){
				UIHelper.showUrlRedirect(getActivity(), item.getUrl());
			} else {
				UIHelper.enterNewsDetail(getActivity(), item, false);
			}
		}
	}
}
