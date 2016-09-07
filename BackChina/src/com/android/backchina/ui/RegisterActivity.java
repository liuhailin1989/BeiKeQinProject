package com.android.backchina.ui;

import java.lang.reflect.Type;

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

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.bean.Login;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.ui.dialog.DialogHelper;
import com.android.backchina.ui.dialog.WaitDialog;
import com.android.backchina.utils.TLog;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends BaseActivity {

	private ImageView mBtnBack;

	private TextView mTitle;

	private EditText etUserName;

	private EditText etPassword;

	private EditText etEmail;

	private Button mBtnRegister;

	private TextView mLoginAcount;

	private TextView mErrorInfo;

	private TextHttpResponseHandler mRegisterHandler;

	private WaitDialog mWaitDialog;

	private String mUserName;

	private String mPassword;

	private String mEmail;

	public static void show(Context context) {
		Intent intent = new Intent(context, RegisterActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_layout);
		setupViews();
		initData();
	}

	private void setupViews() {
		mTitle = (TextView) findViewById(R.id.tv_title);
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
		etEmail = (EditText) findViewById(R.id.et_user_email);
		etEmail.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		mBtnRegister = (Button) findViewById(R.id.btn_register);
		mBtnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handleRegister();
			}
		});
		mLoginAcount = (TextView) findViewById(R.id.tv_login_acount);
		mLoginAcount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                finish();
			}
		});
		mErrorInfo = (TextView) findViewById(R.id.tv_error_info);
	}

	private void initData() {
		mWaitDialog = DialogHelper.getWaitDialog(this, "正在提交...");
		mRegisterHandler = new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int code, Header[] headers,
					String responseString) {
				// TODO Auto-generated method stub
				handleRegisterResponse(headers,responseString);
			}

			@Override
			public void onFailure(int code, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				mWaitDialog.hide();
				mErrorInfo.setText("网络错误");
			}
		};

		mTitle.setText("账户注册");
	}

	private void handleRegister() {
		mWaitDialog.show();
		mUserName = etUserName.getText().toString();
		mPassword = etPassword.getText().toString();
		mEmail = etEmail.getText().toString();
		
		//
//		mUserName = "liuwei1989";
//		mPassword = "00000000";
//		mEmail = "1294866130@qq.com";
		BackChinaApi.register(mUserName, mPassword, mEmail, mRegisterHandler);
	}
	
	private void handleRegisterResponse(Header[] headers,String response){
		TLog.d("CALLED");
        Type type = new TypeToken<ActivitiesBean<Login>>() {
        }.getType();
        ActivitiesBean<Login> activitiesBean = AppContext.createGson().fromJson(response, type);
        Login login = activitiesBean.getActivities();
        if(login == null){
        	mErrorInfo.setText("注册失败");
        }else if (login.getStatus() == -1) {
        	mErrorInfo.setText("用户名不正确");
            mWaitDialog.hide();
        }else if (login.getStatus() == -2) {
        	mErrorInfo.setText("包含不允许注册的词语");
            mWaitDialog.hide();
        }else if (login.getStatus() == -3) {
        	mErrorInfo.setText("用户名已经存在");
            mWaitDialog.hide();
        } else if (login.getStatus() == -4) {
        	mErrorInfo.setText("Email 格式有误");
            mWaitDialog.hide();
        } else if (login.getStatus() == -5) {
        	mErrorInfo.setText("Email 不允许注册");
            mWaitDialog.hide();
        } else if (login.getStatus() == -6) {
        	mErrorInfo.setText("该 Email 已经被注册");
            mWaitDialog.hide();
        } else if(login.getStatus() > 0){
        	mErrorInfo.setText("恭喜您,注册成功");
            mWaitDialog.hide();
        }else{
        	mErrorInfo.setText("注册失败,未知错误");
        	mWaitDialog.hide();
        }
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mWaitDialog.dismiss();
	}
}
