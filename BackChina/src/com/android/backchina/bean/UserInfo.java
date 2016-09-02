package com.android.backchina.bean;

import java.io.Serializable;

public class UserInfo implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -2228001324009921465L;
    
    private int uid;
    
    private String username;
    
    private String avatar;
    
    private int groupid;
    
    private String regdate;//用户注册日期
    
    private int newprompt;
    
    private int friends; //用户好友数
    
    private int doings;// 用户记录
    
    private int blogs;// 用户日志数
    
    private int albums;// 用户相册
    
    private int threads;// 用户主题数
    
    private int views;// 用户访问量

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public int getNewprompt() {
        return newprompt;
    }

    public void setNewprompt(int newprompt) {
        this.newprompt = newprompt;
    }

    public int getFriends() {
        return friends;
    }

    public void setFriends(int friends) {
        this.friends = friends;
    }

    public int getDoings() {
        return doings;
    }

    public void setDoings(int doings) {
        this.doings = doings;
    }

    public int getBlogs() {
        return blogs;
    }

    public void setBlogs(int blogs) {
        this.blogs = blogs;
    }

    public int getAlbums() {
        return albums;
    }

    public void setAlbums(int albums) {
        this.albums = albums;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
