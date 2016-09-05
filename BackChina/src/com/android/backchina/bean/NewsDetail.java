package com.android.backchina.bean;

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
