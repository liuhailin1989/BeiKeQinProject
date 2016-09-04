package com.android.backchina.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.backchina.base.BaseActivity;

public class SubscribeDetailActivity extends BaseActivity{

	public static void show(Context context){
		Intent intent = new Intent(context, SubscribeDetailActivity.class);
        context.startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
}
