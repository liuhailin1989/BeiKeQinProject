package com.android.backchina.fragment;

import java.lang.reflect.Type;
import java.util.List;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.R;
import com.android.backchina.adapter.VideoAdapter;
import com.android.backchina.adapter.VideoLinearAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseRecyclerViewFragment;
import com.android.backchina.base.adapter.BaseRecyclerViewAdapter;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.News;
import com.android.backchina.bean.Video;
import com.android.backchina.bean.base.NewsListBean;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.cache.CacheManager;
import com.android.backchina.interf.OnTabReselectListener;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.UIHelper;
import com.android.backchina.widget.RecycleViewItemDecoration;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class VideoLinearFrament extends BaseRecyclerViewFragment<Video> implements OnTabReselectListener {

	private static final String CACHE_KEY_PREFIX = "videolist_";
	
	public static final String BUNDLE_KEY_CATNAME = "BUNDLE_KEY_CATNAME";

	public static final String BUNDLE_KEY_CHANNELITEM = "BUNDLE_KEY_CHANNELITEM";
	
	private String mCatName = "";
	
	private int mCurrentPage = 1;
	
	private ChannelItem currentChannelItem;
	
	protected TextHttpResponseHandler mHandler;
	
	private boolean isLoadMoreAction = false;
	
//	@Override
//	protected int getLayoutId() {
//		// TODO Auto-generated method stub
//		return R.layout.test_layout;
//	}
	
	
	protected String getCacheKeyPrefix() {
		// TODO Auto-generated method stub
		return CACHE_KEY_PREFIX + mCatName;
	}

	private String getCacheKey() {
		return getCacheKeyPrefix() + "_" + 1;//1表示page=1
	}
	
	
	@Override
	protected void initBundle(Bundle bundle) {
		// TODO Auto-generated method stub
		mCatName = bundle.getString(BUNDLE_KEY_CATNAME);
		currentChannelItem = (ChannelItem) bundle
				.getSerializable(BUNDLE_KEY_CHANNELITEM);
	}

	
	@Override
	protected void setupViews(View root) {
		// TODO Auto-generated method stub
		super.setupViews(root);
	}
	
	@Override
	protected BaseRecyclerViewAdapter<Video> getAdapter() {
		// TODO Auto-generated method stub
		return new VideoLinearAdapter(getActivity(), this);
	}

	@Override
	protected LayoutManager getLayoutManager() {
		// TODO Auto-generated method stub
		return new LinearLayoutManager(getActivity());
	}
	

	@Override
	protected ItemDecoration getItemDecoration() {
		// TODO Auto-generated method stub
		return new RecycleViewItemDecoration(0,30,0,0);
	}
	
	protected Type getType() {
		// TODO Auto-generated method stub
		return new TypeToken<ResultListBean<Video>>() {
		}.getType();
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		mCurrentPage = 1;
		isLoadMoreAction = false;
		mHandler = new TextHttpResponseHandler() {

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString,
					Throwable throwable) {
				// TODO Auto-generated method stub
				if (isLoadMoreAction) {
					loadMoreNodata();
				} else {
					onRequestError(EmptyLayout.NODATA);
				}
				isLoadMoreAction = false;
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				// TODO Auto-generated method stub
				ResultListBean<Video> resultBean = AppContext
						.createGson().fromJson(responseString, getType());
				if (resultBean != null && resultBean.getItems() != null && resultBean.getItems().size() > 0) {
					if (mCurrentPage == 1) {
						showDataUpdate(resultBean.getItems());
						setListData(resultBean, true);
					} else {
						setListData(resultBean, false);
					}
					onRequestSuccess();
					stopLoadMore();
				}else{
					if(isLoadMoreAction){
						loadMoreNodata();
					}else{
					onRequestError(EmptyLayout.NODATA);
					}
				}
				isLoadMoreAction = false;
			}
		};
	}
	
	
	private void showDataUpdate(List<Video> newDatas){
		if(newDatas == null){
			return;
		}
		int updateValue = 0;
		final ResultListBean<Video> bean = (ResultListBean<Video>) CacheManager.readObject(getActivity(), getCacheKey());
		if (bean == null) {
			updateValue = newDatas.size();
		} else {
			List<Video> oldDatas = bean.getItems();
			Video video = oldDatas.get(0);
			for(int i = 0; i < newDatas.size(); i++){
				Video temp = newDatas.get(i);
				if(temp.getId() == video.getId()){
					updateValue = i;
					break;
				}
			}
		}
		String value = getActivity().getResources().getString(R.string.header_hint_refresh_notify);
		String resultData = String.format(value, updateValue);
		showCrouton(resultData,(ViewGroup)mRoot);
	}
	
	protected void setListData(final ResultListBean<Video> pageBean,
			boolean isrefresh) {
		// is refresh
		if (isrefresh) {
			mAdapter.clear();
			mAdapter.addItem(pageBean.getItems());
			AppOperator.runOnThread(new Runnable() {
				@Override
				public void run() {
					CacheManager.saveObject(getActivity(), pageBean,
							getCacheKey());
				}
			});
		} else {
			mAdapter.addItem(pageBean.getItems());
		}
	}
	
	@Override
	protected void onShow() {
		// TODO Auto-generated method stub
		AppOperator.runOnThread(new Runnable() {
			@Override
			public void run() {
				final ResultListBean<Video> bean = (ResultListBean<Video>) CacheManager.readObject(getActivity(), getCacheKey());
				// if is the first loading
				AppOperator.runOnMainThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (bean == null ||bean.getItems() == null || bean.getItems().size() <= 0) {
							onRequestData();
						} else {
							setListData(bean, false);
							onRequestSuccess();
						}
					}
				});
			}
		});
	}
	
	@Override
	protected void onRequestData() {
		// TODO Auto-generated method stub
		mCurrentPage = 1;
		BackChinaApi.getVideoList(currentChannelItem.getUrlapi(),mCurrentPage, mHandler);
	}
	
	@Override
	public void onLoadMoreData() {
		// TODO Auto-generated method stub
		mCurrentPage = mCurrentPage+1;
		isLoadMoreAction = true;
		BackChinaApi.getVideoList(currentChannelItem.getUrlapi(),mCurrentPage, mHandler);
	}
	
    @Override
    public void onTabReselect() {
        // TODO Auto-generated method stub
        
    }

	@Override
	protected void onVideoItemClick(View view, int position) {
		// TODO Auto-generated method stub
		List<Video> datas = mAdapter.getmDatas();
		Video video = datas.get(position);
		//
		if(StringUtils.isEmpty(video.getUrlapi())){
//			UIHelper.showUrlRedirect(getActivity(), video.getUrl());
			UIHelper.enterCommonWebActivity(getActivity(), video.getUrl());
		} else {
			UIHelper.enterVideoPlayerActivity(getActivity(), video);
		}
	}
	
	public void autoRefreshIfNecessary(){
		if (isNeedToAutoRefresh(getCacheKeyPrefix())) {
			autoRefresh();
			saveRefreshTime(getCacheKeyPrefix());
		}
	}
}