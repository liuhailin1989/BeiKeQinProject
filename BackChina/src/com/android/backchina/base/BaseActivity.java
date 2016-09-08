package com.android.backchina.base;

import java.util.Date;

import com.android.backchina.base.adapter.BaseListAdapter.Callback;
import com.android.backchina.ui.dialog.WaitDialog;
import com.android.backchina.utils.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

public abstract class BaseActivity extends FragmentActivity implements Callback {

	private RequestManager mImgLoader;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
    
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mImgLoader != null) {
        	mImgLoader.onDestroy();
            mImgLoader = null;
        }
    }
    
    
    public int getContentViewId(){
        return 0;
    }
    
    /***
     * 从网络中加载数据
     *
     * @param imageView imageView
     * @param imageUrl 图片地址
     */
    protected void setImageFromNet(ImageView imageView, String imageUrl) {
        setImageFromNet(imageView, imageUrl, 0);
    }

    /***
     * 从网络中加载数据
     *
     * @param imageView imageView
     * @param imageUrl 图片地址
     * @param placeholder 图片地址为空时的资源
     */
    protected void setImageFromNet(ImageView imageView, String imageUrl, int placeholder) {
        ImageLoader.loadImage(getImgLoader(), imageView, imageUrl, placeholder);
    }
    
	@Override
	public RequestManager getImgLoader() {
		// TODO Auto-generated method stub
		if (mImgLoader == null) {
			mImgLoader = Glide.with(this);
		}
		return mImgLoader;
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Date getSystemTime() {
		// TODO Auto-generated method stub
		return new Date();
	}
}
