package com.android.backchina.bean;

/** 
 * 资讯详情
 */
public class NewsDetail extends Entity {
	
	/**
     * 
     */
    private static final long serialVersionUID = 3315333109780307014L;
    
    private News news;

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}
}
