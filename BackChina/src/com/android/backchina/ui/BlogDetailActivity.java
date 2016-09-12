package com.android.backchina.ui;

import java.lang.reflect.Type;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.android.backchina.AppContext;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.bean.Blog;
import com.android.backchina.bean.BlogDetail;
import com.android.backchina.bean.StatusBean;
import com.android.backchina.bean.base.ActivitiesBean;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.fragment.BlogDetailFragment;
import com.android.backchina.fragment.DetailFragment;
import com.android.backchina.ui.dialog.DialogHelper;
import com.android.backchina.ui.dialog.WaitDialog;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class BlogDetailActivity extends BaseDetailActivity {

    private final static String BUNDLE_KEY_BLOG = "BUNDLE_KEY_BLOG";
    
    private Blog mCurrentBlog;
    
    private BlogDetail mBlogDetail;
    
    private WaitDialog mWaitDialog;
    
	public static void show(Context context, Blog blog) {
		Intent intent = new Intent(context, BlogDetailActivity.class);
		intent.putExtra(BUNDLE_KEY_BLOG, blog);
		context.startActivity(intent);
	}

	@Override
	protected void initBundle(Bundle bundle) {
		// TODO Auto-generated method stub
		mCurrentBlog = (Blog) bundle.getSerializable(BUNDLE_KEY_BLOG);
	}
	
	@Override
	protected void setupViews() {
		// TODO Auto-generated method stub
		super.setupViews();
		setTitle("博文详情");
		setCommentCount(mCurrentBlog.getComments());
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
		 BackChinaApi.getNewsDetail(mCurrentBlog.getUrlapi(), mHandler);
	}
	
    private Type getType(){
        return new TypeToken<ResultListBean<BlogDetail>>() {}.getType();
    }
	
	@Override
    public boolean handleData(String responseString) {
        try {
        ResultListBean<BlogDetail> bean = AppContext.createGson().fromJson(responseString, getType());
        mBlogDetail = bean.getItems().get(0);
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
//           AppContext.showToastShort("评论不能为空");
           return;
       }
       mWaitDialog.show();
       int id = mCurrentBlog.getId();
       BackChinaApi.sendNewsComment(id,comment,new TextHttpResponseHandler() {

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
        if (statusBean.getStatus() == 1) {
        	Toast.makeText(getContext(), "评论成功", Toast.LENGTH_SHORT).show();
			if (operatorCallBack != null) {
				operatorCallBack.toSendCommentSucess();
			}
        }else if (statusBean.getStatus() == -1) {
        	Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
        }else if (statusBean.getStatus() == -2) {
        	Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
        }else{
        	Toast.makeText(getContext(), "评论失败", Toast.LENGTH_SHORT).show();
        }
    }
	
}
