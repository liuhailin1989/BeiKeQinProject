
package com.android.backchina.utils;

import com.android.backchina.AppContext;
import com.android.backchina.bean.Blog;
import com.android.backchina.bean.News;
import com.android.backchina.ui.AboutUsActivity;
import com.android.backchina.ui.BlogDetailActivity;
import com.android.backchina.ui.LoginActivity;
import com.android.backchina.ui.NewsDetailActivity;
import com.android.backchina.ui.SubscribeActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class UIHelper {

    
    public static void showUrlRedirect(Context context, String url) {
//        showUrlRedirect(context, 0, url);
    }
    
    public static void enterNewsDetail(Context context, long newsId, int commentCount) {
        NewsDetailActivity.show(context, newsId);
    }
    
    public static void enterNewsDetail(Context context, News news) {
    	NewsDetailActivity.show(context, news);
    }
    
    public static void enterBlogDetail(Context context, Blog blog) {
    	BlogDetailActivity.show(context, blog);
    }
    
    public static void enterLoginActivity(Context context){
    	LoginActivity.show(context);
    }
    
    public static void enterAboutUsActivity(Context context){
        AboutUsActivity.show(context);
    }
    
    public static void enterSubscribeActivity(Context context){
        SubscribeActivity.show(context);
    }
    
    /**
     * 清除app缓存
     *
     * @param activity
     */
    public static void clearAppCache(final Activity activity) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Toast.makeText(activity, "缓存清除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, "缓存清除失败", Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread() {
            @Override
            public void run() {
                Message msg = new Message();
                try {
                    AppContext.getInstance().clearAppCache();
                    msg.what = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }.start();
    }
}
