package com.android.backchina.fragment;

import android.view.View;

import com.android.backchina.R;
import com.android.backchina.base.BaseRecyclerViewFragment;
import com.android.backchina.bean.Video;
import com.android.backchina.interf.OnTabReselectListener;

public class VideoFragment extends BaseRecyclerViewFragment<Video> implements OnTabReselectListener {

	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.test_layout;
	}
	
	@Override
	protected void setupViews(View root) {
		// TODO Auto-generated method stub
//		super.setupViews(root);
	}
	
    @Override
    public void onTabReselect() {
        // TODO Auto-generated method stub
        
    }
}
