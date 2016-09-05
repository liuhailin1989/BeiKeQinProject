package com.android.backchina.bean;

/**
 * 新闻实体类
 */
public class News extends BaseBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8399190484147014388L;

	public final static int NEWSTYPE_NEWS = 0x00;//0 新闻
	
    /**
     * 发布日期
     */
    private long dateline;
    
    public long getDateline() {
        return dateline;
    }

    public void setDateline(long dateline) {
        this.dateline = dateline;
    }
	
}
