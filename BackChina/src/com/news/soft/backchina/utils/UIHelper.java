
package com.news.soft.backchina.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.BackChinaMode;
import com.news.soft.backchina.bean.Blog;
import com.news.soft.backchina.bean.BlogDetail;
import com.news.soft.backchina.bean.News;
import com.news.soft.backchina.bean.NewsDetail;
import com.news.soft.backchina.bean.Subscribe;
import com.news.soft.backchina.bean.SubscribeDetail;
import com.news.soft.backchina.bean.Video;
import com.news.soft.backchina.ui.AboutUsActivity;
import com.news.soft.backchina.ui.BlogDetailActivity;
import com.news.soft.backchina.ui.BlogSpaceActivity;
import com.news.soft.backchina.ui.ChannelBlogActivity;
import com.news.soft.backchina.ui.ChannelNewsActivity;
import com.news.soft.backchina.ui.ChannelVideoActivity;
import com.news.soft.backchina.ui.CityListActivity;
import com.news.soft.backchina.ui.CommentBlogActivity;
import com.news.soft.backchina.ui.CommentNewsActivity;
import com.news.soft.backchina.ui.CommonWebActivity;
import com.news.soft.backchina.ui.LoginActivity;
import com.news.soft.backchina.ui.MyFavoriteActivity;
import com.news.soft.backchina.ui.NewsDetailActivity;
import com.news.soft.backchina.ui.RegisterActivity;
import com.news.soft.backchina.ui.SpecialNewsDetailActivity;
import com.news.soft.backchina.ui.SubscribeActivity;
import com.news.soft.backchina.ui.SubscribeDetailActivity;
import com.news.soft.backchina.ui.YouToBeVideoPlayerActivity;

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
    	if(StringUtils.isEmpty(url)){
    		return;
    	}
    	Uri uri = Uri.parse(url);
    	Intent intent = new Intent(Intent.ACTION_VIEW,uri);
    	context.startActivity(intent);
    }
    
    public static void enterCommonWebActivity(Context context, String url) {
    	CommonWebActivity.show(context, url);
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
