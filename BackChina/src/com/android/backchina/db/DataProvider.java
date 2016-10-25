package com.android.backchina.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class DataProvider extends ContentProvider{

    private static final String DB_NAME = "backchina_data.db";
    
    private static final int DB_VERSION = 1;
    
    private DatabaseHelper mDbHelper;
    
    public static final String DATA_AUTHORITY = "com.android.backchina.DataProvider";
    
    public static final String _ID = "_id";
    
    //channel_news_local
    public static final int CODE_CHANNEL_NEWS_LOCAL = 1;
    public static final String TAB_CHANNEL_NEWS_LOCAL = "channel_news_local";
    public static final Uri CHANNEL_NEWS_LOCAL_URI = Uri.parse("content://"+DATA_AUTHORITY+"/" + TAB_CHANNEL_NEWS_LOCAL);
    
    //channel_news_all
    public static final int CODE_CHANNEL_NEWS_ALL = 2;
    public static final String TAB_CHANNEL_NEWS_ALL = "channel_news_all";
    public static final Uri CHANNEL_NEWS_ALL_URI = Uri.parse("content://"+DATA_AUTHORITY+"/" + TAB_CHANNEL_NEWS_ALL);
    
    //channel_blog_all
    public static final int CODE_CHANNEL_BLOG_LOCAL = 3;
    public static final String TAB_CHANNEL_BLOG_LOCAL = "channel_blog_local";
    public static final Uri CHANNEL_BLOG_LOCAL_URI = Uri.parse("content://"+DATA_AUTHORITY+"/" + TAB_CHANNEL_BLOG_LOCAL);
    
    //channel_blog_all
    public static final int CODE_CHANNEL_BLOG_ALL = 4;
    public static final String TAB_CHANNEL_BLOG_ALL = "channel_blog_all";
    public static final Uri CHANNEL_BLOG_ALL_URI = Uri.parse("content://"+DATA_AUTHORITY+"/" + TAB_CHANNEL_BLOG_ALL);
    
    //channel_video_all
    public static final int CODE_CHANNEL_VIDEO_LOCAL = 5;
    public static final String TAB_CHANNEL_VIDEO_LOCAL = "channel_video_local";
    public static final Uri CHANNEL_VIDEO_LOCAL_URI = Uri.parse("content://"+DATA_AUTHORITY+"/" + TAB_CHANNEL_VIDEO_LOCAL);
    
    //channel_video_all
    public static final int CODE_CHANNEL_VIDEO_ALL = 6;
    public static final String TAB_CHANNEL_VIDEO_ALL = "channel_video_all";
    public static final Uri CHANNEL_VIDEO_ALL_URI = Uri.parse("content://"+DATA_AUTHORITY+"/" + TAB_CHANNEL_VIDEO_ALL);
    
    public static final String CHANNEL_ID = "id";
    public static final String CHANNEL_TYPEID = "typeid";
    public static final String CHANNEL_NAME = "name";
    public static final String CHANNEL_URL = "url";
    public static final String CHANNEL_URLAPI = "urlapi";
    
    //subscribe
    public static final int CODE_SUBSCRIBE_LOCAL = 7;
    public static final String TAB_SUBSCRIBE_LOCAL = "subscribe_local";
    public static final Uri SUBSCRIBE_LOCAL_URI = Uri.parse("content://"+DATA_AUTHORITY+"/" + TAB_SUBSCRIBE_LOCAL);
    
    public static final String SUBSCRIBE_ID = "id";
    public static final String SUBSCRIBE_FAVID = "favid";
    public static final String SUBSCRIBE_TYPE = "type";
    public static final String SUBSCRIBE_TITLE = "title";
    public static final String SUBSCRIBE_LOGO = "logo";
    public static final String SUBSCRIBE_URL = "url";
    public static final String SUBSCRIBE_URLAPI = "urlapi";
    
    //favorite
    public static final int CODE_FAVORITE_LOCAL = 8;
    public static final String TAB_FAVORITE_LOCAL = "favorite_local";
    public static final Uri FAVORITE_LOCAL_URI = Uri.parse("content://"+DATA_AUTHORITY+"/" + TAB_FAVORITE_LOCAL);
    
    public static final int CODE_FAVORITE_ONLINE = 9;
    public static final String TAB_FAVORITE_ONLINE = "favorite_online";
    public static final Uri FAVORITE_ONELINE_URI = Uri.parse("content://"+DATA_AUTHORITY+"/" + TAB_FAVORITE_ONLINE);
    
    public static final String FAVORITE_ID = "id";
    public static final String FAVORITE_FAVID = "favid";
    public static final String FAVORITE_IDTYPE = "idtype";
    public static final String FAVORITE_SPACEUID = "spaceuid";
    public static final String FAVORITE_TITLE = "title";
    public static final String FAVORITE_DESC = "desc";
    public static final String FAVORITE_DATELINE = "dateline";
    public static final String FAVORITE_URL = "url";
    public static final String FAVORITE_URLAPI = "urlapi";
    
    
    
    public static final UriMatcher uriMatcher;
    
    static { 
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DATA_AUTHORITY, TAB_CHANNEL_NEWS_LOCAL, CODE_CHANNEL_NEWS_LOCAL);
        uriMatcher.addURI(DATA_AUTHORITY, TAB_CHANNEL_NEWS_ALL, CODE_CHANNEL_NEWS_ALL);
        uriMatcher.addURI(DATA_AUTHORITY, TAB_CHANNEL_BLOG_LOCAL, CODE_CHANNEL_BLOG_LOCAL);
        uriMatcher.addURI(DATA_AUTHORITY, TAB_CHANNEL_BLOG_ALL, CODE_CHANNEL_BLOG_ALL);
        uriMatcher.addURI(DATA_AUTHORITY, TAB_CHANNEL_VIDEO_LOCAL, CODE_CHANNEL_VIDEO_LOCAL);
        uriMatcher.addURI(DATA_AUTHORITY, TAB_CHANNEL_VIDEO_ALL, CODE_CHANNEL_VIDEO_ALL);
        //subscribe
        uriMatcher.addURI(DATA_AUTHORITY, TAB_SUBSCRIBE_LOCAL, CODE_SUBSCRIBE_LOCAL);
        //favorite
        uriMatcher.addURI(DATA_AUTHORITY, TAB_FAVORITE_LOCAL, CODE_FAVORITE_LOCAL);
        uriMatcher.addURI(DATA_AUTHORITY, TAB_FAVORITE_ONLINE, CODE_FAVORITE_ONLINE);
    }
    
    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        final Context context = getContext();
        if(mDbHelper == null){
            mDbHelper = new DatabaseHelper(context);
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // TODO Auto-generated method stub
        Cursor c = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int codeResult = uriMatcher.match(uri);
        switch (codeResult) {
            case CODE_CHANNEL_NEWS_LOCAL:
                c = db.query(TAB_CHANNEL_NEWS_LOCAL, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_CHANNEL_NEWS_ALL:
                c = db.query(TAB_CHANNEL_NEWS_ALL, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_CHANNEL_BLOG_LOCAL:
                c = db.query(TAB_CHANNEL_BLOG_LOCAL, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_CHANNEL_BLOG_ALL:
                c = db.query(TAB_CHANNEL_BLOG_ALL, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_CHANNEL_VIDEO_LOCAL:
                c = db.query(TAB_CHANNEL_VIDEO_LOCAL, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_CHANNEL_VIDEO_ALL:
                c = db.query(TAB_CHANNEL_VIDEO_ALL, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_SUBSCRIBE_LOCAL:
                c = db.query(TAB_SUBSCRIBE_LOCAL, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_FAVORITE_LOCAL:
                c = db.query(TAB_FAVORITE_LOCAL, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CODE_FAVORITE_ONLINE:
                c = db.query(TAB_FAVORITE_ONLINE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return c;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        Uri resultUri = null;
        long id = 0;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int codeResult = uriMatcher.match(uri);
        switch (codeResult) {
            case CODE_CHANNEL_NEWS_LOCAL:
                id = db.insert(TAB_CHANNEL_NEWS_LOCAL, "", values);
                break;
            case CODE_CHANNEL_NEWS_ALL:
                id = db.insert(TAB_CHANNEL_NEWS_ALL, "", values);
                break;
            case CODE_CHANNEL_BLOG_LOCAL:
                id = db.insert(TAB_CHANNEL_BLOG_LOCAL, "", values);
                break;
            case CODE_CHANNEL_BLOG_ALL:
                id = db.insert(TAB_CHANNEL_BLOG_ALL, "", values);
                break;
            case CODE_CHANNEL_VIDEO_LOCAL:
                id = db.insert(TAB_CHANNEL_VIDEO_LOCAL, "", values);
                break;
            case CODE_CHANNEL_VIDEO_ALL:
                id = db.insert(TAB_CHANNEL_VIDEO_ALL, "", values);
                break;
            case CODE_SUBSCRIBE_LOCAL:
                id = db.insert(TAB_SUBSCRIBE_LOCAL, "", values);
                break;
            case CODE_FAVORITE_LOCAL:
                id = db.insert(TAB_FAVORITE_LOCAL, "", values);
                break;
            case CODE_FAVORITE_ONLINE:
                id = db.insert(TAB_FAVORITE_ONLINE, "", values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        resultUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(uri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        int result = 0;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int codeResult = uriMatcher.match(uri);
        switch (codeResult) {
            case CODE_CHANNEL_NEWS_LOCAL:
                result = db.delete(TAB_CHANNEL_NEWS_LOCAL, selection, selectionArgs);
                break;
            case CODE_CHANNEL_NEWS_ALL:
                result = db.delete(TAB_CHANNEL_NEWS_ALL, selection, selectionArgs);
                break;
            case CODE_CHANNEL_BLOG_LOCAL:
                result = db.delete(TAB_CHANNEL_BLOG_LOCAL, selection, selectionArgs);
                break;
            case CODE_CHANNEL_BLOG_ALL:
                result = db.delete(TAB_CHANNEL_BLOG_ALL, selection, selectionArgs);
                break;
            case CODE_CHANNEL_VIDEO_LOCAL:
                result = db.delete(TAB_CHANNEL_VIDEO_LOCAL, selection, selectionArgs);
                break;
            case CODE_CHANNEL_VIDEO_ALL:
                result = db.delete(TAB_CHANNEL_VIDEO_ALL, selection, selectionArgs);
                break;
            case CODE_SUBSCRIBE_LOCAL:
                result = db.delete(TAB_SUBSCRIBE_LOCAL, selection, selectionArgs);
                break;
            case CODE_FAVORITE_LOCAL:
                result = db.delete(TAB_FAVORITE_LOCAL, selection, selectionArgs);
                break;
            case CODE_FAVORITE_ONLINE:
                result = db.delete(TAB_FAVORITE_ONLINE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        int result = 0;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int codeResult = uriMatcher.match(uri);
        switch (codeResult) {
            case CODE_CHANNEL_NEWS_LOCAL:
                result = db.update(TAB_CHANNEL_NEWS_LOCAL, values, selection, selectionArgs);
                break;
            case CODE_CHANNEL_NEWS_ALL:
                result = db.update(TAB_CHANNEL_NEWS_ALL, values, selection, selectionArgs);
                break;
            case CODE_CHANNEL_BLOG_LOCAL:
                result = db.update(TAB_CHANNEL_BLOG_LOCAL, values, selection, selectionArgs);
                break;
            case CODE_CHANNEL_BLOG_ALL:
                result = db.update(TAB_CHANNEL_BLOG_ALL, values, selection, selectionArgs);
                break;
            case CODE_CHANNEL_VIDEO_LOCAL:
                result = db.update(TAB_CHANNEL_VIDEO_LOCAL, values, selection, selectionArgs);
                break;
            case CODE_CHANNEL_VIDEO_ALL:
                result = db.update(TAB_CHANNEL_VIDEO_ALL, values, selection, selectionArgs);
                break;
            case CODE_SUBSCRIBE_LOCAL:
                result = db.update(TAB_SUBSCRIBE_LOCAL, values, selection, selectionArgs);
                break;
            case CODE_FAVORITE_LOCAL:
                result = db.update(TAB_FAVORITE_LOCAL, values, selection, selectionArgs);
                break;
            case CODE_FAVORITE_ONLINE:
                result = db.update(TAB_FAVORITE_ONLINE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }
    
    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            createTabs(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            
        }
        
        private void createTabs(SQLiteDatabase db){
            createChannelNewsLocalTab(db);
            createChannelNewsAllTab(db);
            createChannelBlogLocalTab(db);
            createChannelBlogAllTab(db);
            createChannelVideoLocalTab(db);
            createChannelVideoAllTab(db);
            createSubscribeLocalTab(db);
            createFavoriteLocalTab(db);
            createFavoriteOnlineTab(db);
        }
        
        private void createChannelNewsLocalTab(SQLiteDatabase db){
            try{
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TAB_CHANNEL_NEWS_LOCAL + "(" +
                        _ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + 
                        CHANNEL_ID +" INTEGER default -1," + 
                        CHANNEL_TYPEID + " INTEGER default -1," +
                        CHANNEL_NAME + " VARCHAR," +
                        CHANNEL_URL + " VARCHAR," +
                        CHANNEL_URLAPI + " VARCHAR" +
                        ")"
                        );
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        private void createChannelNewsAllTab(SQLiteDatabase db){
            try{
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TAB_CHANNEL_NEWS_ALL + "(" +
                        _ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + 
                        CHANNEL_ID +" INTEGER default -1," + 
                        CHANNEL_TYPEID + " INTEGER default -1," +
                        CHANNEL_NAME + " VARCHAR," +
                        CHANNEL_URL + " VARCHAR," +
                        CHANNEL_URLAPI + " VARCHAR" +
                        ")"
                        );
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        private void createChannelBlogLocalTab(SQLiteDatabase db){
            try{
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TAB_CHANNEL_BLOG_LOCAL + "(" +
                        _ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + 
                        CHANNEL_ID +" INTEGER default -1," + 
                        CHANNEL_TYPEID + " INTEGER default -1," +
                        CHANNEL_NAME + " VARCHAR," +
                        CHANNEL_URL + " VARCHAR," +
                        CHANNEL_URLAPI + " VARCHAR" +
                        ")"
                        );
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        private void createChannelBlogAllTab(SQLiteDatabase db){
            try{
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TAB_CHANNEL_BLOG_ALL + "(" +
                        _ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + 
                        CHANNEL_ID +" INTEGER default -1," + 
                        CHANNEL_TYPEID + " INTEGER default -1," +
                        CHANNEL_NAME + " VARCHAR," +
                        CHANNEL_URL + " VARCHAR," +
                        CHANNEL_URLAPI + " VARCHAR" +
                        ")"
                        );
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        //
        private void createChannelVideoLocalTab(SQLiteDatabase db){
            try{
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TAB_CHANNEL_VIDEO_LOCAL + "(" +
                        _ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + 
                        CHANNEL_ID +" INTEGER default -1," + 
                        CHANNEL_TYPEID + " INTEGER default -1," +
                        CHANNEL_NAME + " VARCHAR," +
                        CHANNEL_URL + " VARCHAR," +
                        CHANNEL_URLAPI + " VARCHAR" +
                        ")"
                        );
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        private void createChannelVideoAllTab(SQLiteDatabase db){
            try{
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TAB_CHANNEL_VIDEO_ALL + "(" +
                        _ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + 
                        CHANNEL_ID +" INTEGER default -1," + 
                        CHANNEL_TYPEID + " INTEGER default -1," +
                        CHANNEL_NAME + " VARCHAR," +
                        CHANNEL_URL + " VARCHAR," +
                        CHANNEL_URLAPI + " VARCHAR" +
                        ")"
                        );
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        private void createSubscribeLocalTab(SQLiteDatabase db){
            try{
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TAB_SUBSCRIBE_LOCAL + "(" +
                        _ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + 
                        SUBSCRIBE_ID + " INTEGER default -1," +
                        SUBSCRIBE_FAVID + " VARCHAR," +
                        SUBSCRIBE_TYPE +" INTEGER default -1," + 
                        SUBSCRIBE_TITLE + " VARCHAR," +
                        SUBSCRIBE_LOGO + " VARCHAR," +
                        SUBSCRIBE_URL + " VARCHAR," +
                        SUBSCRIBE_URLAPI + " VARCHAR" +
                        ")"
                        );
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        private void createFavoriteLocalTab(SQLiteDatabase db){
            try{
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TAB_FAVORITE_LOCAL + "(" +
                        _ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + 
                        FAVORITE_ID + " INTEGER default -1," +
                        FAVORITE_FAVID + " VARCHAR," +
                        FAVORITE_IDTYPE +" VARCHAR," + 
                        FAVORITE_SPACEUID + " VARCHAR," +
                        FAVORITE_TITLE + " VARCHAR," +
                        FAVORITE_DESC + " VARCHAR," +
                        FAVORITE_DATELINE + " VARCHAR," +
                        FAVORITE_URL + " VARCHAR," +
                        FAVORITE_URLAPI + " VARCHAR" +
                        ")"
                        );
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
        private void createFavoriteOnlineTab(SQLiteDatabase db){
            try{
                db.execSQL("CREATE TABLE IF NOT EXISTS " + TAB_FAVORITE_ONLINE + "(" +
                        _ID +" INTEGER PRIMARY KEY AUTOINCREMENT," + 
                        FAVORITE_ID + " INTEGER default -1," +
                        FAVORITE_FAVID + " VARCHAR," +
                        FAVORITE_IDTYPE +" VARCHAR," + 
                        FAVORITE_SPACEUID + " VARCHAR," +
                        FAVORITE_TITLE + " VARCHAR," +
                        FAVORITE_DESC + " VARCHAR," +
                        FAVORITE_DATELINE + " VARCHAR," +
                        FAVORITE_URL + " VARCHAR," +
                        FAVORITE_URLAPI + " VARCHAR" +
                        ")"
                        );
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    
}
