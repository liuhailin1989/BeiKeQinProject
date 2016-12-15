package com.news.soft.backchina;

import java.lang.reflect.Type;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.bean.FavoriteBean;
import com.news.soft.backchina.bean.Subscribe;
import com.news.soft.backchina.bean.base.ActivitiesBean;
import com.news.soft.backchina.interf.FavoriteCallback;
import com.news.soft.backchina.interf.ISpecialNewsSubscribeListener;
import com.news.soft.backchina.interf.SubscribeCallback;
import com.news.soft.backchina.interf.SubscribeCatCallback;
import com.news.soft.backchina.manager.FavoriteManager;
import com.news.soft.backchina.manager.SubscribeManager;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class BackChinaMode extends BroadcastReceiver{

	public final static String ACTION_SUBSCRIBE_DATA_CHANGED = "backchina.action.SUBSCRIBE_DATA_CHANGED";
	public final static String ACTION_FAVORITE_DATA_CHANGED = "backchina.action.FAVORITE_DATA_CHANGED";
	//
	public final static String ACTION_SPECIAL_NEWS_SUBSCRIBE = "backchina.action.SPECIAL_NEWS_SUBSCRIBE";
	
	private SubscribeCallback subscribeCallback;
	
	private SubscribeCatCallback subscribeCatCallback;
	
	private FavoriteCallback favoriteCallback;
	
	private ISpecialNewsSubscribeListener specicalNewsSubscribeCallback; 
	
	private int localSubscribeCount = 0;
	
	private int localFavoriteCount = 0;
	
	public BackChinaMode() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final String action = intent.getAction();
        if (ACTION_SUBSCRIBE_DATA_CHANGED.equals(action)) {
        	subscribeDataChanged();
        	subscribeCatDataStatusChanged();
        }else if(ACTION_FAVORITE_DATA_CHANGED.equals(action)){
        	favoriteDataChanged();
        }else if(Constants.INTENT_ACTION_USER_CHANGE.equals(action)){
        	subscribeLocalData(context);
        	handleFavoriteLocalData(context);
        }else if(ACTION_SPECIAL_NEWS_SUBSCRIBE.equals(action)){
        	handleSpecialNewsSubscribe();
        }
	}
	
	public void setSubscribeCallBack(SubscribeCallback callback){
		subscribeCallback = callback;
	}
	
	public void setSubscribeCatCallBack(SubscribeCatCallback callback){
		subscribeCatCallback = callback;
	}
	
	public void setSpecialNewsSubscribeCallBack(ISpecialNewsSubscribeListener callback){
		specicalNewsSubscribeCallback = callback;
	}
	
	private void subscribeDataChanged(){
		if(subscribeCallback != null){
			subscribeCallback.OnSubscribeDataChanged();
		}
	}
	
	private void subscribeCatDataStatusChanged(){
		if(subscribeCatCallback != null){
			subscribeCatCallback.OnSubscribeCatDataStatusChanged();
		}
	}
	
	public void setFavoriteCallback(FavoriteCallback callback){
		favoriteCallback = callback;
	}
	
	private void favoriteDataChanged(){
		if(favoriteCallback!=null){
			favoriteCallback.OnFavoriteDataChanged();
		}
	}
	
	private void subscribeLocalData(final Context context){
		List<Subscribe> localSubscribes = SubscribeManager.getInstance().getSubscribeFromTabLocal(context);
		if (localSubscribes != null && localSubscribes.size() > 0) {
			localSubscribeCount = localSubscribes.size();
			for (final Subscribe subscribe : localSubscribes) {
				BackChinaApi.subscribe(subscribe.getId(),
						new TextHttpResponseHandler() {

							@Override
							public void onSuccess(int code, Header[] headers,
									String responseString) {
								// TODO Auto-generated method stub
								localSubscribeCount--;
								boolean result = handleSubscribeResponse(
										headers, responseString);
								if (result) {
									SubscribeManager.getInstance()
											.deleteSubscribeToTabLocal(context,
													subscribe);
								}
								subscribeLocalNotify();
							}

							@Override
							public void onFailure(int code, Header[] headers,
									String responseString, Throwable arg3) {
								// TODO Auto-generated method stub
								localSubscribeCount--;
								subscribeLocalNotify();
							}
						});
			}
		}else{
			subscribeLocalNotify();
		}
	}
	
	private void subscribeLocalNotify(){
		if(localSubscribeCount <= 0){
			subscribeDataChanged();
		}
	}
	
	private boolean handleSubscribeResponse(Header[] headers, String response) {
		Type type = new TypeToken<ActivitiesBean<Subscribe>>() {
        }.getType();
        ActivitiesBean<Subscribe> activitiesBean = AppContext.createGson().fromJson(response, type);
        Subscribe subscribe = activitiesBean.getActivities();
		if (subscribe.getStatus() == null) {
			if (subscribe.getFavid() != null) {
				return true;
			}else{
				return false;
			}
		} else {
			if (subscribe.getStatus().contains("repeat")) {
				return true;
			} else if (subscribe.getStatus().equals("-1")) {
				return false;
			} else if (subscribe.getStatus().equals("-2")) {
				return false;
			} else {
				return false;
			}
		}
	}
	
	//
	private void handleFavoriteLocalData(final Context context){
		List<FavoriteBean> localFavoriteBeans = FavoriteManager.getInstance().getFavoriteBeanFromTabLocal(context);
		if (localFavoriteBeans != null && localFavoriteBeans.size() > 0) {
			localFavoriteCount = localFavoriteBeans.size();
			for(final FavoriteBean favoriteBean : localFavoriteBeans){
				if(favoriteBean.getIdtype().equals("aid")){
					BackChinaApi.favoriteNews(favoriteBean.getId(), new TextHttpResponseHandler() {
						
						@Override
						public void onSuccess(int code, Header[] headers, String responseString) {
							// TODO Auto-generated method stub
							localFavoriteCount--;
							boolean result = handleFavoriteResp(context,responseString);
							if (result) {
								FavoriteManager.getInstance().deleteFavoriteBeanFromTabLocal(context, favoriteBean);
							}
							favoriteLocalNotify();
						}
						
						@Override
						public void onFailure(int code, Header[] headers, String responseString, Throwable throwable) {
							// TODO Auto-generated method stub
							localFavoriteCount--;
							favoriteLocalNotify();
						}
					});
				}else if(favoriteBean.getIdtype().equals("blogid")){
					BackChinaApi.favoriteBlog(favoriteBean.getId(),  new TextHttpResponseHandler() {
						
						@Override
						public void onSuccess(int code, Header[] headers, String responseString) {
							// TODO Auto-generated method stub
							localFavoriteCount--;
							boolean result = handleFavoriteResp(context,responseString);
							if (result) {
								FavoriteManager.getInstance().deleteFavoriteBeanFromTabLocal(context, favoriteBean);
							}
							favoriteLocalNotify();
						}
						
						@Override
						public void onFailure(int code, Header[] headers, String responseString, Throwable throwable) {
							// TODO Auto-generated method stub
							localFavoriteCount--;
							favoriteLocalNotify();
						}
					});
				}
			}
		}else{
			favoriteLocalNotify();
		}
	}
	
	private void favoriteLocalNotify(){
		if(localFavoriteCount <= 0){
			//
			favoriteDataChanged();
		}
	}
	
    protected boolean handleFavoriteResp(Context context,String responseString) {
    	// TODO Auto-generated method stub
    	Type type = new TypeToken<ActivitiesBean<FavoriteBean>>() {
        }.getType();
        ActivitiesBean<FavoriteBean> activitiesBean = AppContext.createGson().fromJson(responseString, type);
        FavoriteBean favoriteBean = activitiesBean.getActivities();
        //favorite_repeat
        if (favoriteBean.getStatus() == null) {
        	if (favoriteBean.getFavid() != null) {
        		FavoriteManager.getInstance().saveFavoriteBeanToTabOnline(context, favoriteBean);
        		return true;
        	}else{
        		return false;
        	}
		} else {
			if (favoriteBean.getStatus().equals("favorite_repeat")) {// 重复收藏
				return true;
			} else if (favoriteBean.getStatus().equals("-1")) {// 收藏失败
				return false;
			} else if (favoriteBean.getStatus().equals("-2")) {// 请登录
				return false;
			} else {// 收藏失败
				return false;
			}
		}
    }
    
    private void handleSpecialNewsSubscribe(){
    	if(specicalNewsSubscribeCallback != null){
    		specicalNewsSubscribeCallback.onSubscribe();
    	}
    }

}
