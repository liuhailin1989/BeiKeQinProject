package com.android.backchina.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.backchina.R;
import com.android.backchina.base.BaseFragment;
import com.android.backchina.interf.OnTabReselectListener;

public class BlogFragment extends BaseFragment implements OnTabReselectListener {

    @Override
    public void onTabReselect() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view =  inflater.inflate(R.layout.view_error_layout, null);
        view.setBackgroundColor(getResources().getColor(R.color.red));
        return view;
    }
}
