package com.news.soft.backchina;

import java.util.List;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import com.news.soft.backchina.interf.OnTabReselectListener;
import com.news.soft.backchina.ui.DoubleClickExitHelper;
import com.news.soft.backchina.ui.MainTab;
import com.news.soft.backchina.widget.MainFragmentTabHost;
import com.news.soft.backchina.R;

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
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        boolean consumed = false;
        // use getTabHost().getCurrentTabView to decide if the current tab is
        // touched again
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && v.equals(mTabHost.getCurrentTabView())) {
            // use getTabHost().getCurrentView() to get a handle to the view
            // which is displayed in the tab - and to get this views context
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
                consumed = true;
            }
        }
        return consumed;
    }
    
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                mTabHost.getCurrentTabTag());
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return mDoubleClickExit.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
     	  FragmentManager fm = getSupportFragmentManager();
    	  int index = requestCode >> 16;
    	  if (index != 0) {
    	   index--;
    	   if (fm.getFragments() == null || index < 0
    	     || index >= fm.getFragments().size()) {
    	    return;
    	   }
    	   Fragment frag = fm.getFragments().get(index);
    	   if (frag == null) {
    	   } else {
    	    handleResult(frag, requestCode, resultCode, data);
    	   }
    	   return;
    	  }
    }
    /**
     * 递归调用，对所有子Fragement生效
     * 
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
	private void handleResult(Fragment frag, int requestCode, int resultCode,Intent data) {
		frag.onActivityResult(requestCode & 0xffff, resultCode, data);
		List<Fragment> frags = frag.getChildFragmentManager().getFragments();
		if (frags != null) {
			for (Fragment f : frags) {
				if (f != null){
					handleResult(f, requestCode, resultCode, data);
				}
			}
		}
	}
}
