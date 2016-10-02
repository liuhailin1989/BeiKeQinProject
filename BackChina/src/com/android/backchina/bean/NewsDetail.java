package com.android.backchina.bean;

import java.util.List;

/** 
 * 资讯详情
 */
public class NewsDetail extends BaseBean {
	
	/**
     * 
     */
    private static final long serialVersionUID = 3315333109780307014L;
    
    private String content;
    
    /**
     * 发布日期
     */
    private long dateline;
    
    private int newsType = News.TYPE_NEWS_NORMAL;
    
    private List<News> related_b;
    

	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }
    
    public int getNewsType() {
		return newsType;
	}

	public void setNewsType(int newsType) {
		this.newsType = newsType;
	}
    
    public List<News> getRelated_b() {
		return related_b;
	}

	public void setRelated_b(List<News> related_b) {
		this.related_b = related_b;
	}
}
