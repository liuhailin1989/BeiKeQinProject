package com.news.soft.backchina.bean;

public class SubscribeCat extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -688943027354170745L;

	private int catid;
	
	private String name;
	
	private String urlapi;

	public int getCatid() {
		return catid;
	}

	public void setCatid(int catid) {
		this.catid = catid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrlapi() {
		return urlapi;
	}

	public void setUrlapi(String urlapi) {
		this.urlapi = urlapi;
	}
	
	
}
