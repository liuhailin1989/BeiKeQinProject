package com.news.soft.backchina.bean;

public class City extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3140699534386360890L;

	private String title;
	private String citle;
	private String ftitle;
	private String url;
	private String urlapi;
	
	private int listItemType;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCitle() {
		return citle;
	}

	public void setCitle(String citle) {
		this.citle = citle;
	}

	public String getFtitle() {
		return ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlapi() {
		return urlapi;
	}

	public void setUrlapi(String urlapi) {
		this.urlapi = urlapi;
	}

	public int getListItemType() {
		return listItemType;
	}

	public void setListItemType(int listItemType) {
		this.listItemType = listItemType;
	}
}
