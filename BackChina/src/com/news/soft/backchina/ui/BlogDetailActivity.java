package com.news.soft.backchina.ui;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.bean.Blog;
import com.news.soft.backchina.bean.BlogDetail;
import com.news.soft.backchina.bean.FavoriteBean;
import com.news.soft.backchina.bean.StatusBean;
import com.news.soft.backchina.bean.base.ActivitiesBean;
import com.news.soft.backchina.bean.base.BlogCommentBean;
import com.news.soft.backchina.bean.base.ResultListBean;
import com.news.soft.backchina.fragment.BlogDetailFragment;
import com.news.soft.backchina.fragment.DetailFragment;
import com.news.soft.backchina.manager.FavoriteManager;
import com.news.soft.backchina.ui.dialog.DialogHelper;
import com.news.soft.backchina.ui.dialog.WaitDialog;
import com.news.soft.backchina.utils.FileUtil;
import com.news.soft.backchina.utils.StringUtils;
import com.news.soft.backchina.utils.TLog;
import com.news.soft.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class BlogDetailActivity extends BaseDetailActivity {

    private final static String BUNDLE_KEY_BLOG = "BUNDLE_KEY_BLOG";
    private final static String BUNDLE_KEY_IS_FAVORITE = "BUNDLE_KEY_IS_FAVORITE";
    
    private final static String FAVORITE_ID_TYPE = "blogid";
    
    private Blog mCurrentBlog;
    
    private BlogDetail mBlogDetail;
    
    private WaitDialog mWaitDialog;
    
    private boolean isFavorite;
    
	public static void show(Context context, Blog blog,boolean isFavorite) {
		Intent intent = new Intent(context, BlogDetailActivity.class);
		intent.putExtra(BUNDLE_KEY_BLOG, blog);
		intent.putExtra(BUNDLE_KEY_IS_FAVORITE, isFavorite);
		context.startActivity(intent);
	}

	@Override
	protected void initBundle(Bundle bundle) {
		// TODO Auto-generated method stub
		mCurrentBlog = (Blog) bundle.getSerializable(BUNDLE_KEY_BLOG);
		isFavorite = (boolean) bundle.getSerializable(BUNDLE_KEY_IS_FAVORITE);
		FavoriteBean favoriteBean = null;
		if (AppContext.getInstance().isLogin()) {
			favoriteBean = FavoriteManager.getInstance().getFavoriteBeanFromTabOnlineById(this,"" + mCurrentBlog.getId(), FAVORITE_ID_TYPE);
		} else {
			favoriteBean = FavoriteManager.getInstance().getFavoriteBeanFromTabLocalById(this,"" + mCurrentBlog.getId(), FAVORITE_ID_TYPE);
		}
		isFavorite = (favoriteBean != null) ? true : false;
		if (isFavorite) {
			mCurrentBlog.setFavid(favoriteBean.getFavid());
		}
	}
	
	@Override
	protected void setupViews() {
		// TODO Auto-generated method stub
		super.setupViews();
		setTitle("博文详情");
//		setCommentCount(mCurrentBlog.getComments());
		setCommentCount(0);
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		mWaitDialog = DialogHelper.getWaitDialog(this, "正在提交");
	}
	
	@Override
	public void onRequestData() {
		// TODO Auto-generated method stub
		 BackChinaApi.getBlogDetail(mCurrentBlog.getUrlapi(), mHandler);
	}
	
    private Type getType(){
        return new TypeToken<ResultListBean<BlogDetail<BlogCommentBean>>>() {}.getType();
    }
	
	@Override
    public boolean handleData(String responseString) {
        try {
        ResultListBean<BlogDetail<BlogCommentBean>> bean = AppContext.createGson().fromJson(responseString, getType());
        mBlogDetail = bean.getItems().get(0);
        mBlogDetail.setFavorite(isFavorite);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
	@Override
    public Fragment getDataViewFragment(){
        return BlogDetailFragment.newInstance();
    }

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return mBlogDetail;
	}

	@Override
	public void hideLoading() {
		// TODO Auto-generated method stub
		onRequestDataSuccess();
	}
	
    @Override
    public void resfreshTitleComments(int value) {
    	// TODO Auto-generated method stub
    	setCommentCount(value);
    }
    
	@Override
	public void toSeeMoreComments() {
		// TODO Auto-generated method stub
		UIHelper.enterCommentBlogActivity(this, mCurrentBlog);
	}

	@Override
	public void toFavorite() {
		// TODO Auto-generated method stub
		if(AppContext.getInstance().isLogin()){
		    BackChinaApi.favoriteBlog(mCurrentBlog.getId(), mFavoriteHandler);
		}else{
			FavoriteBean favoriteBean = new FavoriteBean();
			favoriteBean.setId(mCurrentBlog.getId());
			favoriteBean.setFavid("favid_"+mCurrentBlog.getId());
			favoriteBean.setIdtype(FAVORITE_ID_TYPE);
			favoriteBean.setSpaceuid("");
			favoriteBean.setTitle(mCurrentBlog.getTitle());
			favoriteBean.setDesc("");
			favoriteBean.setDateline(""+System.currentTimeMillis()/1000);
			favoriteBean.setUrl(mCurrentBlog.getUrl());
			favoriteBean.setUrlapi(mCurrentBlog.getUrlapi());
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
      		String favid = mCurrentBlog.getFavid();
			if (!StringUtils.isEmpty(favid)) {
				BackChinaApi.cancleFavoriteBlog(favid, mCancleFavoriteHandler);
			}else{
      			showCancleFavoriteFailed();
      		}
		}else{
			FavoriteManager.getInstance().deleteFavoriteBeanFromTabLocal(this,""+mCurrentBlog.getId(),FAVORITE_ID_TYPE);
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
		shareMsg("分享到",mCurrentBlog.getTitle(),mCurrentBlog.getUrl());
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
	public void toSendComment(String comment,int cid, int position) {
		// TODO Auto-generated method stub
		 if (StringUtils.isEmpty(comment)) {
//           AppContext.showToastShort("评论不能为空");
           return;
       }
       mWaitDialog.show();
       int id = mCurrentBlog.getId();
       BackChinaApi.sendBlogComment(id,cid,position,comment,new TextHttpResponseHandler() {

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
			BackChinaApi.getNewsDetail(mCurrentBlog.getUrlapi(),
					new TextHttpResponseHandler() {

						@Override
						public void onFailure(int code, Header[] headers,
								String responseString, Throwable arg3) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(int code, Header[] headers,
								String responseString) {
							// TODO Auto-generated method stub
							try {
								ResultListBean<BlogDetail<BlogCommentBean>> bean = AppContext
										.createGson().fromJson(responseString,
												getType());
								mBlogDetail = bean.getItems().get(0);
								if (operatorCallBack != null) {
									operatorCallBack.toSendCommentSucess();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					});
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
        		mCurrentBlog.setFavid(favoriteBean.getFavid());
        		if (operatorCallBack != null) {
					operatorCallBack.toFavoriteSucess();
				}
        		FavoriteManager.getInstance().saveFavoriteBeanToTabOnline(this, favoriteBean);
        		UIHelper.notifyFavoriteDataChanged(this);
        		showFavoriteSucessed();
        	}else{
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
        	if (operatorCallBack != null) {
				operatorCallBack.toCancleFavoriteSucess();
			}
        	FavoriteManager.getInstance().deleteFavoriteBeanFromTabOnline(this, ""+mCurrentBlog.getId(),FAVORITE_ID_TYPE);
        	UIHelper.notifyFavoriteDataChanged(this);
        	showCancleFavoriteSucessed();
        }else if (statusBean.getStatus().equals("favorite_repeat")) {
        	if (operatorCallBack != null) {
				operatorCallBack.toCancleFavoriteSucess();
			}
        	FavoriteManager.getInstance().deleteFavoriteBeanFromTabOnline(this, ""+mCurrentBlog.getId(),FAVORITE_ID_TYPE);
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
