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

import com.android.backchina.bean.FavoriteBean;
import com.android.backchina.bean.Subscribe;
import com.android.backchina.db.DataProvider;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;

public class SubscribeManager {

	public final static String SUBSCRIBE_ID_TYPE_SEARCHID = "searchid";
	public final static String SUBSCRIBE_ID_TYPE_UID = "uid";
	
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

	//
	
	public boolean isSearchIdType(Subscribe subscribe){
		if(subscribe != null 
				&& !StringUtils.isEmpty(subscribe.getIdtype()) 
				&& subscribe.getIdtype().equals(SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isUIdType(Subscribe subscribe){
		if(subscribe != null 
				&& !StringUtils.isEmpty(subscribe.getIdtype()) 
				&& subscribe.getIdtype().equals(SubscribeManager.SUBSCRIBE_ID_TYPE_UID)){
			return true;
		}else{
			return false;
		}
	}
	
	//local
	public Subscribe getSubscribeFromTabLocalById(Context context,String id,String idType){
		List<Subscribe> result = querrySubscribeById(context,DataProvider.SUBSCRIBE_LOCAL_URI,id,idType);
		if(result != null && result.size() > 0){
			return result.get(0);
		}else{
			return null;
		}
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
	
	public void deleteSubscribeToTabLocal(Context context, String id, String idType) {
		deleteSubscribeByIdFromDb(context, DataProvider.SUBSCRIBE_LOCAL_URI, id, idType);
	}

	public List<Subscribe> getSubscribeFromTabLocal(Context context) {
		return getSubscribeListFromDb(context, DataProvider.SUBSCRIBE_LOCAL_URI);
	}
	
	//online
	public Subscribe getSubscribeFromTabOnlineById(Context context,String id,String idType){
		List<Subscribe> result = querrySubscribeById(context,DataProvider.SUBSCRIBE_ONLINE_URI,id,idType);
		if(result != null && result.size() > 0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	public void saveSubscribeToTabOnline(Context context, Subscribe subscribe) {
		saveSubscribeToDb(context, DataProvider.SUBSCRIBE_ONLINE_URI, subscribe);
	}
	
	public void deleteSubscribeTabOnline(Context context, Subscribe subscribe) {
		deleteSubscribeToDb(context, DataProvider.SUBSCRIBE_ONLINE_URI, subscribe);
	}
	
	public void deleteSubscribeTabOnline(Context context, String id, String idType) {
		deleteSubscribeByIdFromDb(context, DataProvider.SUBSCRIBE_ONLINE_URI, id, idType);
	}
	
	//

	private ContentValues subscribeToContentValues(Subscribe subscribe) {
		ContentValues values = null;
		if (subscribe != null) {
			values = new ContentValues();
			values.put(DataProvider.SUBSCRIBE_ID, subscribe.getId());
			values.put(DataProvider.SUBSCRIBE_FAVID, subscribe.getFavid());
			values.put(DataProvider.SUBSCRIBE_TYPE, subscribe.getType());
			values.put(DataProvider.SUBSCRIBE_ID_TYPE, subscribe.getIdtype());
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
		subscribe.setIdtype(cursor.getString(cursor
				.getColumnIndex(DataProvider.SUBSCRIBE_ID_TYPE)));
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

	private List<Subscribe> querrySubscribeById(Context context, Uri uri,String id,String idType){
		List<Subscribe> localSubscribe = null;
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = contentResolver.query(uri, null, "id=? and idtype=?", new String[]{id,idType}, null);
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
	
	private void deleteSubscribeByIdFromDb(Context context, Uri url, String id,String idType) {
		try {
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			Builder b = null;
			//
				b = ContentProviderOperation.newDelete(url).withSelection("id=? and idtype=?",new String[]{id,idType});
				ops.add(b.build());
			//
			ContentResolver contentResolver = context.getContentResolver();
			contentResolver.applyBatch(DataProvider.DATA_AUTHORITY, ops);
		} catch (Exception e) {
			// TODO: handle exception
			TLog.e(e.toString());
			e.printStackTrace();
		}
	}
	
	
	//static
	
	
}
