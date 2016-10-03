package com.xiaonan.fcmpush;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by xiaonan on 16/5/19.
 */
public class MyIDService extends FirebaseInstanceIdService {
    private static final String TAG = "xiaonan_FCM";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Token注册服务已经开启");
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

}
