package com.android.backchina.bean.base;

import java.io.Serializable;

public class BlogCommentBean  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2137540311467581230L;

	private int cid;
	
	private int authorid;
	
	private String author;
	
	private String avatar;
	
	private String avatar_middle;
	
	private String avatar_big;
	
	private long dateline;
	
	private String message;

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getAuthorid() {
		return authorid;
	}

	public void setAuthorid(int authorid) {
		this.authorid = authorid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAvatar_middle() {
		return avatar_middle;
	}

	public void setAvatar_middle(String avatar_middle) {
		this.avatar_middle = avatar_middle;
	}

	public String getAvatar_big() {
		return avatar_big;
	}

	public void setAvatar_big(String avatar_big) {
		this.avatar_big = avatar_big;
	}

	public long getDateline() {
		return dateline;
	}

	public void setDateline(long dateline) {
		this.dateline = dateline;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
