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
import com.android.backchina.db.DataProvider;
import com.android.backchina.utils.TLog;

public class FavoriteManager {

	private static FavoriteManager instance;

	private FavoriteManager() {

	}

	public static FavoriteManager getInstance() {
		if (null == instance) {
			synchronized (FavoriteManager.class) {
				if (null == instance) {
					instance = new FavoriteManager();
				}
			}
		}
		return instance;
	}
	
	public boolean hasFavorited(Context context,String id,String idType){
		List<FavoriteBean> result = queryFavoriteBeanByFavid(context, DataProvider.FAVORITE_LOCAL_URI, id, idType);
		if(result != null && result.size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public FavoriteBean getFavoriteBeanById(Context context,String id,String idType){
		List<FavoriteBean> result = queryFavoriteBeanByFavid(context, DataProvider.FAVORITE_LOCAL_URI, id, idType);
		if(result != null && result.size() > 0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	public void saveFavoriteBeanToTabLocal(Context context,
			List<FavoriteBean> favoriteBeans) {
		saveFavoriteBeanToDb(context, DataProvider.FAVORITE_LOCAL_URI, favoriteBeans);
	}

	public void saveFavoriteBeanToTabLocal(Context context, FavoriteBean favoriteBean) {
		saveFavoriteBeanToDb(context, DataProvider.FAVORITE_LOCAL_URI, favoriteBean);
	}
	
	public void deleteFavoriteBeanFromTabLocal(Context context, FavoriteBean favoriteBean) {
		deleteFavoriteBeanFromDb(context, DataProvider.FAVORITE_LOCAL_URI, favoriteBean);
	}
	public void deleteFavoriteBeanFromTabLocal(Context context, String id, String idType) {
		deleteFavoriteBeanByIdFromDb(context, DataProvider.FAVORITE_LOCAL_URI, id, idType);
	}

	public List<FavoriteBean> getFavoriteBeanFromTabLocal(Context context) {
		return getFavoriteBeanListFromDb(context, DataProvider.FAVORITE_LOCAL_URI);
	}
	
	private ContentValues favoriteBeanToContentValues(FavoriteBean favoriteBean) {
		ContentValues values = null;
		if (favoriteBean != null) {
			values = new ContentValues();
			values.put(DataProvider.FAVORITE_ID, favoriteBean.getId());
			values.put(DataProvider.FAVORITE_FAVID, favoriteBean.getFavid());
			values.put(DataProvider.FAVORITE_IDTYPE, favoriteBean.getIdtype());
			values.put(DataProvider.FAVORITE_SPACEUID, favoriteBean.getSpaceuid());
			values.put(DataProvider.FAVORITE_TITLE, favoriteBean.getTitle());
			values.put(DataProvider.FAVORITE_DESC, favoriteBean.getDesc());
			values.put(DataProvider.FAVORITE_DATELINE, favoriteBean.getDateline());
			values.put(DataProvider.FAVORITE_URL, favoriteBean.getUrl());
			values.put(DataProvider.FAVORITE_URLAPI, favoriteBean.getUrlapi());
		}
		return values;
	}

	private FavoriteBean cursorToFavoriteBean(Cursor cursor) {
		FavoriteBean favoriteBean = new FavoriteBean();
		favoriteBean.setId(cursor.getInt(cursor
				.getColumnIndex(DataProvider.FAVORITE_ID)));
		favoriteBean.setFavid(cursor.getString(cursor
				.getColumnIndex(DataProvider.FAVORITE_FAVID)));
		favoriteBean.setIdtype(cursor.getString(cursor
				.getColumnIndex(DataProvider.FAVORITE_IDTYPE)));
		favoriteBean.setSpaceuid(cursor.getString(cursor
				.getColumnIndex(DataProvider.FAVORITE_SPACEUID)));
		favoriteBean.setTitle(cursor.getString(cursor
				.getColumnIndex(DataProvider.SUBSCRIBE_TITLE)));
		favoriteBean.setDesc(cursor.getString(cursor
				.getColumnIndex(DataProvider.FAVORITE_DESC)));
		favoriteBean.setDateline(cursor.getString(cursor
				.getColumnIndex(DataProvider.FAVORITE_DATELINE)));
		favoriteBean.setUrl(cursor.getString(cursor
				.getColumnIndex(DataProvider.FAVORITE_URL)));
		favoriteBean.setUrlapi(cursor.getString(cursor
				.getColumnIndex(DataProvider.FAVORITE_URLAPI)));
		return favoriteBean;
	}
	
	private List<FavoriteBean> queryFavoriteBeanByFavid(Context context, Uri uri, String id, String idType) {
		List<FavoriteBean> localFavoriteBean = null;
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = contentResolver.query(uri, null, "id=? and idtype=?", new String[]{id,idType}, null);
			if (cursor != null && cursor.getCount() > 0) {
				localFavoriteBean = new ArrayList<FavoriteBean>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					FavoriteBean item = cursorToFavoriteBean(cursor);
					localFavoriteBean.add(item);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return localFavoriteBean;
	}
	//
	private List<FavoriteBean> getFavoriteBeanListFromDb(Context context, Uri uri) {
		List<FavoriteBean> localFavoriteBean = null;
		ContentResolver contentResolver = context.getContentResolver();
		Cursor cursor = null;
		try {
			cursor = contentResolver.query(uri, null, "", null, null);
			if (cursor != null && cursor.getCount() > 0) {
				localFavoriteBean = new ArrayList<FavoriteBean>();
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					FavoriteBean item = cursorToFavoriteBean(cursor);
					localFavoriteBean.add(item);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return localFavoriteBean;
	}

	private void deleteFavoriteBeanByIdFromDb(Context context, Uri url, String id,String idType) {
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
	
	private void deleteFavoriteBeanFromDb(Context context, Uri url, FavoriteBean favoriteBean) {
		try {
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			Builder b = null;
			//
			if (favoriteBean != null) {
				b = ContentProviderOperation.newDelete(url).withSelection("favid = ?",new String[]{favoriteBean.getFavid()});
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
	
	private void saveFavoriteBeanToDb(Context context, Uri url, FavoriteBean favoriteBean) {
		try {
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			Builder b = null;
			//
			if (favoriteBean != null) {
				b = ContentProviderOperation.newDelete(url).withSelection("favid = ?",new String[]{favoriteBean.getFavid()});
				ops.add(b.build());
			}
			//
			ContentResolver contentResolver = context.getContentResolver();
			if (favoriteBean != null) {
				ContentValues values = favoriteBeanToContentValues(favoriteBean);
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

	private void saveFavoriteBeanToDb(Context context, Uri url, List<FavoriteBean> favoriteBeans) {
		try {
			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
			Builder b = null;
			//
			ContentResolver contentResolver = context.getContentResolver();
			if (favoriteBeans != null && favoriteBeans.size() > 0) {
				for (FavoriteBean favoriteBean : favoriteBeans) {
					if (favoriteBean != null) {
						//
						b = ContentProviderOperation.newDelete(url).withSelection("favid = ?",new String[]{favoriteBean.getFavid()});
						ops.add(b.build());
						ContentValues values = favoriteBeanToContentValues(favoriteBean);
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
