package com.android.backchina.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.backchina.R;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.ui.dialog.DialogHelper;
import com.android.backchina.ui.dialog.WaitDialog;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.widget.XListView;

public abstract class BaseCommentsActivity<T> extends BaseActivity implements XListView.IXListViewListener,
OnItemClickListener, View.OnClickListener{

	private Context mContext;

	private ImageView mBtnBack;
	
	private TextView mTitle;
	
	protected XListView mListView;

	private EmptyLayout mEmptyLayout;
	
	protected BaseListAdapter<T> mAdapter;
	
	private RelativeLayout layRealComentEdit;
	
	private LinearLayout layCommit;
	
    private TextView mInput;
    
    private EditText mCommentEditView;
    
    private Button mBtnSend;
    
    protected WaitDialog mWaitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initBundle(getIntent().getExtras());
		setContentView(R.layout.activity_comment_list_layout);
		mContext = this;
		setupViews();
		initData();
	}

	protected void initBundle(Bundle bundle) {
		//
	}

	protected void setupViews() {
		mEmptyLayout = (EmptyLayout) findViewById(R.id.error_layout);
		mListView = (XListView) findViewById(R.id.pull_and_load_listview);
		mListView.setAutoLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(this);
		mEmptyLayout.setOnLayoutClickListener(this);
		//
		mBtnBack = (ImageView) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mTitle = (TextView) findViewById(R.id.tv_title);
		//
		layCommit = (LinearLayout) findViewById(R.id.lay_commit_edit);
        layCommit.setVisibility(View.VISIBLE);
        
		layRealComentEdit = (RelativeLayout) findViewById(R.id.lay_real_comment_edit);
        layRealComentEdit.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hideCommentView();
            }
        });
        layRealComentEdit.setVisibility(View.GONE);
        mCommentEditView = (EditText) findViewById(R.id.et_comment);
        mCommentEditView.setOnEditorActionListener(new OnEditorActionListener() {
            
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        
        mInput = (TextView) findViewById(R.id.tv_put);
        
        mInput.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            	String hintString = getResources().getString(R.string.comments_hint_text);
                handleInputComment(hintString);
            }
        });
        
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				handleSendComment();
				hideCommentView();
			}
		});
	}
	
	protected void handleInputComment(String hintString) {
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
		InputMethodManager inputMethodManager = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(
				mCommentEditView.getWindowToken(), 0);
		layRealComentEdit.setVisibility(View.GONE);
		layCommit.setVisibility(View.VISIBLE);
	}
	
	private void handleSendComment() {
        String msg = mCommentEditView.getText().toString();
		onHandleSendComment(msg);
	}
	
	protected void onHandleSendComment(String content){
		
	}
	
	protected void setTitle(String title){
		mTitle.setText(title);
	}
	 
	protected void initData() {
		mWaitDialog = DialogHelper.getWaitDialog(this, "正在提交...");
		mAdapter = getListAdapter();
        mListView.setAdapter(mAdapter);
		//
		requestData();
	}

    private void requestData() {
    	onRequestData();
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	mWaitDialog.dismiss();
    }
    
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		requestData();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		requestData();
	}
	
    public void stopLoadMore(){
    	mListView.stopLoadMore();
    }
    
    public void loadMoreNodata(){
    	mListView.completeLoadMore();
    }
    
    public void autoRefresh(){
    	mListView.autoRefresh();
    }
    
    protected void onRequestData() {

    }
    
    public void refreshComplete(){
    	mListView.stopRefresh();
    }
    
	protected void onRequestError(int type) {
		refreshComplete();
		setEmptyLayoutStatus(type);
	}
    
    protected void onRequestSuccess() {
    	refreshComplete();
    	mEmptyLayout.dismiss();
    }
    
    protected void setEmptyLayoutStatus(int type){
    	if (mEmptyLayout != null) {
    		mEmptyLayout.setErrorType(type);
		}
    }
	
	 protected abstract BaseListAdapter<T> getListAdapter();
}
