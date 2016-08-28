package com.android.backchina.bean.base;

import java.io.Serializable;
import java.util.List;

public class PageBean<T> implements Serializable {
	
    private List<T> newslist;
    
    public List<T> getItems() {
        return newslist;
    }

    public void setItems(List<T> itemlist) {
        this.newslist = itemlist;
    }
}