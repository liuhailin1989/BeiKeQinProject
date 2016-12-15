package com.news.soft.backchina;

import java.util.Properties;
import java.util.UUID;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

import com.news.soft.backchina.api.ApiHttpClient;
import com.news.soft.backchina.base.BaseApplication;
import com.news.soft.backchina.bean.UserInfo;
import com.news.soft.backchina.cache.DataCleanManager;
import com.news.soft.backchina.fcmpush.FcmPush;
import com.news.soft.backchina.utils.MethodsCompat;
import com.news.soft.backchina.utils.StringUtils;
import com.news.soft.backchina.utils.TLog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;

public class AppContext extends BaseApplication {
	private static AppContext instance;

	private int loginUid;

	private boolean login;

	private BackChinaMode mBackChinaMode;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		mBackChinaMode = new BackChinaMode();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BackChinaMode.ACTION_SUBSCRIBE_DATA_CHANGED);
		filter.addAction(BackChinaMode.ACTION_FAVORITE_DATA_CHANGED);
		filter.addAction(Constants.INTENT_ACTION_USER_CHANGE);
		filter.addAction(BackChinaMode.ACTION_SPECIAL_NEWS_SUBSCRIBE);
		instance.registerReceiver(mBackChinaMode, filter);
		init();
		initLogin();
		FcmPush.init(this);
	}
	
	public BackChinaMode getBackChinaMode(){
		return mBackChinaMode;
	}

	/** 获取Application */
	public static AppContext getInstance() {
		return instance;
	}

	private void init() {
		//
		TLog.DEBUG = true;
		TLog.d("called");

		// 初始化网络请求
		AsyncHttpClient client = new AsyncHttpClient();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		client.setCookieStore(myCookieStore);
		ApiHttpClient.setHttpClient(client);
		ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));

	}

	private void initLogin() {
		UserInfo user = getLoginUser();
		if (null != user && user.getUid() > 0) {
			login = true;
			loginUid = user.getUid();
		} else {
			this.cleanLoginInfo();
		}
	}

	/**
	 * 获取App安装包信息
	 *
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	public UserInfo getLoginUser() {
		UserInfo user = new UserInfo();
		user.setUid(StringUtils.toInt(getProperty("user.uid"), 0));
		user.setUsername(getProperty("user.username"));
		user.setAlbums(StringUtils.toInt(getProperty("user.albums"), 0));
		user.setAvatar(getProperty("user.avatar"));
		user.setBlogs(StringUtils.toInt(getProperty("user.blogs"), 0));
		user.setDoings(StringUtils.toInt(getProperty("user.doings"), 0));
		user.setFriends(StringUtils.toInt(getProperty("user.friends"), 0));
		user.setGroupid(StringUtils.toInt(getProperty("user.groupid"), 0));
		user.setNewprompt(StringUtils.toInt(getProperty("user.newprompt"), 0));
		user.setRegdate(getProperty("user.regdate"));
		user.setThreads(StringUtils.toInt(getProperty("user.threads"), 0));
		user.setViews(StringUtils.toInt(getProperty("user.views"), 0));
		return user;
	}

	public void saveUserInfo(final UserInfo user) {
		if (user == null) {
			return;
		}
		this.loginUid = user.getUid();
		this.login = true;
		setProperties(new Properties() {
			{
				setProperty("user.uid", String.valueOf(user.getUid()));
				setProperty("user.username", user.getUsername());
				setProperty("user.albums", String.valueOf(user.getAlbums()));
				setProperty("user.avatar", user.getAvatar());
				setProperty("user.blogs", String.valueOf(user.getBlogs()));
				setProperty("user.doings", String.valueOf(user.getDoings()));
				setProperty("user.friends", String.valueOf(user.getFriends()));
				setProperty("user.groupid", String.valueOf(user.getGroupid()));
				setProperty("user.newprompt",
						String.valueOf(user.getNewprompt()));
				setProperty("user.regdate", user.getRegdate());
				setProperty("user.threads", String.valueOf(user.getThreads()));
				setProperty("user.views", String.valueOf(user.getViews()));
			}
		});
	}

	public void updateUserInfo(final UserInfo user) {
		if (user == null) {
			return;
		}
		setProperties(new Properties() {
			{
				setProperty("user.username", user.getUsername());
				setProperty("user.albums", String.valueOf(user.getAlbums()));
				setProperty("user.avatar", user.getAvatar());
				setProperty("user.blogs", String.valueOf(user.getBlogs()));
				setProperty("user.doings", String.valueOf(user.getDoings()));
				setProperty("user.friends", String.valueOf(user.getFriends()));
				setProperty("user.groupid", String.valueOf(user.getGroupid()));
				setProperty("user.newprompt",
						String.valueOf(user.getNewprompt()));
				setProperty("user.regdate", user.getRegdate());
				setProperty("user.threads", String.valueOf(user.getThreads()));
				setProperty("user.views", String.valueOf(user.getViews()));
			}
		});
	}

	/**
	 * 用户注销
	 */
	public void Logout() {
		cleanLoginInfo();
		ApiHttpClient.cleanCookie();
		this.cleanCookie();
		this.login = false;
		this.loginUid = 0;

		Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
		sendBroadcast(intent);
	}

	/**
	 * 清除保存的缓存
	 */
	public void cleanCookie() {
		removeProperty(AppConfig.CONF_COOKIE);
	}

	public void cleanLoginInfo() {
		this.loginUid = 0;
		this.login = false;
		removeProperty("user.uid", "user.username", "user.albums",
				"user.avatar", "user.blogs", "user.doings", "user.friends",
				"user.groupid", "user.newprompt", "user.regdate",
				"user.threads", "user.views");
	}

	/**
	 * 清除app缓存
	 */
	public void clearAppCache() {
		DataCleanManager.cleanDatabases(this);
		// 清除数据缓存
		DataCleanManager.cleanInternalCache(this);
		// 2.2版本才有将应用缓存转移到sd卡的功能
		if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			DataCleanManager.cleanCustomCache(MethodsCompat
					.getExternalCacheDir(this));
		}
		// 清除编辑器保存的临时内容
		Properties props = getProperties();
		for (Object key : props.keySet()) {
			String _key = key.toString();
			if (_key.startsWith("temp"))
				removeProperty(_key);
		}
		// Core.getKJBitmap().cleanCache();
	}

	public int getLoginUid() {
		return loginUid;
	}

	public boolean isLogin() {
		return login;
	}

	public boolean containsProperty(String key) {
		Properties props = getProperties();
		return props.containsKey(key);
	}

	public void setProperties(Properties ps) {
		AppConfig.getAppConfig(this).set(ps);
	}

	public Properties getProperties() {
		return AppConfig.getAppConfig(this).get();
	}

	public void setProperty(String key, String value) {
		AppConfig.getAppConfig(this).set(key, value);
	}

	/**
	 * 获取cookie时传AppConfig.CONF_COOKIE
	 *
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		String res = AppConfig.getAppConfig(this).get(key);
		return res;
	}

	public void removeProperty(String... key) {
		AppConfig.getAppConfig(this).remove(key);
	}

	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	/**
	 * 获取App唯一标识
	 *
	 * @return
	 */
	public String getAppId() {
		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
		if (StringUtils.isEmpty(uniqueID)) {
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}

	public static Gson createGson() {
		com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();
		// gsonBuilder.setExclusionStrategies(new
		// SpecificClassExclusionStrategy(null, Model.class));
		gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
		return gsonBuilder.create();
	}
}
