package com.android.backchina.fcmpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.android.backchina.fcmpush.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {
    private static final String TAG = "xiaonan_FCM";
    private static Context context = null;
    private static int status_code = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        context = FcmPush.context;
        Log.d(TAG,"消息接收服务被启动");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG,"Title: "+remoteMessage.getData().get("Title"));
        Log.d(TAG,"data: "+remoteMessage.getData().get("Content"));
        notifyMessage(remoteMessage);
    }
    
    /**
     * 发送一个Notification
     * @author xiaonan
     * @param message
     */
    private void notifyMessage(RemoteMessage message){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        //设置一个启动意图
        Intent it = new Intent(context,context.getClass());
        PendingIntent contentIndent = PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);  
        builder.setTicker(message.getData().get("Title")); //首次提醒会有动画上升效果
        builder.setContentTitle(message.getData().get("Title"));//设置下拉列表里的标题
        builder.setContentText(message.getData().get("Content"));//设置上下文内容
        builder.setSmallIcon(R.drawable.naruto);//设置小图标
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.naruto));//设置大图标
        builder.setWhen(System.currentTimeMillis());//设置时间发生时间
        builder.setAutoCancel(true); //自动取消
        builder.setContentIntent(contentIndent);
        manager.notify(status_code,builder.getNotification());
        status_code++;
    }
    
}
