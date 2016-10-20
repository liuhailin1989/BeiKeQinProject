package com.android.backchina.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContentProviderOperation.Builder;
import android.database.Cursor;
import android.net.Uri;

import com.android.backchina.bean.Subscribe;
import com.android.backchina.db.DataProvider;
import com.android.backchina.utils.TLog;

public class SubscribeManager {

	private static SubscribeManager instance;

	private SubscribeManager() {

	}

	public static SubscribeManager getInstance() {
		if (null == instance) {
			synchronized (SubscribeManager.class) {
				if (null == instance) {
					instance = new SubscribeManager();
				}
			}
		}
		return instance;
	}

	public void saveSubscribeToTabLocal(Context context,
			List<Subscribe> subscribes) {
		saveSubscribeToDb(context, DataProvider.SUBSCRIBE_LOCAL_URI, subscribes);
	}

	public void saveSubscribeToTabLocal(Context context, Subscribe subscribe) {
		saveSubscribeToDb(context, DataProvider.SUBSCRIBE_LOCAL_URI, subscribe);
	}
	
	public void deleteSubscribeToTabLocal(Context context, Subscribe subscribe) {
		deleteSubscribeToDb(context, DataProvider.SUBSCRIBE_LOCAL_URI, subscribe);
	}

	public List<Subscribe> getSubscribeFromTabLocal(Context context) {
		return getSubscribeListFromDb(context, DataProvider.SUBSCRIBE_LOCAL_URI);
	}

	private ContentValues subscribeToContentValues(Subscribe subscribe) {
		ContentValues values = null;
		if (subscribe != null) {
			values = new ContentValues();
			values.put(DataProvider.SUBSCRIBE_ID, subscribe.getId());
			values.put(DataProvider.SUBSCRIBE_FAVID, subscribe.getFavid());
			values.put(DataProvider.SUBSCRIBE_TYPE, subscribe.getType());
			values.put(DataProvider.SUBSCRIBE_TITLE, subscribe.getTitle());
			values.put(DataProvider.SUBSCRIBE_LOGO, subscribe.getLogo());
			values.put(DataProvider.SUBSCRIBE_URL, subscribe.getUrl());
			values.put(DataProvider.SUBSCRIBE_URLAPI, subscribe.getUrlapi());
		}
		return values;
	}

	private Subscribe cursorToSubscribe(Cursor cursor) {
		Subscribe subscribe = new Subscribe();
		subscribe.setId(cursor.getInt(cursor
				.getColumnIndex(DataProvider.SUBSCRIBE_ID)));
		subscribe.setFavid(cursor.getString(cursor
				.getColumnIndex(DataProvider.SUBSCRIBE_FAVID)));
		subscribe.setType(cursor.getInt(cursor
				.getColumnIndex(DataProvider.SUBSCRIBE_TYPE)));
		subscribe.setTitle(cursor.getString(cursor
				.getColumnIndex(DataProvider.SUBSCRIBE_TITLE)));
		subscribe.setLogo(cursor.getString(cursor
				.getColumnIndex(DataProvider.SUBSCRIBE_LOGO)));
		subscribe.setUrl(cursor.getString(cursor
				.getColumnIndex(DataProvider.CHANNEL_URL)));
		subscribe.setUrlapi(cursor.getString(cursor
				.getColumnIndex(DataProvider.CHANNEL_URLAPI)));
		return subscribe;
	}

	private List<Subscribe> getSubscribeListFromDb(Context context, Uri uri) {
		List<Subscribe> localSubscribe = null;
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = contentResolver.query(uri, null, "", null, null);
			if (cursor != null && cursor.getCount() > 0) {
				localSubscribe = new ArrayList<Subscribe>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					Subscribe item = cursorToSubscribe(cursor);
					localSubscribe.add(item);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return localSubscribe;
	}

	private void deleteSubscribeToDb(Context context, Uri url, Subscribe subscribe) {
		try {
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			Builder b = null;
			//
			if (subscribe != null) {
				b = ContentProviderOperation.newDelete(url).withSelection("favid = ?",new String[]{subscribe.getFavid()});
				ops.add(b.build());
			}
			//
			ContentResolver contentResolver = context.getContentResolver();
			contentResolver.applyBatch(DataProvider.DATA_AUTHORITY, ops);
		} catch (Exception e) {
			// TODO: handle exception
			TLog.e(e.toString());
			e.printStackTrace();
		}
	}
	
	private void saveSubscribeToDb(Context context, Uri url, Subscribe subscribe) {
		try {
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			Builder b = null;
			//
			if (subscribe != null) {
				b = ContentProviderOperation.newDelete(url).withSelection("favid = ?",new String[]{subscribe.getFavid()});
				ops.add(b.build());
			}
			//
			ContentResolver contentResolver = context.getContentResolver();
			if (subscribe != null) {
				ContentValues values = subscribeToContentValues(subscribe);
				b = ContentProviderOperation.newInsert(url).withValues(values);
				ops.add(b.build());
			}
			contentResolver.applyBatch(DataProvider.DATA_AUTHORITY, ops);
		} catch (Exception e) {
			// TODO: handle exception
			TLog.e(e.toString());
			e.printStackTrace();
		}
	}

	private void saveSubscribeToDb(Context context, Uri url,
			List<Subscribe> subscribes) {
		try {
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			Builder b = null;
			//
			ContentResolver contentResolver = context.getContentResolver();
			if (subscribes != null && subscribes.size() > 0) {
				for (Subscribe subscribe : subscribes) {
					if (subscribe != null) {
						//
						b = ContentProviderOperation.newDelete(url).withSelection("favid = ?",new String[]{subscribe.getFavid()});
						ops.add(b.build());
						ContentValues values = subscribeToContentValues(subscribe);
						b = ContentProviderOperation.newInsert(url).withValues(
								values);
						ops.add(b.build());
					}
				}
			}
			contentResolver.applyBatch(DataProvider.DATA_AUTHORITY, ops);
		} catch (Exception e) {
			// TODO: handle exception
			TLog.e(e.toString());
			e.printStackTrace();
		}
	}
}
