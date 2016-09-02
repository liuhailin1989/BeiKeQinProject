package com.android.backchina.bean.base;

import java.io.Serializable;

public class ActivitiesBean<T> implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 7385514104387236109L;
    
    T activities;

    public T getActivities() {
        return activities;
    }

    public void setActivities(T activities) {
        this.activities = activities;
    }
    
    
}
