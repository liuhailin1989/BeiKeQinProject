package com.android.backchina.bean;

public class SpecialNewsDetail extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1891136134207596340L;
	
	
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
