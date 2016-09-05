package com.android.backchina.ui;

import java.lang.reflect.Type;
import java.util.List;

import com.android.backchina.AppContext;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.bean.News;
import com.android.backchina.bean.NewsDetail;
import com.android.backchina.bean.SpecialNewsDetail;
import com.android.backchina.bean.SubscribeDetail;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.fragment.DetailFragment;
import com.android.backchina.fragment.NewsDetailFragment;
import com.android.backchina.fragment.SpecialNewsDetailFragment;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SpecialNewsDetailActivity extends BaseDetailActivity{

    private final static String BUNDLE_KEY_SUBSCRIBE_DETAIL = "BUNDLE_KEY_SUBSCRIBE_DETAIL";
    
	private SubscribeDetail mCurrentSubscribeDetail;
	
	private SpecialNewsDetail mSpecialNewsDetail;
	
    public static void show(Context context, SubscribeDetail subscribeDetail) {
        Intent intent = new Intent(context, SpecialNewsDetailActivity.class);
        intent.putExtra(BUNDLE_KEY_SUBSCRIBE_DETAIL, subscribeDetail);
        context.startActivity(intent);
    }
	
    @Override
    protected void initBundle(Bundle bundle){
    	mCurrentSubscribeDetail = (SubscribeDetail) bundle.getSerializable(BUNDLE_KEY_SUBSCRIBE_DETAIL);
    }
    
    @Override
    public int getCommentCount() {
    	// TODO Auto-generated method stub
    	if(mCurrentSubscribeDetail != null){
			return mCurrentSubscribeDetail.getComments();
		}
		return 0;
    }
    
    @Override
    public void onRequestData(){
        BackChinaApi.getHttp(mCurrentSubscribeDetail.getUrlapi(), mHandler);
    }
    
    private Type getType(){
        return new TypeToken<ResultBean<List<SpecialNewsDetail>>>() {}.getType();
    }
    
    public boolean handleData(String responseString) {
        try {
        ResultBean<List<SpecialNewsDetail>> bean = AppContext.createGson().fromJson(responseString, getType());
        mSpecialNewsDetail = bean.getResult().get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
	@Override
    public Fragment getDataViewFragment(){
        return SpecialNewsDetailFragment.newInstance();
    }
    
    
	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return mSpecialNewsDetail;
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
//          AppContext.showToastShort("评论不能为空");
          return;
      }
      int id = mSpecialNewsDetail.getId();
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
