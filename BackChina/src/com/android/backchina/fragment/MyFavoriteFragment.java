package com.android.backchina.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.backchina.AppContext;
import com.android.backchina.AppOperator;
import com.android.backchina.BackChinaMode;
import com.android.backchina.R;
import com.android.backchina.adapter.MyFavoriteAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseListFragment;
import com.android.backchina.base.adapter.BaseListAdapter;
import com.android.backchina.bean.Blog;
import com.android.backchina.bean.FavoriteBean;
import com.android.backchina.bean.News;
import com.android.backchina.bean.base.ResultListBean;
import com.android.backchina.interf.FavoriteCallback;
import com.android.backchina.manager.FavoriteManager;
import com.android.backchina.ui.empty.EmptyLayout;
import com.android.backchina.utils.StringUtils;
import com.android.backchina.utils.TLog;
import com.android.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class MyFavoriteFragment extends BaseListFragment<FavoriteBean> implements FavoriteCallback{

	public final static String ID_TYPE_AID = "aid";
	public final static String ID_TYPE_BLOGID = "blogid";
	public final static String ID_TYPE_TID = "tid";
	
	private int mCurrentPage = 1;
	
	private boolean isLoadMoreAction = false;
	
	private Handler mHandler = new Handler();
	
	protected TextHttpResponseHandler mFavoriteNewsHandler;
	
	protected TextHttpResponseHandler mFavoriteBlogHandler;
	
	protected TextHttpResponseHandler mFavoriteSpecialNewsHandler;
	
	private  List<FavoriteBean> mFavoriteBeanList = new ArrayList<FavoriteBean>();
	private  boolean isFavoriteNewsRequestFinish = false;
	private  boolean isFavoriteBlogRequestFinish = false;
	private  boolean isFavoriteSpecialNewsRequestFinish = false;
	
	private Object mLock = new Object();
	
	public static MyFavoriteFragment newInstance() {
		MyFavoriteFragment fragment = new MyFavoriteFragment();
		return fragment;
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
	}
	
	
	private void resetFlag(){
		mFavoriteBeanList.clear();
		isFavoriteNewsRequestFinish = false;
		isFavoriteBlogRequestFinish = false;
		isFavoriteSpecialNewsRequestFinish = false;
	}
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		AppContext.getInstance().getBackChinaMode().setFavoriteCallback(this);
		mCurrentPage = 1;
		isLoadMoreAction = false;
		resetFlag();
		mFavoriteNewsHandler = new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				synchronized (mLock) {
					TLog.d("mFavoriteNewsHandler");
					isFavoriteNewsRequestFinish = true;
					mLock.notifyAll();
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				synchronized (mLock) {
					try {
						ResultListBean<FavoriteBean> resultBean = AppContext
								.createGson().fromJson(responseString,
										getType());
						if (resultBean != null && resultBean.getItems() != null) {
							//
							mFavoriteBeanList.addAll(resultBean.getItems());
						} else {

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					TLog.d("mFavoriteNewsHandler");
					isFavoriteNewsRequestFinish = true;
					mLock.notifyAll();
				}
			}
		};
		//
		mFavoriteBlogHandler = new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				synchronized (mLock) {
					TLog.d("mFavoriteBlogHandler");
					isFavoriteBlogRequestFinish = true;
					mLock.notifyAll();
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				synchronized (mLock) {
					try {
						ResultListBean<FavoriteBean> resultBean = AppContext
								.createGson().fromJson(responseString,
										getType());
						if (resultBean != null && resultBean.getItems() != null) {
							//
							mFavoriteBeanList.addAll(resultBean.getItems());
						} else {

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					TLog.d("mFavoriteBlogHandler");
					isFavoriteBlogRequestFinish = true;
					mLock.notifyAll();
				}
			}
		};
		//
		mFavoriteSpecialNewsHandler = new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				synchronized (mLock) {
					TLog.d("mFavoriteSpecialNewsHandler");
					isFavoriteSpecialNewsRequestFinish = true;
					mLock.notifyAll();
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				synchronized (mLock) {
					try {
						ResultListBean<FavoriteBean> resultBean = AppContext
								.createGson().fromJson(responseString,
										getType());
						if (resultBean != null && resultBean.getItems() != null) {
							//
							mFavoriteBeanList.addAll(resultBean.getItems());
						} else {

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					TLog.d("mFavoriteSpecialNewsHandler");
					isFavoriteSpecialNewsRequestFinish = true;
					mLock.notifyAll();
				}
			}
		};
	}
	
	private void showDataUpdate(List<FavoriteBean> newDatas){
//		String value = getActivity().getResources().getString(R.string.header_hint_refresh_notify);
//		String resultData = String.format(value, 0);
		String resultData = "已更新至最新";
		showCrouton(resultData,(ViewGroup)mRoot);
	}
	
	protected void setListData(final List<FavoriteBean> favoriteBeans,
			boolean isrefresh) {
		if(favoriteBeans == null){
			mAdapter.clear();
			return;
		}
		// is refresh
		Collections.sort(favoriteBeans, new FavoriteBeanComparator());
		if (isrefresh) {
			mAdapter.clear();
			mAdapter.addItem(favoriteBeans);
		} else {
			mAdapter.addItem(favoriteBeans);
		}
		AppOperator.runOnThread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				FavoriteManager.getInstance().saveFavoriteBeanToTabOnline(getActivity(), favoriteBeans);
			}
		});
	}
	
	@Override
	protected void onShow() {
		// TODO Auto-generated method stub
		TLog.d("called");
		onRefresh();
	}
	
	private void checkResult(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				synchronized (mLock) {
					while(!isFavoriteNewsRequestFinish || !isFavoriteBlogRequestFinish || !isFavoriteSpecialNewsRequestFinish){
						try {
							mLock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(mFavoriteBeanList != null && mFavoriteBeanList.size() > 0){
								if (mCurrentPage == 1) {
									showDataUpdate(mFavoriteBeanList);
									setListData(mFavoriteBeanList, true);
								}else{
									setListData(mFavoriteBeanList, false);
								}
								onRequestSuccess();
								stopLoadMore();
							}else{
								if(isLoadMoreAction){
									loadMoreNodata();
								}else{
								setListData(null,true);
								onRequestError(EmptyLayout.NODATA);
								}
							}
							isLoadMoreAction = false;
						}
					});
				}
			}
		}).start();
	}
	
	@Override
	protected void onRequestData() {
		// TODO Auto-generated method stub
		mCurrentPage = 1;
		resetFlag();
		if (AppContext.getInstance().isLogin()) {
			BackChinaApi.getFavoriteNews(mCurrentPage, mFavoriteNewsHandler);
			BackChinaApi.getFavoriteBlog(mCurrentPage, mFavoriteBlogHandler);
			BackChinaApi.getFavoriteSpecialNews(mCurrentPage,mFavoriteSpecialNewsHandler);
			checkResult();
		}else{
			List<FavoriteBean> localFavoriteBeans = FavoriteManager.getInstance().getFavoriteBeanFromTabLocal(getActivity());
			if (localFavoriteBeans != null) {
				mFavoriteBeanList.addAll(localFavoriteBeans);
				setListData(mFavoriteBeanList, true);
				//
				onRequestSuccess();
				stopLoadMore();
			}else{
				if(isLoadMoreAction){
					loadMoreNodata();
				}else{
				setListData(null,true);
				onRequestError(EmptyLayout.NODATA);
				}
			}
			isLoadMoreAction = false;
		}
	}
	
	
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
//		super.onLoadMore();
		mCurrentPage = mCurrentPage+1;
		isLoadMoreAction = true;
		resetFlag();
		if (AppContext.getInstance().isLogin()) {
			BackChinaApi.getFavoriteNews(mCurrentPage, mFavoriteNewsHandler);
			BackChinaApi.getFavoriteBlog(mCurrentPage, mFavoriteBlogHandler);
			BackChinaApi.getFavoriteSpecialNews(mCurrentPage,mFavoriteSpecialNewsHandler);
			checkResult();
		}else{
			if(isLoadMoreAction){
				loadMoreNodata();
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		FavoriteBean item = (FavoriteBean) parent.getAdapter().getItem(position);
		if(item.getIdtype().equals(ID_TYPE_AID)){
			News news = new News();
			news.setId(item.getId());
			news.setFavid(item.getFavid());
			news.setTitle(item.getTitle());
			news.setUrl(item.getUrl());
			news.setUrlapi(item.getUrlapi());
			UIHelper.enterNewsDetail(getActivity(), news,true);
		}else if(item.getIdtype().equals(ID_TYPE_BLOGID)){
			Blog blog = new Blog();
			blog.setId(item.getId());
			blog.setTitle(item.getTitle());
			blog.setUrl(item.getUrl());
			blog.setUrlapi(item.getUrlapi());
			UIHelper.enterBlogDetail(getActivity(), blog,true);
		}else if(item.getIdtype().equals(ID_TYPE_TID)){
			//TODO
		}
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// TODO Auto-generated method stub
		FavoriteBean item = (FavoriteBean) parent.getAdapter().getItem(position);
		BackChinaApi.cancleFavoriteNews(item.getFavid(), new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				UIHelper.notifyFavoriteDataChanged(getActivity());
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		return true;
	}
	
	@Override
	public void onTabReselect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected BaseListAdapter<FavoriteBean> getListAdapter() {
		// TODO Auto-generated method stub
		return new MyFavoriteAdapter(this);
	}

	@Override
	protected Type getType() {
		// TODO Auto-generated method stub
		return new TypeToken<ResultListBean<FavoriteBean>>() {
		}.getType();
	}

	@Override
	public void OnFavoriteDataChanged() {
		// TODO Auto-generated method stub
		if (getActivity() != null) {
			onRefresh();
		}
	}
	
	public class FavoriteBeanComparator implements Comparator<FavoriteBean>{

		@Override
		public int compare(FavoriteBean lhs, FavoriteBean rhs) {
			// TODO Auto-generated method stub
			try {
				String lhs_dateline = lhs.getDateline();
				String rhs_dateline = rhs.getDateline();
				long lhs_time = 0;
				long rhs_time = 0;
				//
				if(lhs_dateline.contains("-")){
					Date date = StringUtils.toDate2(lhs_dateline);
					lhs_time = date.getTime()/1000;
				}else{
					lhs_time = Long.valueOf(lhs_dateline);
				}
				//
				if(rhs_dateline.contains("-")){
					Date date = StringUtils.toDate2(rhs_dateline);
					rhs_time = date.getTime()/1000;
				}else{
					rhs_time = Long.valueOf(rhs_dateline);
				}
				//
				if (lhs_time > rhs_time) {
					return -1;
				} else if (lhs_time < rhs_time) {
					return 1;
				} else {
					return 0;
				}
			}catch(Exception ex){
				return -1;
			}
		}
		
	}
	
}