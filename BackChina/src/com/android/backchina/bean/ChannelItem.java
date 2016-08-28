package com.android.backchina.bean;

/** 
 * ITEM的对应可序化队列属性
 *  */
public class ChannelItem extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	
	private int typeid;
	
	private String name;
	
	private String url;
	
	private String urlapi;

	public ChannelItem() {
		
	}

	public int getTypeid() {
		return typeid;
	}

	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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