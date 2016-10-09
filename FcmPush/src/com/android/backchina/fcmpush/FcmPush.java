package com.android.backchina.fcmpush;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * @describe FCM服务实现类
 * Created by xiaonan on 16/5/22.
 */
public class FcmPush {
	private static final String TAG = "xiaonan_FCM";
	public static Context context = null;
	private static String authorizedEntity = "";
	
	/**
	 * 
	 * @param context 上下文环境
	 */
	public static void init(Context context) {
		HashMap<String, String> params = ResolveJson.resolve(context);
		if (context == null && params.isEmpty()) {
			return;
		}
		FcmPush.context = context;
		authorizedEntity = params.get("ProjectId");
		FirebaseOptions.Builder builder = new FirebaseOptions.Builder();
		builder.setApiKey(params.get("ApiKey"));
		builder.setApplicationId(params.get("ApplicationId"));
		builder.setDatabaseUrl(params.get("DatabaseUrl"));
		builder.setGcmSenderId(params.get("GcmSenderId"));
		FirebaseOptions options = builder.build();
		FirebaseApp.initializeApp(context, options);
		Log.d(TAG, "FirebaseAPP初始化完成");
	}
	
	/**
	 * 取消注册FCM服务
	 */
	public static void unRegister() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					FirebaseApp app = FirebaseApp.getInstance();
					FirebaseInstanceId.getInstance(app).deleteInstanceId();
					FirebaseInstanceId.getInstance(app).deleteToken(authorizedEntity, "GCM");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 返回注册好的Token
	 * @return Token
	 */
	public static String getToken(){
		String token = FirebaseInstanceId.getInstance().getToken();
		if (token == null){
			token = "";
		}
		return token;
	}

}
