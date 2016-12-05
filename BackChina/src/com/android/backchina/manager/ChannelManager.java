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

import com.android.backchina.bean.ChannelItem;
import com.android.backchina.db.DataProvider;
import com.android.backchina.utils.TLog;

public class ChannelManager {
    
    private static ChannelManager instance;
    
    
    private ChannelManager() {

    }

    public static ChannelManager getInstance() {
        if (null == instance) {
            synchronized (ChannelManager.class) {
                if (null == instance) {
                    instance = new ChannelManager();
                }
            }
        }
        return instance;
    }
    
    public ContentValues channelItemToContentValues(ChannelItem channelItem){
        ContentValues values = null;
        if(channelItem != null){
            values = new ContentValues();
            values.put(DataProvider.CHANNEL_ID, channelItem.getId());
            values.put(DataProvider.CHANNEL_NAME, channelItem.getName());
            values.put(DataProvider.CHANNEL_TYPEID, channelItem.getTypeid());
            values.put(DataProvider.CHANNEL_URL, channelItem.getUrl());
            values.put(DataProvider.CHANNEL_URLAPI, channelItem.getUrlapi());
        }
        return values;
    }
    
    public ChannelItem cursorToChannelItem(Cursor cursor) {
        ChannelItem item = new ChannelItem();
        item.setId(cursor.getInt(cursor.getColumnIndex(DataProvider.CHANNEL_ID)));
        item.setTypeid(cursor.getInt(cursor.getColumnIndex(DataProvider.CHANNEL_ID)));
        item.setName(cursor.getString(cursor.getColumnIndex(DataProvider.CHANNEL_NAME)));
        item.setUrl(cursor.getString(cursor.getColumnIndex(DataProvider.CHANNEL_URL)));
        item.setUrlapi(cursor.getString(cursor.getColumnIndex(DataProvider.CHANNEL_URLAPI)));
        return item;
    }
    
    public void saveNewsChannelItemToTabLocal(Context context, List<ChannelItem> channelItems) {
        saveChannelItemToDb(context,DataProvider.CHANNEL_NEWS_LOCAL_URI,channelItems);
    }
    
    public void saveNewsChannelItemToTabAll(Context context,List<ChannelItem> channelItems){
        saveChannelItemToDb(context,DataProvider.CHANNEL_NEWS_ALL_URI,channelItems);
    }
    
    public List<ChannelItem> getNewsChannelItemsFromTabLocal(Context context){
        return getChannelItemsFromDb(context,DataProvider.CHANNEL_NEWS_LOCAL_URI);
    }
    
    public List<ChannelItem> getNewsChannelItemsFromTabAll(Context context){
        return getChannelItemsFromDb(context,DataProvider.CHANNEL_NEWS_ALL_URI);
    }
    
    //
    public void saveBlogChannelItemToTabLocal(Context context, List<ChannelItem> channelItems) {
        saveChannelItemToDb(context,DataProvider.CHANNEL_BLOG_LOCAL_URI,channelItems);
    }
    
    public void saveBlogChannelItemToTabAll(Context context,List<ChannelItem> channelItems){
        saveChannelItemToDb(context,DataProvider.CHANNEL_BLOG_ALL_URI,channelItems);
    }
    
    public List<ChannelItem> getBlogChannelItemsFromTabLocal(Context context){
        return getChannelItemsFromDb(context,DataProvider.CHANNEL_BLOG_LOCAL_URI);
    }
    
    public List<ChannelItem> getBlogChannelItemsFromTabAll(Context context){
        return getChannelItemsFromDb(context,DataProvider.CHANNEL_BLOG_ALL_URI);
    }
    
    //
    public void saveVideoChannelItemToTabLocal(Context context, List<ChannelItem> channelItems) {
        saveChannelItemToDb(context,DataProvider.CHANNEL_VIDEO_LOCAL_URI,channelItems);
    }
    
    public void saveVideoChannelItemToTabAll(Context context,List<ChannelItem> channelItems){
        saveChannelItemToDb(context,DataProvider.CHANNEL_VIDEO_ALL_URI,channelItems);
    }
    
    public List<ChannelItem> getVideoChannelItemsFromTabLocal(Context context){
        return getChannelItemsFromDb(context,DataProvider.CHANNEL_VIDEO_LOCAL_URI);
    }
    
    public List<ChannelItem> getVideoChannelItemsFromTabAll(Context context){
        return getChannelItemsFromDb(context,DataProvider.CHANNEL_VIDEO_ALL_URI);
    }
    
    private List<ChannelItem> getChannelItemsFromDb(Context context,Uri uri){
        List<ChannelItem> localChannelItems = null;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, null, "", null, null);
            if (cursor != null && cursor.getCount() > 0) {
                localChannelItems = new ArrayList<ChannelItem>();
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    ChannelItem item = cursorToChannelItem(cursor);
                    localChannelItems.add(item);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        if (cursor != null) {
			cursor.close();
		}
        return localChannelItems;
    }
    
    private void saveChannelItemToDb(Context context,Uri url,List<ChannelItem> channelItems){
        try {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            Builder b = null;
            //
            b = ContentProviderOperation.newDelete(url).withSelection("", null);
            ops.add(b.build());
            //
            ContentResolver contentResolver = context.getContentResolver();
            if (channelItems != null && channelItems.size() > 0) {
                for (ChannelItem channelItem : channelItems) {
                    if (channelItem != null) {
                        ContentValues values = channelItemToContentValues(channelItem);
                        b = ContentProviderOperation.newInsert(url)
                                .withValues(values);
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
