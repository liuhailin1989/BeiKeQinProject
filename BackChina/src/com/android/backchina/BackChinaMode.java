package com.android.backchina;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.backchina.interf.SubscribeCallback;

public class BackChinaMode extends BroadcastReceiver{

	public final static String ACTION_SUBSCRIBE_DATA_CHANGED = "backchina.action.SUBSCRIBE_DATA_CHANGED";
	
	
	private SubscribeCallback subscribeCallback;
	
	
	public BackChinaMode() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		final String action = intent.getAction();
        if (ACTION_SUBSCRIBE_DATA_CHANGED.equals(action)) {
        	subscribeDataChanged();
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

}
