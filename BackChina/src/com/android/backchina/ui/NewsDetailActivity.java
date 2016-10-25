
package com.android.backchina.ui;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.backchina.AppContext;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.bean.FavoriteBean;
import com.android.backchina.bean.News;
import com.android.backchina.bean.NewsDetail;
import com.android.backchina.bean.StatusBean;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.fragment.NewsDetailFragment;
import com.android.backchina.manager.FavoriteManager;
import com.android.backchina.ui.dialog.DialogHelper;
import com.android.backchina.ui.dialog.WaitDialog;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class NewsDetailActivity extends BaseDetailActivity{

    private final static String BUNDLE_KEY_NEWS = "BUNDLE_KEY_NEWS";
    private final static String BUNDLE_KEY_IS_FAVORITE = "BUNDLE_KEY_IS_FAVORITE";
    
    private final static String FAVORITE_ID_TYPE = "aid";
    
    private News currentNews;
    
    private NewsDetail mDetail;
    
    private WaitDialog mWaitDialog;
    
    private boolean isFavorite;
    
    public static void show(Context context, long id) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }
    
    public static void show(Context context, News news,boolean isFavorite) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra(BUNDLE_KEY_NEWS, news);
        intent.putExtra(BUNDLE_KEY_IS_FAVORITE, isFavorite);
        context.startActivity(intent);
    }

    @Override
    protected void initBundle(Bundle bundle){
        currentNews = (News) bundle.getSerializable(BUNDLE_KEY_NEWS);
        isFavorite = (boolean) bundle.getSerializable(BUNDLE_KEY_IS_FAVORITE);
        FavoriteBean favoriteBean = null;
        if (AppContext.getInstance().isLogin()) {
			 favoriteBean = FavoriteManager.getInstance().getFavoriteBeanFromTabOnlineById(this,"" + currentNews.getId(), FAVORITE_ID_TYPE);
		}else{
			favoriteBean = FavoriteManager.getInstance().getFavoriteBeanFromTabLocalById(this, ""+currentNews.getId(), FAVORITE_ID_TYPE);
    	}
        isFavorite = (favoriteBean != null) ? true : false;
        if(isFavorite){
        	currentNews.setFavid(favoriteBean.getFavid());
        }
    }
    
    @Override
    protected void setupViews() {
    	// TODO Auto-generated method stub
    	super.setupViews();
    	setTitle("");
    	setCommentCount(currentNews.getComments());
    }
    
    @Override
    protected void initData() {
    	// TODO Auto-generated method stub
    	super.initData();
    	mWaitDialog = DialogHelper.getWaitDialog(this, "正在提交...");
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
        mDetail.setNewsType(currentNews.getNewsType());
        mDetail.setFavorite(isFavorite);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public Fragment getDataViewFragment(){
        return NewsDetailFragment.newInstance();
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
	public void toSeeMoreComments() {
		// TODO Auto-generated method stub
		UIHelper.enterCommentNewsActivity(this,mDetail);
	}
	
    @Override
    public void toFavorite() {
        // TODO Auto-generated method stub
		if (AppContext.getInstance().isLogin()) {
			BackChinaApi.favoriteNews(currentNews.getId(), mFavoriteHandler);
		}else{
			FavoriteBean favoriteBean = new FavoriteBean();
			favoriteBean.setId(currentNews.getId());
			favoriteBean.setFavid("favid_"+currentNews.getId());
			favoriteBean.setIdtype(FAVORITE_ID_TYPE);
			favoriteBean.setSpaceuid("");
			favoriteBean.setTitle(currentNews.getTitle());
			favoriteBean.setDesc("");
			favoriteBean.setDateline(""+System.currentTimeMillis()/1000);
			favoriteBean.setUrl(currentNews.getUrl());
			favoriteBean.setUrlapi(currentNews.getUrlapi());
			FavoriteManager.getInstance().saveFavoriteBeanToTabLocal(this,favoriteBean);
			if (operatorCallBack != null) {
				operatorCallBack.toFavoriteSucess();
			}
			UIHelper.notifyFavoriteDataChanged(this);
			showFavoriteSucessed();
		}
    }
    
    @Override
    public void cancleFavorite(){
    	if (AppContext.getInstance().isLogin()) {
    		String favid = currentNews.getFavid();
			if (!StringUtils.isEmpty(favid)) {
				BackChinaApi.cancleFavoriteNews(favid, mCancleFavoriteHandler);
			}else{
				showCancleFavoriteFailed();
			}
		}else{
			FavoriteManager.getInstance().deleteFavoriteBeanFromTabLocal(this,""+currentNews.getId(),FAVORITE_ID_TYPE);
			if (operatorCallBack != null) {
				operatorCallBack.toCancleFavoriteSucess();
			}
			UIHelper.notifyFavoriteDataChanged(this);
			showCancleFavoriteSucessed();
		}
    }

    @Override
    public void toShare() {
        // TODO Auto-generated method stub
    	shareMsg("分享到",currentNews.getTitle(),currentNews.getUrl());
    }
    
	public void shareMsg(String activityTitle, String msgTitle, String msgText) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain"); // 纯文本
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, activityTitle));
	}

    @Override
    public void toSendComment(final String comment,int cid, int position) {
        // TODO Auto-generated method stub
        if (StringUtils.isEmpty(comment)) {
//            AppContext.showToastShort("评论不能为空");
            return;
        }
        mWaitDialog.show();
        int id = currentNews.getId();
        int type = currentNews.getNewsType();
        BackChinaApi.sendNewsComment(type,id,cid,position,"回帖",comment,new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO Auto-generated method stub
                TLog.d("called");
                mWaitDialog.hide();
                Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // TODO Auto-generated method stub
                TLog.d("called");
                handleCommentResponse(headers,responseString);
                mWaitDialog.hide();
            }
        });
    }
    
    private void handleCommentResponse(Header[] headers,String response) {
    	Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
        }.getType();
        ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson().fromJson(response, type);
        StatusBean statusBean = activitiesBean.getActivities();
        if (statusBean.getStatus().equals("1")) {
        	Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
			if (operatorCallBack != null) {
				operatorCallBack.toSendCommentSucess();
			}
        }else if (statusBean.getStatus().equals("-1")) {
        	Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
        }else if (statusBean.getStatus().equals("-2")) {
        	Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
        }else{
        	Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void handleFavoriteResp(String responseString) {
    	// TODO Auto-generated method stub
    	Type type = new TypeToken<ActivitiesBean<FavoriteBean>>() {
        }.getType();
        ActivitiesBean<FavoriteBean> activitiesBean = AppContext.createGson().fromJson(responseString, type);
        FavoriteBean favoriteBean = activitiesBean.getActivities();
        //favorite_repeat
		if (favoriteBean.getStatus() == null) {
			if (favoriteBean.getFavid() != null) {
				if(currentNews != null){
					currentNews.setFavid(favoriteBean.getFavid());
				}
				if (operatorCallBack != null) {
					operatorCallBack.toFavoriteSucess();
				}
				FavoriteManager.getInstance().saveFavoriteBeanToTabOnline(this, favoriteBean);
				UIHelper.notifyFavoriteDataChanged(this);
				showFavoriteSucessed();
			} else {
				showFavoriteFailed();
			}
		} else {
			if (favoriteBean.getStatus().equals("favorite_repeat")) {
				if (operatorCallBack != null) {
					operatorCallBack.toFavoriteSucess();
				}
				UIHelper.notifyFavoriteDataChanged(this);
				showHasFavorited();
			} else if (favoriteBean.getStatus().equals("-1")) {
				showFavoriteFailed();
			} else if (favoriteBean.getStatus().equals("-2")) {
				needLogin();
			} else {
				showFavoriteFailed();
			}
		}
    }
    
    @Override
    protected void handleCancleFavoriteResp(String responseString) {
    	// TODO Auto-generated method stub
    	Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
        }.getType();
        ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson().fromJson(responseString, type);
        StatusBean statusBean = activitiesBean.getActivities();
        //favorite_repeat
        if (statusBean.getStatus().equals("1")) {
        	FavoriteManager.getInstance().deleteFavoriteBeanFromTabOnline(this, ""+currentNews.getId(),FAVORITE_ID_TYPE);
        	if (operatorCallBack != null) {
				operatorCallBack.toCancleFavoriteSucess();
			}
        	UIHelper.notifyFavoriteDataChanged(this);
        	showCancleFavoriteSucessed();
        }else if (statusBean.getStatus().equals("favorite_repeat")) {
        	FavoriteManager.getInstance().deleteFavoriteBeanFromTabOnline(this, ""+currentNews.getId(),FAVORITE_ID_TYPE);
        	if (operatorCallBack != null) {
				operatorCallBack.toCancleFavoriteSucess();
			}
        	UIHelper.notifyFavoriteDataChanged(this);
        	showCancleFavoriteSucessed();
        }else if (statusBean.getStatus().equals("-1")) {
        	showCancleFavoriteFailed();
        }else if (statusBean.getStatus().equals("-2")) {
        	needLogin();
        }else{
        	showCancleFavoriteFailed();
        }
    }

}
