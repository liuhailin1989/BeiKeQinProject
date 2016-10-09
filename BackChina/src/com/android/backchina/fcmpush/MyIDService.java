package com.android.backchina.fcmpush;

import com.android.backchina.utils.TLog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyIDService extends FirebaseInstanceIdService {
    @Override
    public void onCreate() {
        super.onCreate();
        TLog.d("Token注册服务已经开启");
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        TLog.d("Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

}
