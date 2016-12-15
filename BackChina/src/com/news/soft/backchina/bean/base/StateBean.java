package com.news.soft.backchina.bean.base;

import java.io.Serializable;
import java.util.List;

import com.news.soft.backchina.bean.City;

public class StateBean<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8086740326873085868L;

	private T state;
	
	private List<City> bigcity;
}
