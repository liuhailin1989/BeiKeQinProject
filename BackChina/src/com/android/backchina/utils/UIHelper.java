
package com.android.backchina.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.backchina.AppContext;
import com.android.backchina.BackChinaMode;
import com.android.backchina.bean.Blog;
import com.android.backchina.bean.BlogDetail;
import com.android.backchina.bean.News;
import com.android.backchina.bean.NewsDetail;
import com.android.backchina.bean.Subscribe;
import com.android.backchina.bean.SubscribeDetail;
import com.android.backchina.bean.Video;
import com.android.backchina.ui.AboutUsActivity;
import com.android.backchina.ui.BlogDetailActivity;
import com.android.backchina.ui.BlogSpaceActivity;
import com.android.backchina.ui.ChannelBlogActivity;
import com.android.backchina.ui.ChannelNewsActivity;
import com.android.backchina.ui.ChannelVideoActivity;
import com.android.backchina.ui.CityListActivity;
import com.android.backchina.ui.CommentBlogActivity;
import com.android.backchina.ui.CommentNewsActivity;
import com.android.backchina.ui.LoginActivity;
import com.android.backchina.ui.MyFavoriteActivity;
import com.android.backchina.ui.NewsDetailActivity;
import com.android.backchina.ui.RegisterActivity;
import com.android.backchina.ui.SpecialNewsDetailActivity;
import com.android.backchina.ui.SubscribeActivity;
import com.android.backchina.ui.SubscribeDetailActivity;
import com.android.backchina.ui.YouToBeVideoPlayerActivity;

public class UIHelper {

    
	public static void notifySubscribeDataChanged(Context context){
		Intent intent = new Intent();
		intent.setAction(BackChinaMode.ACTION_SUBSCRIBE_DATA_CHANGED);
		context.sendBroadcast(intent);
	}
	
	public static void notifyFavoriteDataChanged(Context context){
		Intent intent = new Intent();
		intent.setAction(BackChinaMode.ACTION_FAVORITE_DATA_CHANGED);
		context.sendBroadcast(intent);
	}
	
	public static void notifySpecialNewsSubscribe(Context context){
		Intent intent = new Intent();
		intent.setAction(BackChinaMode.ACTION_SPECIAL_NEWS_SUBSCRIBE);
		context.sendBroadcast(intent);
	}
	
    public static void showUrlRedirect(Context context, String url) {
//        showUrlRedirect(context, 0, url);
    }
    
    public static void enterChannelNewsActivity(Context context,Fragment fragment) {
    	ChannelNewsActivity.show(context,fragment);
    }
    
    public static void enterChannelBlogActivity(Context context,Fragment fragment) {
    	ChannelBlogActivity.show(context,fragment);
    }
    
    public static void enterChannelVideoActivity(Context context,Fragment fragment) {
    	ChannelVideoActivity.show(context,fragment);
    }
    
    public static void enterNewsDetail(Context context, long newsId, int commentCount) {
        NewsDetailActivity.show(context, newsId);
    }
    
    public static void enterNewsDetail(Context context, News news,boolean isFavorite) {
    	NewsDetailActivity.show(context, news, isFavorite);
    }
    
    public static void enterBlogDetail(Context context, Blog blog, boolean isFavorite) {
    	BlogDetailActivity.show(context, blog, isFavorite);
    }
    
    public static void enterVideoPlayerActivity(Context context,Video video){
    	YouToBeVideoPlayerActivity.show(context,video);
    }
    
    public static void enterLoginActivity(Context context){
    	LoginActivity.show(context);
    }
    
    public static void enterMyFavoriteActivity(Context context){
    	MyFavoriteActivity.show(context);
    }
    
    public static void enterAboutUsActivity(Context context){
        AboutUsActivity.show(context);
    }
    
    public static void enterSubscribeActivity(Context context){
        SubscribeActivity.show(context);
    }
    public static void enterSubscribeDetailActivity(Context context,Subscribe subscribe){
        SubscribeDetailActivity.show(context,subscribe);
    }
    public static void enterSpecialNewsDetailActivity(Context context,SubscribeDetail subscribeDetail){
        SpecialNewsDetailActivity.show(context,subscribeDetail);
    }
    public static void enterRegisterActivity(Context context){
        RegisterActivity.show(context);
    }
    
    public static void enterCityListActivity(Context context,Fragment fragment){
    	CityListActivity.show(context,fragment);
    }
    
    public static void enterCommentNewsActivity(Context context,NewsDetail newsDetail){
    	CommentNewsActivity.show(context,newsDetail);
    }
    
    public static void enterCommentBlogActivity(Context context,Blog blog){
    	CommentBlogActivity.show(context,blog);
    }
    
    public static void enterBlogSpaceActivity(Context context,BlogDetail blogDetail){
    	BlogSpaceActivity.show(context, blogDetail);
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
