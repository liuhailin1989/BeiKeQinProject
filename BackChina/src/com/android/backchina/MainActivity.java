package com.android.backchina;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.android.backchina.ui.DoubleClickExitHelper;
import com.android.backchina.ui.MainTab;
import com.android.backchina.widget.MainFragmentTabHost;

public class MainActivity extends FragmentActivity implements OnTouchListener,OnTabChangeListener{

    private DoubleClickExitHelper mDoubleClickExit;
    
    public MainFragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppManager.getInstance().addActivity(this);
		init();
		setupView();
	}
	
	private void init(){
	    
	}
	
    private void setupView() {
        mDoubleClickExit = new DoubleClickExitHelper(this);
        mTabHost = (MainFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }
        initTabs();
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setCurrentTab(0);
    }

    private void initTabs(){
        MainTab[] tabs = MainTab.values();
        for (int i = 0; i < tabs.length; i++){
            MainTab mainTab = tabs[i];
            TabSpec tabSpec = mTabHost.newTabSpec(getString(mainTab.getTitleId()));
            View indicator = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = getResources().getDrawable(mainTab.getDrawableId());
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,
                    null);
            title.setText(getString(mainTab.getTitleId()));
            tabSpec.setIndicator(indicator);
            tabSpec.setContent(new TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            mTabHost.addTab(tabSpec, mainTab.getClz(), null);
            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }
    
    @Override
    public void onTabChanged(String tabId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return mDoubleClickExit.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
