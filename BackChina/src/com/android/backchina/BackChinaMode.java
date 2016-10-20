package com.android.backchina;

import java.lang.reflect.Type;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.bean.StatusBean;
import com.android.backchina.bean.Subscribe;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.interf.SubscribeCallback;
import com.android.backchina.manager.SubscribeManager;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class BackChinaMode extends BroadcastReceiver{

	public final static String ACTION_SUBSCRIBE_DATA_CHANGED = "backchina.action.SUBSCRIBE_DATA_CHANGED";
	
	
	private SubscribeCallback subscribeCallback;
	
	private int count = 0;
	
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
			count = localSubscribes.size();
			for (final Subscribe subscribe : localSubscribes) {
				BackChinaApi.subscribe(subscribe.getId(),
						new TextHttpResponseHandler() {

							@Override
							public void onSuccess(int code, Header[] headers,
									String responseString) {
								// TODO Auto-generated method stub
								count--;
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
								count--;
								subscribeLocalNotify();
							}
						});
			}
		}else{
			subscribeLocalNotify();
		}
	}
	
	private void subscribeLocalNotify(){
		if(count <= 0){
			subscribeDataChanged();
		}
	}
	
	private boolean handleSubscribeResponse(Header[] headers, String response) {
		Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
		}.getType();
		ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson()
				.fromJson(response, type);
		StatusBean statusBean = activitiesBean.getActivities();
		if (statusBean.getStatus().equals("1")) {
			return true;
		} else if (statusBean.getStatus().equals("-1")) {
//			Toast.makeText(getContext(), "订阅失败", Toast.LENGTH_SHORT).show();
			return false;
		} else if (statusBean.getStatus().equals("-2")) {
//			Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
			return false;
		} else {
//			Toast.makeText(getContext(), "订阅失败", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

}
