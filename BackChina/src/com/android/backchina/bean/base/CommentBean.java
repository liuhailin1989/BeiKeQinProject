package com.android.backchina.bean.base;

import java.util.List;

import com.google.gson.JsonArray;

public class CommentBean<T> {
    
    private List<T> Newscomms;
    
    private int page;
    
    public List<T> getNewscomms() {
        return Newscomms;
    }

    public void setNewscomms(List<T> newscomms) {
        Newscomms = newscomms;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
    
}
