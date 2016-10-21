package com.android.backchina;

import java.lang.reflect.Type;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.bean.FavoriteBean;
import com.android.backchina.bean.StatusBean;
import com.android.backchina.bean.Subscribe;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.interf.SubscribeCallback;
import com.android.backchina.manager.FavoriteManager;
import com.android.backchina.manager.SubscribeManager;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class BackChinaMode extends BroadcastReceiver{

	public final static String ACTION_SUBSCRIBE_DATA_CHANGED = "backchina.action.SUBSCRIBE_DATA_CHANGED";
	
	
	private SubscribeCallback subscribeCallback;
	
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
        }else if(Constants.INTENT_ACTION_USER_CHANGE.equals(action)){
        	subscribeLocalData(context);
        	handleFavoriteLocalData(context);
        }
	}
	
	public void setSubscribeCallBack(SubscribeCallback callback){
		subscribeCallback = callback;
	}
	
	private void subscribeDataChanged(){
		if(subscribeCallback != null){
			subscribeCallback.OnSubscribeDataChanged();
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
		Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
		}.getType();
		ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson()
				.fromJson(response, type);
		StatusBean statusBean = activitiesBean.getActivities();
		if (statusBean.getStatus().equals("1")) {//成功
			return true;
		} else if (statusBean.getStatus().equals("-1")) {//失败
			return false;
		} else if (statusBean.getStatus().equals("-2")) {//请登录
			return false;
		} else {//失败
			return false;
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
							boolean result = handleFavoriteResp(responseString);
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
							boolean result = handleFavoriteResp(responseString);
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
		}
	}
	
    protected boolean handleFavoriteResp(String responseString) {
    	// TODO Auto-generated method stub
    	Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
        }.getType();
        ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson().fromJson(responseString, type);
        StatusBean statusBean = activitiesBean.getActivities();
        //favorite_repeat
        if (statusBean.getStatus().equals("1")) {//成功
        	return true;
        }else if (statusBean.getStatus().equals("favorite_repeat")) {//重复收藏
        	return true;
        }else if (statusBean.getStatus().equals("-1")) {//收藏失败
        	return false;
        }else if (statusBean.getStatus().equals("-2")) {//请登录
        	return false;
        }else{//收藏失败
        	return false;
        }
    }

}
