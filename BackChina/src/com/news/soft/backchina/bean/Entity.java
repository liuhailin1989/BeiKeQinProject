
package com.news.soft.backchina.bean;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8825907945528736250L;

    protected int id;

    protected String cacheKey;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
