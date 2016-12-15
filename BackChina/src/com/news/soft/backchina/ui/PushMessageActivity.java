package com.news.soft.backchina.ui;

import android.os.Bundle;

import com.news.soft.backchina.R;
import com.news.soft.backchina.base.BaseActivity;
import com.news.soft.backchina.bean.News;
import com.news.soft.backchina.utils.StringUtils;
import com.news.soft.backchina.utils.UIHelper;

public class PushMessageActivity extends BaseActivity{

	public final static String BUNDLE_KEY_MESSAGE_ID_TYPE = "idtype";
	public final static String BUNDLE_KEY_MESSAGE_ID = "id";
	public final static String BUNDLE_KEY_MESSAGE_TIELE = "title";
	public final static String BUNDLE_KEY_MESSAGE_URL = "url";
	public final static String BUNDLE_KEY_MESSAGE_URLAPI = "urlapi";
	
	private String idtype;
	private String id;
	private String title;
	private String url;
	private String urlapi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initBundle(getIntent().getExtras());
		setContentView(R.layout.activity_test_layout);
		setupViews();
		initData();
	}
	
	private void initBundle(Bundle bundle){
		idtype = bundle.getString(BUNDLE_KEY_MESSAGE_ID_TYPE);
		id = bundle.getString(BUNDLE_KEY_MESSAGE_ID);
		title = bundle.getString(BUNDLE_KEY_MESSAGE_TIELE);
		url = bundle.getString(BUNDLE_KEY_MESSAGE_URL);
		urlapi = bundle.getString(BUNDLE_KEY_MESSAGE_URLAPI);
	}
	
	private void setupViews(){
		
	}
	
	private void initData(){
		if(!StringUtils.isEmpty(idtype) && idtype.equals("aid")){
			News news = new News();
			news.setId(Integer.parseInt(id));
			news.setTitle(title);
			news.setUrl(url);
			news.setUrlapi(urlapi);
			entryNewsDetail(news);
		}
	}
	
	private void entryNewsDetail(News news){
		UIHelper.enterNewsDetail(this, news,true);
		finish();
	}
}
