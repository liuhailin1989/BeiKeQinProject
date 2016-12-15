package com.news.soft.backchina.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void show(Context context,int resId){
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
	
	public static void showlong(Context context,int resId){
		Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
	}
	
	public static void show(Context context,String content){
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	
	public static void showlong(Context context,String content){
		Toast.makeText(context, content, Toast.LENGTH_LONG).show();
	}
}
