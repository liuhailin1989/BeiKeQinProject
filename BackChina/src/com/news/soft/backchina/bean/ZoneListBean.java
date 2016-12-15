package com.news.soft.backchina.bean;

import java.util.List;

public class ZoneListBean<T> extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7973584574011675859L;
	
	List<T> city;
	
	private String title;
	private String citle;
	private String ftitle;
	private String url;
	private String urlapi;

	
	public List<T> getCity() {
		return city;
	}

	public void setCity(List<T> city) {
		this.city = city;
	}

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
}
