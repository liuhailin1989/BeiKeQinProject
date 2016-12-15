package com.news.soft.backchina;

import com.news.soft.backchina.bean.News;
import com.news.soft.backchina.ui.PushMessageActivity;
import com.news.soft.backchina.utils.StringUtils;
import com.news.soft.backchina.utils.UIHelper;
import com.news.soft.backchina.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WellcomeActivity extends Activity{
    
    private Handler handler = new Handler();
    
    private Runnable runnable;
    
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
        setContentView(R.layout.activity_wellcome_layout);
        setupView();
        init();
    }
    
    private void initBundle(Bundle bundle){
		if (bundle != null) {
			 idtype = bundle
					.getString(PushMessageActivity.BUNDLE_KEY_MESSAGE_ID_TYPE);
			 id = bundle
					.getString(PushMessageActivity.BUNDLE_KEY_MESSAGE_ID);
			 title = bundle
					.getString(PushMessageActivity.BUNDLE_KEY_MESSAGE_TIELE);
			 url = bundle
					.getString(PushMessageActivity.BUNDLE_KEY_MESSAGE_URL);
			 urlapi = bundle
					.getString(PushMessageActivity.BUNDLE_KEY_MESSAGE_URLAPI);
		}
	}
    
	private void setupView() {
	    handler.removeCallbacks(runnable);
	}
    
    private void init(){
        runnable = new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                redirectTo();
            }
        };
        handler.postDelayed(runnable, 2000);
    } 
    
    private void redirectTo(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        entryNewsDetail();
        finish();
    }
    
	private void entryNewsDetail(){
		if(!StringUtils.isEmpty(idtype) && idtype.equals("aid")){
			News news = new News();
			news.setId(Integer.parseInt(id));
			news.setTitle(title);
			news.setUrl(url);
			news.setUrlapi(urlapi);
			UIHelper.enterNewsDetail(this, news,true);
		}
	}
	
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
    
}
