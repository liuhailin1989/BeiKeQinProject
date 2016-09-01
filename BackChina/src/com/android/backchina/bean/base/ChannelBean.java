package com.android.backchina.bean.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChannelBean<T> implements Serializable{

	private List<T> result = new ArrayList<T>();
	
	private long version;
	
    public List<T> getItems() {
        return result;
    }

    public void setItems(List<T> itemlist) {
        this.result = itemlist;
    }

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
}
