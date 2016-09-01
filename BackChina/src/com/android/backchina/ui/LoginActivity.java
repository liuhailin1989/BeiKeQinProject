package com.android.backchina.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.android.backchina.R;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.utils.TLog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends BaseActivity{

	private ImageView mBtnBack;
	
	private TextView mTitle;
	
	private EditText etUserName;
	
	private EditText etPassword;
	
	private Button btnLogin;
	
	private TextView tvRegisterAcount;
	
	private String mUserName;
	
	private String mPassword;
	
	public static void show(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_layout);
		setupViews();
		initData();
	}
	
	private void setupViews(){
		etUserName = (EditText) findViewById(R.id.et_user_name);
		etUserName.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		etPassword = (EditText) findViewById(R.id.et_user_password);
		etPassword.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				handleLogin();
			}
		});
	}
	
	private void initData(){
		
	}
	
	private final TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {

		@Override
		public void onFailure(int code, Header[] headers, String responseString,
				Throwable throwable) {
			// TODO Auto-generated method stub
			TLog.d("called");
		}

		@Override
		public void onSuccess(int code, Header[] headers, String responseString) {
			// TODO Auto-generated method stub
			TLog.d("called");
		}
		
	};
	
	private void handleLogin(){
		mUserName = etUserName.getText().toString();
		mPassword = etPassword.getText().toString();
		//
		BackChinaApi.login("liuhailin1989", "Launcher20130829", mHandler);
	}
}
