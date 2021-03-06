package com.news.soft.backchina.bean.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResultListBean <T> implements Serializable {
    
    private List<T> result = new ArrayList<T>();
    
    private int page;
    
    public List<T> getItems() {
        return result;
    }

    public void setItems(List<T> itemlist) {
        this.result = itemlist;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
    
}
