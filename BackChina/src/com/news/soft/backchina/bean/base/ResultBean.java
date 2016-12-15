package com.news.soft.backchina.bean.base;

import java.io.Serializable;

public class ResultBean<T> implements Serializable{
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_UNKNOW = 0;
    public static final int RESULT_ERROR = -1;
    public static final int RESULT_NOT_FIND = 404;
    public static final int RESULT_NOT_LOGIN = 201;
    public static final int RESULT_TOKEN_EXPRIED = 202;
    public static final int RESULT_NOT_PERMISSION = 203;
    
    private T result;
    
    private int page;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
    
    
}
