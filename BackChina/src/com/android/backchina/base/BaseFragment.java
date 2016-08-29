
package com.android.backchina.base;

import java.io.Serializable;

import com.android.backchina.utils.ImageLoader;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class BaseFragment<T> extends Fragment{

    protected View mRoot;

    protected Bundle mBundle;

    private RequestManager mImgLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TLog.d("called");
        mBundle = getArguments();
        initBundle(mBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	TLog.d("called");
        if (mRoot != null) {
            ViewGroup parent = (ViewGroup) mRoot.getParent();
            if (parent != null)
                parent.removeView(mRoot);
        } else {
            mRoot = inflater.inflate(getLayoutId(), container, false);
            if (savedInstanceState != null) {
                onRestartInstance(savedInstanceState);
            }
            setupViews(mRoot);
            initData();
        }
        return mRoot;
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        TLog.d("called");
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        TLog.d("called");
        RequestManager manager = mImgLoader;
        if (manager != null) {
            manager.onDestroy();
            mImgLoader = null;
        }
//        mRoot = null;
//        mBundle = null;
    }
    
    protected abstract int getLayoutId();

    protected void initBundle(Bundle bundle) {

    }
    
    protected void setupViews(View root) {

    }

    protected void initData() {

    }

    protected <T extends View> T findView(int viewId) {
        return (T) mRoot.findViewById(viewId);
    }

    protected <T extends Serializable> T getBundleSerializable(String key) {
        if (mBundle == null) {
            return null;
        }
        return (T) mBundle.getSerializable(key);
    }

    /**
     * 获取一个图片加载管理器
     *
     * @return RequestManager
     */
    public synchronized RequestManager getImgLoader() {
        if (mImgLoader == null){
            mImgLoader = Glide.with(this);
        }
        return mImgLoader;
    }

    /***
     * 从网络中加载数据
     *
     * @param viewId view的id
     * @param imageUrl 图片地址
     */
    protected void setImageFromNet(int viewId, String imageUrl) {
        setImageFromNet(viewId, imageUrl, 0);
    }

    /***
     * 从网络中加载数据
     *
     * @param viewId view的id
     * @param imageUrl 图片地址
     * @param placeholder 图片地址为空时的资源
     */
    protected void setImageFromNet(int viewId, String imageUrl, int placeholder) {
        ImageView imageView = findView(viewId);
        setImageFromNet(imageView, imageUrl, placeholder);
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

    protected void setText(int viewId, String text) {
        TextView textView = findView(viewId);
        if (StringUtils.isEmpty(text)) {
            return;
        }
        textView.setText(text);
    }

    protected void setText(int viewId, String text, String emptyTip) {
        TextView textView = findView(viewId);
        if (StringUtils.isEmpty(text)) {
            textView.setText(emptyTip);
            return;
        }
        textView.setText(text);
    }

    protected void setTextEmptyGone(int viewId, String text) {
        TextView textView = findView(viewId);
        if (StringUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
            return;
        }
        textView.setText(text);
    }

    protected void setGone(int id) {
        findView(id).setVisibility(View.GONE);
    }

    protected void setVisibility(int id) {
        findView(id).setVisibility(View.VISIBLE);
    }

    protected void setInVisibility(int id) {
        findView(id).setVisibility(View.INVISIBLE);
    }

    protected void onRestartInstance(Bundle bundle) {

    }
}
