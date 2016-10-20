package com.android.backchina.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.backchina.R;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.fragment.MyFavoriteFragment;

public class MyFavoriteActivity extends BaseActivity{

	private TextView mTitleView;
	
	private ImageView mBtnBack;
	
    public static void show(Context context) {
        Intent intent = new Intent(context, MyFavoriteActivity.class);
        context.startActivity(intent);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_layout);
		setupViews();
		initData();
		handleView();
	}
	
	private void setupViews(){
		mTitleView = (TextView) findViewById(R.id.tv_title);
		mBtnBack = (ImageView) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void initData(){
		mTitleView.setText("收藏");
	}
	
    public Fragment getDataViewFragment(){
        return MyFavoriteFragment.newInstance();
    }
    
    private void handleView() {
        try {
        	Fragment fragment = getDataViewFragment();
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.favorite_content, fragment);
            trans.commitAllowingStateLoss();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
