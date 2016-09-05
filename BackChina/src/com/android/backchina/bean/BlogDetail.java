package com.android.backchina.bean;


/**
 * 博客详情
 */
public class BlogDetail extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 745201044989421671L;
	
    private String content;

    /**
     * 发布日期
     */
    private long dateline;
    
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
}
