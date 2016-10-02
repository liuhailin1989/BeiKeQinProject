package com.android.backchina.bean;

/**
 * 新闻实体类
 */
public class News extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8399190484147014388L;

	public final static int TYPE_NEWS_NORMAL = 0;//0普通 新闻
	public final static int TYPE_NEWS_LOCAL = 1;//1 本地新闻
	
    /**
     * 发布日期
     */
    private long dateline;
    
    private int newsType = TYPE_NEWS_NORMAL;

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
	
}
