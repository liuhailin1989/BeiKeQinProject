package com.android.backchina.bean;

import java.util.List;


/**
 * 博客详情
 * @param <T>
 */
public class BlogDetail<T> extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 745201044989421671L;
	
    private String content;

    /**
     * 发布日期
     */
    private long dateline;
    
    
    private List<T> blogcomments;
    
    private boolean isFavorite;
    
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
	
	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
}
