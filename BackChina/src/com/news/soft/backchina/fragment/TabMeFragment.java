
package com.news.soft.backchina.fragment;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.AppOperator;
import com.news.soft.backchina.Constants;
import com.news.soft.backchina.R;
import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.base.BaseFragment;
import com.news.soft.backchina.bean.BlogDetail;
import com.news.soft.backchina.bean.UserInfo;
import com.news.soft.backchina.bean.base.ActivitiesBean;
import com.news.soft.backchina.cache.CacheManager;
import com.news.soft.backchina.ui.dialog.DialogHelper;
import com.news.soft.backchina.utils.FileUtil;
import com.news.soft.backchina.utils.MethodsCompat;
import com.news.soft.backchina.utils.TLog;
import com.news.soft.backchina.utils.UIHelper;
import com.news.soft.backchina.widget.CircleImageView;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class TabMeFragment extends BaseFragment {

    private LinearLayout layUserInfoContainer;
    
    private TextView mFriends;//好友
    private TextView mViews;//访客
    private TextView mDoings;//记录
    private TextView mBlogs;//日志
    private TextView mAlbums;//相册
    
    private CircleImageView mAvatar;
    
    private Button mUserName;
    
    private Button mBtnLogout;
    
    private LinearLayout mFavoriteContainer;
    private LinearLayout mBlogContainer;
    private LinearLayout mCacheContainer;
    private LinearLayout mAboutContainer;
    
    private TextView mCacheSizeView;
    
    private UserInfo mUserInfo;
    
    private AsyncTask<String, Void, UserInfo> mCacheTask;
    
    private boolean mIsWatingLogin = false;

    //获取用户信息
    private final TextHttpResponseHandler mUserHandler = new TextHttpResponseHandler() {

        @Override
        public void onFailure(int code, Header[] headers, String responseString, Throwable throwable) {
            // TODO Auto-generated method stub
            TLog.d("called");
        }

        @Override
        public void onSuccess(int code, Header[] headers, String responseString) {
            // TODO Auto-generated method stub
            TLog.d("called");
            try {
                Type type = new TypeToken<ActivitiesBean<UserInfo>>() {
                }.getType();
                ActivitiesBean<UserInfo> activitiesBean = AppContext.createGson().fromJson(responseString,
                        type);
                mUserInfo = activitiesBean.getActivities();
                if (mUserInfo != null) {
                    refreshUserInfoUI();
                    AppContext.getInstance().updateUserInfo(mUserInfo);
                    AppOperator.runOnThread(new Runnable() {
                        @Override
                        public void run() {
                            CacheManager.saveObject(getActivity(), mUserInfo, getCacheKey());
                        }
                    });
                }
            } catch (Exception e) {
                onFailure(code, headers, responseString, e);
            }
        }

    };
    
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.INTENT_ACTION_USER_CHANGE)) {
                requestData(true);
            } else if (action.equals(Constants.INTENT_ACTION_LOGOUT)) {
				mIsWatingLogin = true;
				mUserInfo = AppContext.getInstance().getLoginUser();
				setupUser();
				refreshUserInfoUI();
			}
        }
    };

    
    private String getCacheKey() {
        return "my_information" + AppContext.getInstance().getLoginUid();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(Constants.INTENT_ACTION_LOGOUT);
        filter.addAction(Constants.INTENT_ACTION_USER_CHANGE);
        getActivity().registerReceiver(mReceiver, filter);
    }
    
    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.fragment_me_layout;
    }

    @Override
    protected void initBundle(Bundle bundle) {
        // TODO Auto-generated method stub
        super.initBundle(bundle);
    }

    @Override
    protected void setupViews(View root) {
        // TODO Auto-generated method stub
        super.setupViews(root);
        
        //
        layUserInfoContainer = (LinearLayout) root.findViewById(R.id.ll_user_info_container);
        mFriends = (TextView) root.findViewById(R.id.tv_my_friends);
        mViews = (TextView) root.findViewById(R.id.tv_my_views);
        mDoings = (TextView) root.findViewById(R.id.tv_my_doings);
        mBlogs = (TextView) root.findViewById(R.id.tv_my_blogs);
        mAlbums = (TextView) root.findViewById(R.id.tv_my_albums);
        //
        mFavoriteContainer = (LinearLayout) root.findViewById(R.id.ll_my_favorite_container);
        mFavoriteContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.enterMyFavoriteActivity(getActivity());
			}
		});
        //
        mBlogContainer = (LinearLayout) root.findViewById(R.id.ll_my_blog_container);
        mBlogContainer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 if (mUserInfo == null || mUserInfo.getUid() <=0 ){
					 Toast.makeText(getContext(), "请登录", Toast.LENGTH_SHORT).show();
					 return;
				 }
				BlogDetail myBlogDetail = new BlogDetail();
				myBlogDetail.setAuthorid(String.valueOf(mUserInfo.getUid()));
				myBlogDetail.setAvatar(mUserInfo.getAvatar());
				myBlogDetail.setUsername(mUserInfo.getUsername());
				
				UIHelper.enterBlogSpaceActivity(getActivity(), myBlogDetail);
			}
		});
        mCacheContainer = (LinearLayout) root.findViewById(R.id.ll_cache_container);
        mAboutContainer = (LinearLayout) root.findViewById(R.id.ll_about_container);
        
        mAvatar = (CircleImageView) root.findViewById(R.id.iv_avatar);
        mBtnLogout =(Button) root.findViewById(R.id.btn_logout);
        mBtnLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppContext.getInstance().Logout();
				UIHelper.notifySubscribeDataChanged(getActivity());
			}
		});
        mUserName = (Button) root.findViewById(R.id.tv_user_name);
        mUserName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
            	if(AppContext.getInstance().isLogin()){
            		return;
            	}
                UIHelper.enterLoginActivity(getActivity());
            }
        });
        
        mCacheContainer.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DialogHelper.getConfirmDialog(getActivity(), "是否清除缓存?", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        UIHelper.clearAppCache(getActivity());
                        mCacheSizeView.setText("0M");
                    }
                }).show();
            }
        });
        
        mAboutContainer.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                UIHelper.enterAboutUsActivity(getActivity());
            }
        });
        mCacheSizeView = (TextView) root.findViewById(R.id.tv_cache_size);
    }
    
    @Override
    protected void initData() {
        // TODO Auto-generated method stub
        super.initData();
        requestData(true);
        mUserInfo = AppContext.getInstance().getLoginUser();
        refreshUserInfoUI();
        caculateCacheSize();
    }

    private void caculateCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = getActivity().getFilesDir();
        File cacheDir = getActivity().getCacheDir();

        fileSize += FileUtil.getDirSize(filesDir);
        fileSize += FileUtil.getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (AppContext.isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            File externalCacheDir = MethodsCompat.getExternalCacheDir(getActivity());
            fileSize += FileUtil.getDirSize(externalCacheDir);
//            fileSize += FileUtil.getDirSize(new File(
//                    org.kymjs.kjframe.utils.FileUtils.getSDCardPath()
//                            + File.separator + HttpConfig.CACHEPATH));
        }
        if (fileSize > 0){
            cacheSize = FileUtil.formatFileSize(fileSize);
        }
        mCacheSizeView.setText(cacheSize);
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }
    
    private void setupUser(){
        if (mIsWatingLogin) {
            layUserInfoContainer.setVisibility(View.GONE);
        }else{
            layUserInfoContainer.setVisibility(View.VISIBLE);
        }
    }
    
    private void refreshUserInfoUI(){
        if (mUserInfo == null || mUserInfo.getUid() <=0 ){
        	mUserName.setText("点击登录");
        	mAvatar.setImageResource(R.drawable.default_avatar);
        	mBtnLogout.setVisibility(View.GONE);
            return;
        }
        mUserName.setText(mUserInfo.getUsername());
        setImageFromNet(mAvatar, mUserInfo.getAvatar(),R.drawable.default_avatar);
        mBtnLogout.setVisibility(View.VISIBLE);
        mFriends.setText(mUserInfo.getFriends() + " 好友");
        mViews.setText(mUserInfo.getViews() + " 访客");
        mDoings.setText(mUserInfo.getDoings() + " 记录");
        mBlogs.setText(mUserInfo.getBlogs() + " 日志");
        mAlbums.setText(mUserInfo.getAlbums() + " 相册");
    }

    private void requestData(boolean refresh) {
        if (AppContext.getInstance().isLogin()) {
            mIsWatingLogin = false;
            String cacheKey = getCacheKey();
            if (refresh){
                sendRequestData();
            }else{
                readCacheData(cacheKey);
            }
        }else{
            mIsWatingLogin = true;
        }
        setupUser();
    }
    
    private void sendRequestData(){
        BackChinaApi.getUserInfo(mUserHandler);
    }
    
    private void readCacheData(String key) {
        cancelReadCacheTask();
        mCacheTask = new CacheTask(getActivity()).execute(key);
    }
    

    private void cancelReadCacheTask() {
        if (mCacheTask != null) {
            mCacheTask.cancel(true);
            mCacheTask = null;
        }
    }
    
    private class CacheTask extends AsyncTask<String, Void, UserInfo> {
        private final WeakReference<Context> mContext;

        private CacheTask(Context context) {
            mContext = new WeakReference<Context>(context);
        }

        @Override
        protected UserInfo doInBackground(String... params) {
            Serializable seri = CacheManager.readObject(mContext.get(),
                    params[0]);
            if (seri == null) {
                return null;
            } else {
                return (UserInfo) seri;
            }
        }

        @Override
        protected void onPostExecute(UserInfo info) {
            super.onPostExecute(info);
            if (info != null) {
                mUserInfo = info;
                refreshUserInfoUI();
            }
        }
    }
}
