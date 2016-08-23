package com.android.backchina.bean;

import java.util.ArrayList;
import java.util.List;

public class BlogList extends Entity implements ListEntity<Blog> {
	
	/**
     * 
     */
    private static final long serialVersionUID = 6630804959655340570L;
    

    public final static String PREF_READED_BLOG_LIST = "readed_blog_list.pref";
	
	public static final String CATALOG_LATEST = "latest";
	
	public static final String  CATALOG_RECOMMEND = "recommend";
	
	private int pagesize;
	
	private List<Blog> bloglist = new ArrayList<Blog>();
	
	private int blogsCount;

	public int getPageSize() {
		return pagesize;
	}

	public void setPageSize(int pageSize) {
		this.pagesize = pageSize;
	}

	public List<Blog> getBloglist() {
		return bloglist;
	}

	public void setBloglist(List<Blog> bloglist) {
		this.bloglist = bloglist;
	}

	@Override
	public List<Blog> getList() {
		return bloglist;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getBlogsCount() {
		return blogsCount;
	}

	public void setBlogsCount(int blogsCount) {
		this.blogsCount = blogsCount;
	}
}
