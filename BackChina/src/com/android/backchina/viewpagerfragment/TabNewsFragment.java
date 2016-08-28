package com.android.backchina.viewpagerfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.android.backchina.R;
import com.android.backchina.adapter.ViewPageFragmentAdapter;
import com.android.backchina.base.BaseViewPagerFragment;
import com.android.backchina.fragment.BlogFragment;
import com.android.backchina.fragment.NewsFragment;
import com.android.backchina.fragment.VideoFragment;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.ui.ChannelActivity;

/**
 * 新闻
 */
public class TabNewsFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.news_viewpage_arrays);

        adapter.addTab(title[0], "滚动", NewsFragment.class,
                null);
        adapter.addTab(title[0], "news1", NewsFragment.class,
                null);
        adapter.addTab(title[0], "news2", NewsFragment.class,
                null);
        adapter.addTab(title[0], "news3", NewsFragment.class,
                null);
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        return bundle;
    }

    /**
     * 基类会根据不同的catalog展示相应的数据
     *
     * @param catalog 要显示的数据类别
     * @return
     */
    private Bundle getBundle(String catalog) {
        Bundle bundle = new Bundle();
        return bundle;
    }
    
    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onTabReselect() {
        Fragment fragment = mTabsAdapter.getItem(mViewPager.getCurrentItem());
//        if (fragment != null && fragment instanceof BaseGeneralListFragment) {
//            ((BaseGeneralListFragment) fragment).onTabReselect();
//        }
    }

    @Override
    protected void enterChannelManagerActivity() {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setClass(getActivity(), ChannelActivity.class);
        getActivity().startActivity(intent);
    }

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return 0;
	}
}