package com.news.soft.backchina.bean;

public class FavoriteBean extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8224626535242994167L;
	
	private String status;
	
	private String favid;//
    
    private String idtype;//
    
    private String spaceuid;//
    
    private String title;
    
    private String desc;//

    private String dateline;

    private String url;

    private String urlapi;
    
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFavid() {
		return favid;
	}

	public void setFavid(String favid) {
		this.favid = favid;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getSpaceuid() {
		return spaceuid;
	}

	public void setSpaceuid(String spaceuid) {
		this.spaceuid = spaceuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDateline() {
		return dateline;
	}

	public void setDateline(String dateline) {
		this.dateline = dateline;
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
