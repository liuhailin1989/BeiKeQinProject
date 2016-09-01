package com.android.backchina.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.backchina.R;
import com.android.backchina.base.BaseFragment;
import com.android.backchina.utils.UIHelper;

public class TabMeFragment extends BaseFragment{

	private TextView mClickToLogin;
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_me_layout;
	}
	
	@Override
	protected void initBundle(Bundle bundle) {
	    // TODO Auto-generated method stub
	    super.initBundle(bundle);
	}
	
	@Override
	protected void setupViews(View root) {
	    // TODO Auto-generated method stub
	    super.setupViews(root);
	    mClickToLogin = (TextView) root.findViewById(R.id.tv_click_login);
	    mClickToLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				UIHelper.enterLoginActivity(getActivity());
			}
		});
	}
	
	@Override
	protected void initData() {
	    // TODO Auto-generated method stub
	    super.initData();
	}
    
}
