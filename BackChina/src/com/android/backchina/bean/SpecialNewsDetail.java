package com.android.backchina.bean;

import java.util.List;

public class SpecialNewsDetail<T> extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1891136134207596340L;
	
	
    private String content;
    
    /**
     * 发布日期
     */
    private long dateline;
    
    private List<T> blogcomments;
    
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
    

	public List<T> getBlogcomments() {
		return blogcomments;
	}

	public void setBlogcomments(List<T> blogcomments) {
		this.blogcomments = blogcomments;
	}
}
