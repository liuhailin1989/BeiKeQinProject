
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

public class NewsDetailActivity extends BaseActivity implements IContractDetail{

    private EmptyLayout mEmptyLayout;
    
    private final static String BUNDLE_KEY_NEWS = "BUNDLE_KEY_NEWS";
    
    private News currentNews;
    
    private NewsDetail mDetail;
    
    private ImageView btnBack;
    
    private TextView tvCommentCount;
    
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
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initBundle(getIntent().getExtras());
        setContentView(R.layout.activity_detail_layout);
        setupViews();
        initData();
    }
    
    private void initBundle(Bundle bundle){
        currentNews = (News) bundle.getSerializable(BUNDLE_KEY_NEWS);
    }
    
    private void setupViews(){
        mEmptyLayout = (EmptyLayout) findViewById(R.id.lay_error);
        btnBack = (ImageView) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        tvCommentCount = (TextView) findViewById(R.id.tv_comment_count);
    }
    
    private void initData() {
        if (currentNews != null) {
            tvCommentCount.setText(String.valueOf(currentNews.getComments()));
        } else {
            tvCommentCount.setText("0");
        }
        requestData();
    }
    
    public void requestData(){
        BackChinaApi.getNewsDetail(currentNews.getUrlapi(), getRequestHandler());
    }
    
    public AsyncHttpResponseHandler getRequestHandler() {
        return new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                throwable.printStackTrace();
                // if (isDestroy())
                // return;
                // showError(EmptyLayout.NETWORK_ERROR);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // if (isDestroy())
                // return;
                if (!handleData(responseString)) {
                    showError(EmptyLayout.NODATA);
                }
            }
        };
    }
    
    private Type getType(){
        return new TypeToken<ResultBean<NewsDetail>>() {}.getType();
    }
    
    private boolean handleData(String responseString) {
        try {
        ResultBean<NewsDetail> bean = AppContext.createGson().fromJson(responseString, getType());
        mDetail = bean.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        handleView();
        return true;
    }
    
    private void handleView() {
        try {
            Fragment fragment = getDataViewFragment().newInstance();
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.lay_container, fragment);
            trans.commitAllowingStateLoss();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void showError(int type) {
        EmptyLayout layout = mEmptyLayout;
        if (layout != null) {
            layout.setErrorType(type);
        }
    }
    
    Class<? extends DetailFragment> getDataViewFragment(){
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
        mEmptyLayout.setVisibility(View.GONE);
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
        String url = "http://www.backchina.com/plugin.php?id=bkc_app_androidphone:user&func=reply&aid="+id+"&subject=test&message=verygood";
        BackChinaApi.sendNewsComment(url,  new TextHttpResponseHandler() {

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
