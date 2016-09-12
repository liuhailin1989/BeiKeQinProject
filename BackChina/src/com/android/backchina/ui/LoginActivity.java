
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

import com.android.backchina.AppConfig;
import com.android.backchina.AppContext;
import com.android.backchina.Constants;
import com.android.backchina.R;
import com.android.backchina.api.ApiHttpClient;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.bean.Login;
import com.android.backchina.bean.UserInfo;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.ui.dialog.DialogHelper;
import com.android.backchina.ui.dialog.WaitDialog;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

public class LoginActivity extends BaseActivity {

    private ImageView mBtnBack;

    private TextView mTitle;

    private EditText etUserName;

    private EditText etPassword;

    private Button btnLogin;

    private TextView tvRegisterAcount;
    
    private TextView tvErrorInfo;

    private String mUserName;

    private String mPassword;
    
    private WaitDialog mWaitDialog;
    
    private Context mContext;
    
    public static void show(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        mContext = this;
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
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                handleLogin();
            }
        });
        
        tvRegisterAcount = (TextView) findViewById(R.id.tv_register_acount);
        tvRegisterAcount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.enterRegisterActivity(mContext);
			}
		});
        tvErrorInfo = (TextView) findViewById(R.id.tv_error_info);
    }

    private void initData() {
    	mTitle.setText("登录页面");
    	mWaitDialog = DialogHelper.getWaitDialog(this, "登录中...");
    	tvErrorInfo.setText("");
    }

   //登录
    private final TextHttpResponseHandler mLoginHandler = new TextHttpResponseHandler() {

        @Override
        public void onFailure(int code, Header[] headers, String responseString, Throwable throwable) {
            // TODO Auto-generated method stub
            TLog.d("called");
            mWaitDialog.hide();
            tvErrorInfo.setText("登录失败,请重试");
        }

        @Override
        public void onSuccess(int code, Header[] headers, String responseString) {
            // TODO Auto-generated method stub
            TLog.d("called");
            handleLoginResponse(headers,responseString);
            mWaitDialog.hide();
        }

    };

    private void handleLogin() {
        mUserName = etUserName.getText().toString();
        mPassword = etPassword.getText().toString();
//        mUserName = "liuhailin1989";
//        mPassword = "Launcher20130829";
        //
        mWaitDialog.show();
        BackChinaApi.login(mUserName, mPassword, mLoginHandler);
    }

    private void handleLoginResponse(Header[] headers,String response) {
        TLog.d("response =" + response);
        Type type = new TypeToken<ActivitiesBean<Login>>() {
        }.getType();
        ActivitiesBean<Login> activitiesBean = AppContext.createGson().fromJson(response, type);
        Login login = activitiesBean.getActivities();
        if (login.getStatus() == -1) {
            TLog.d("账号不存在");
            tvErrorInfo.setText("账号不存在");
            mWaitDialog.hide();
        } else if (login.getStatus() == -2) {
            TLog.d("密码错误");
            tvErrorInfo.setText("密码错误");
            mWaitDialog.hide();
        } else if (login.getUid() > 0) {
            TLog.d("登录成功");
            handleCookie(headers);
            handleUserInfo();
        } else {
            TLog.d("登录失败");
            tvErrorInfo.setText("登录失败,请重试");
            mWaitDialog.hide();
        }
    }
    
    private void handleCookie(Header[] headers){
        AsyncHttpClient client = ApiHttpClient.getHttpClient();
        HttpContext httpContext = client.getHttpContext();
        CookieStore cookies = (CookieStore) httpContext
                .getAttribute(ClientContext.COOKIE_STORE);
        if (cookies != null) {
            String tmpcookies = "";
            for (Cookie c : cookies.getCookies()) {
                TLog.d("cookie:" + c.getName() + " " + c.getValue());
                tmpcookies += (c.getName() + "=" + c.getValue()) + ";";
            }
            if (StringUtils.isEmpty(tmpcookies)) {

                if (headers != null) {
                    for (Header header : headers) {
                        String key = header.getName();
                        String value = header.getValue();
                        if (key.contains("Set-Cookie"))
                            tmpcookies += value + ";";
                    }
                    if (tmpcookies.length() > 0) {
                        tmpcookies = tmpcookies.substring(0, tmpcookies.length() - 1);
                    }
                }
            }
            AppContext.getInstance().setProperty(AppConfig.CONF_COOKIE,tmpcookies);
            ApiHttpClient.setCookie(ApiHttpClient.getCookie(AppContext.getInstance()));
        }
    }
    

    //同步登录
    private final TextHttpResponseHandler mSyncLoginHandler = new TextHttpResponseHandler() {

        @Override
        public void onFailure(int code, Header[] headers, String responseString, Throwable throwable) {
            // TODO Auto-generated method stub
            TLog.d("called");
        }

        @Override
        public void onSuccess(int code, Header[] headers, String responseString) {
            // TODO Auto-generated method stub
            TLog.d("called");
            handleSyncLoginResponse(responseString);
        }

    };
    
    private void handleSyncLogin() {
         BackChinaApi.syncLogin(mUserName, mPassword, mSyncLoginHandler);
    }

    private void handleSyncLoginResponse(String response){
        
    }
    
    //获取用户信息
    private final TextHttpResponseHandler mUserHandler = new TextHttpResponseHandler() {

        @Override
        public void onFailure(int code, Header[] headers, String responseString, Throwable throwable) {
            // TODO Auto-generated method stub
            TLog.d("called");
            tvErrorInfo.setText("用户信息获取失败,请重试");
        }

        @Override
        public void onSuccess(int code, Header[] headers, String responseString) {
            // TODO Auto-generated method stub
            TLog.d("called");
            handleUserInfoResponse(headers,responseString);
        }

    };

    private void handleUserInfo() {
        BackChinaApi.getUserInfo(mUserHandler);
    }
    
    private void handleUserInfoResponse(Header[] headers, String response) {
        try {
            Type type = new TypeToken<ActivitiesBean<UserInfo>>() {
            }.getType();
            ActivitiesBean<UserInfo> activitiesBean = AppContext.createGson().fromJson(response,
                    type);
            final UserInfo user = activitiesBean.getActivities();
            AppContext.getInstance().saveUserInfo(user);
            handleLoginSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            AppContext.getInstance().cleanLoginInfo();
        }
    }
    
    private void handleLoginSuccess(){
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        this.sendBroadcast(new Intent(Constants.INTENT_ACTION_USER_CHANGE));
        finish();
    }

}
