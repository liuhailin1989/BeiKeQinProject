package com.android.backchina.bean;

public class Blog extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7627089707133979158L;
	
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
