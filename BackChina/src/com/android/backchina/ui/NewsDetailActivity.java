
package com.android.backchina.ui;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.bean.News;
import com.android.backchina.bean.NewsDetail;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.fragment.DetailFragment;
import com.android.backchina.fragment.NewsDetailFragment;
import com.android.backchina.interf.IContractDetail;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class NewsDetailActivity extends BaseDetailActivity{

    private final static String BUNDLE_KEY_NEWS = "BUNDLE_KEY_NEWS";
    
    private News currentNews;
    
    private NewsDetail mDetail;
    
    public static void show(Context context, long id) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
    
    public static void show(Context context, News news) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(BUNDLE_KEY_NEWS, news);
        context.startActivity(intent);
    }

    @Override
    protected void initBundle(Bundle bundle){
        currentNews = (News) bundle.getSerializable(BUNDLE_KEY_NEWS);
    }
    
    @Override
    public int getCommentCount() {
    	// TODO Auto-generated method stub
    	if(currentNews != null){
			return currentNews.getComments();
		}
		return 0;
    }
    
    @Override
    protected void initData() {
    	// TODO Auto-generated method stub
    	super.initData();
    }
    
    @Override
    public void onRequestData(){
        BackChinaApi.getNewsDetail(currentNews.getUrlapi(), mHandler);
    }
    
    private Type getType(){
        return new TypeToken<ResultBean<NewsDetail>>() {}.getType();
    }
    
    public boolean handleData(String responseString) {
        try {
        ResultBean<NewsDetail> bean = AppContext.createGson().fromJson(responseString, getType());
        mDetail = bean.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public Class<? extends DetailFragment> getDataViewFragment(){
        return NewsDetailFragment.class;
    }

    @Override
    public Object getData() {
        // TODO Auto-generated method stub
        return mDetail;
    }

    @Override
    public void hideLoading() {
        // TODO Auto-generated method stub
        onRequestDataSuccess();
    }

    @Override
    public void toFavorite() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void toShare() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void toSendComment(String comment) {
        // TODO Auto-generated method stub
        if (StringUtils.isEmpty(comment)) {
//            AppContext.showToastShort("评论不能为空");
            return;
        }
        int id = currentNews.getId();
        BackChinaApi.sendNewsComment(id,comment,new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                TLog.d("called");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // TODO Auto-generated method stub
                TLog.d("called");
            }
        });
    }
}
