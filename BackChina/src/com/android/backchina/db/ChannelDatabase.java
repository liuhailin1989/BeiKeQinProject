package com.android.backchina.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.backchina.bean.ChannelItem;

public class ChannelDatabase {

	private final DatabaseHelper dbHelper;
	
	public ChannelDatabase(Context context){
		
		 dbHelper = new DatabaseHelper(context);
	}
	
	public void insert(ChannelItem data){
		String sql = "insert into " + DatabaseHelper.TABLE_NAME_CHANNEL;
		sql += "(_id, typeid, name, url, urlapi) values(?, ?, ?, ?, ?)";
		SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
		sqlite.execSQL(sql, new String[] { data.getId() + "", data.getTypeid() + "", data.getName(),
                data.getUrl(), data.getUrlapi() });
        sqlite.close();
		
	}
	
    /**
     * 删
     * 
     * @param id
     */
    public void delete(int id) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("delete from " + DatabaseHelper.TABLE_NAME_CHANNEL + " where _id=?");
        sqlite.execSQL(sql, new Integer[] { id });
        sqlite.close();
    }
    
    /**
     * 改
     * 
     * @param data
     */
    public void update(ChannelItem data) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("update " + DatabaseHelper.TABLE_NAME_CHANNEL + " set typeid=?, name=?, url=?, urlapi=? where _id=?");
        sqlite.execSQL(sql,
                new String[] { data.getTypeid() + "", data.getName(),
                        data.getUrl(), data.getUrlapi(), data.getId() + "" });
        sqlite.close();
    }
    
    /**
     * 查
     * 
     * @param where
     * @return
     */
    public List<ChannelItem> query(String where) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ArrayList<ChannelItem> data = null;
        data = new ArrayList<ChannelItem>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DatabaseHelper.TABLE_NAME_CHANNEL + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
        	ChannelItem channelItem = new ChannelItem();
        	channelItem.setId(cursor.getInt(0));
        	channelItem.setTypeid(cursor.getInt(1));
        	channelItem.setName(cursor.getString(2));
        	channelItem.setUrl(cursor.getString(3));
        	channelItem.setUrlapi(cursor.getString(4));
            data.add(channelItem);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();

        return data;
    }
    
    /**
     * 重置
     * 
     * @param datas
     */
    public void reset(List<ChannelItem> datas) {
        if (datas != null) {
            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
            // 删除全部
            sqlite.execSQL("delete from " + DatabaseHelper.TABLE_NAME_CHANNEL);
            // 重新添加
            for (ChannelItem data : datas) {
                insert(data);
            }
            sqlite.close();
        }
    }
    
    public void save(ChannelItem data) {
        List<ChannelItem> datas = query(" where _id=" + data.getId());
        if (datas != null && !datas.isEmpty()) {
            update(data);
        } else {
            insert(data);
        }
    }
    
    public void destroy() {
        dbHelper.close();
    }
}
