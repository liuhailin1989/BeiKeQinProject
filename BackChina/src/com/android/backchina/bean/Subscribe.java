
package com.android.backchina.bean;

public class Subscribe extends Entity {

    /**
     * 
     */
    private static final long serialVersionUID = -8711860084114233572L;

    private int favid;//
    
    private String idtype;//
    
    private int spaceuid;//
    
    private String desc;//
    
    private String title;

    private String title_long;

    private String logo;

    private String logo_middle;

    private String logo_big;

    private String url;

    private String urlapi;


	public int getFavid() {
		return favid;
	}

	public void setFavid(int favid) {
		this.favid = favid;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public int getSpaceuid() {
		return spaceuid;
	}

	public void setSpaceuid(int spaceuid) {
		this.spaceuid = spaceuid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_long() {
        return title_long;
    }

    public void setTitle_long(String title_long) {
        this.title_long = title_long;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo_middle() {
        return logo_middle;
    }

    public void setLogo_middle(String logo_middle) {
        this.logo_middle = logo_middle;
    }

    public String getLogo_big() {
        return logo_big;
    }

    public void setLogo_big(String logo_big) {
        this.logo_big = logo_big;
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
