package com.news.soft.backchina.ui;

import com.news.soft.backchina.R;
import com.news.soft.backchina.fragment.TabMeFragment;
import com.news.soft.backchina.fragment.TabSubscribeFragment;
import com.news.soft.backchina.viewpagerfragment.TabBlogFragment;
import com.news.soft.backchina.viewpagerfragment.TabNewsFragment;
import com.news.soft.backchina.viewpagerfragment.TabVideoFragment;


public enum MainTab {
    
    NEWS(0,R.string.tab_name_news,R.drawable.tab_drawable_news,TabNewsFragment.class),
    
    BLOG(1,R.string.tab_name_blog,R.drawable.tab_drawable_blog,TabBlogFragment.class),
    
    SUBSCRIBE(2,R.string.tab_name_subscribe,R.drawable.tab_drawable_subscribe,TabSubscribeFragment.class),
    
    VIDEO(2,R.string.tab_name_video,R.drawable.tab_drawable_video,TabVideoFragment.class),
    
    ME(3,R.string.tab_name_me,R.drawable.tab_drawable_me,TabMeFragment.class);
    
    private int index;
    private int titleId;
    private int drawableId;
    private Class<?> clz;
    
    private MainTab(int index, int titleId, int drawableId, Class<?> clz){
        this.index = index;
        this.titleId = titleId;
        this.drawableId = drawableId;
        this.clz = clz;
    }
    
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
