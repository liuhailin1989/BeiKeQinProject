package com.android.backchina.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "backchina.db";// 数据库名称
	public static final int VERSION = 1;
	
	public static final String TABLE_NEWS_CHANNEL = "news_channel";//频道管理表

	public static final String CHANNEL_ID = "id";//
	public static final String CHANNEL_NAME = "name";
	public static final String CHANNEL_ORDERID = "orderId";
	public static final String CHANNEL_SELECTED = "selected";
	
	private Context context;
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	public Context getContext(){
		return context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 创建数据库后，对数据库的操作
		String sql = "create table if not exists "+TABLE_NEWS_CHANNEL +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				CHANNEL_ID + " INTEGER , " +
				CHANNEL_NAME + " TEXT , " +
				CHANNEL_ORDERID + " INTEGER , " +
				CHANNEL_SELECTED + " SELECTED)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 更改数据库版本的操作
		onCreate(db);
	}

}
