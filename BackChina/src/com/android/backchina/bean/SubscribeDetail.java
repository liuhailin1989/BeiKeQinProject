package com.android.backchina.bean;

public class SubscribeDetail extends BaseBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 236046370834549311L;

    
    /**
     * 发布日期
     */
    private String dateline;
    
    private String favid;
    
    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }
    
	public String getFavid() {
		return favid;
	}

	public void setFavid(String favid) {
		this.favid = favid;
	}
}
