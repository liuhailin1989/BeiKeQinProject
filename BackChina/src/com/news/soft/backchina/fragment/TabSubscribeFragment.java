package com.news.soft.backchina.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.news.soft.backchina.AppConfig;
import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.R;
import com.news.soft.backchina.adapter.SubscribeAdapter;
import com.news.soft.backchina.api.ApiHttpClient;
import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.base.BaseFragment;
import com.news.soft.backchina.base.adapter.BaseListAdapter.Callback;
import com.news.soft.backchina.bean.StatusBean;
import com.news.soft.backchina.bean.Subscribe;
import com.news.soft.backchina.bean.base.ActivitiesBean;
import com.news.soft.backchina.bean.base.ResultListBean;
import com.news.soft.backchina.interf.ISubscribeListener;
import com.news.soft.backchina.interf.OnTabReselectListener;
import com.news.soft.backchina.interf.SubscribeCallback;
import com.news.soft.backchina.manager.SubscribeManager;
import com.news.soft.backchina.utils.StringUtils;
import com.news.soft.backchina.utils.TLog;
import com.news.soft.backchina.utils.ToastUtils;
import com.news.soft.backchina.utils.UIHelper;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.CookieStore;
import cz.msebera.android.httpclient.client.protocol.ClientContext;
import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.protocol.HttpContext;

public class TabSubscribeFragment extends BaseFragment<Subscribe> implements
		OnTabReselectListener, Callback, OnItemClickListener,
		ISubscribeListener, SubscribeCallback {

	private ListView mListView;

	private View mHeadView;

	private View mFooterView;

	private LinearLayout mHeadViewAddSubscribe;

	private SubscribeAdapter mAdapter;

	private boolean isMySubscribeHandlerNone = false;

	private List<Subscribe> mTotalSubscribeList = new ArrayList<Subscribe>();

	protected TextHttpResponseHandler mMySubscribeHandler = new TextHttpResponseHandler() {

		@Override
		public void onSuccess(int code, Header[] headers, String responseString) {
			// TODO Auto-generated method stub
			if (!handleMySubscribeResponse(headers, responseString)) {
				// requestHotSubscribeData();
				isMySubscribeHandlerNone = true;
			} else {
				Type type = new TypeToken<ResultListBean<Subscribe>>() {
				}.getType();
				ResultListBean<Subscribe> resultListBean = AppContext
						.createGson().fromJson(responseString, type);
				if (resultListBean != null && resultListBean.getItems() != null) {
					for (Subscribe subscribe : resultListBean.getItems()) {
						subscribe.setType(SubscribeAdapter.TYPE_INFOLIST);
						mTotalSubscribeList.add(subscribe);
					}
				}
			}
			BackChinaApi.getMySubscribeBlogList(mMySubscribeBlogHandler);
		}

		@Override
		public void onFailure(int code, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			TLog.d("called");
		}
	};

	protected TextHttpResponseHandler mMySubscribeBlogHandler = new TextHttpResponseHandler() {

		@Override
		public void onSuccess(int code, Header[] headers, String responseString) {
			// TODO Auto-generated method stub
			if (!handleMySubscribeResponse(headers, responseString)) {
				if (isMySubscribeHandlerNone) {
					requestHotSubscribeData();
				} else {
					setDataList(mTotalSubscribeList);
				}
			} else {
				Type type = new TypeToken<ResultListBean<Subscribe>>() {
				}.getType();
				ResultListBean<Subscribe> resultListBean = AppContext
						.createGson().fromJson(responseString, type);
				if (resultListBean != null && resultListBean.getItems() != null) {
					for (Subscribe subscribe : resultListBean.getItems()) {
						subscribe.setType(SubscribeAdapter.TYPE_SPACE);
						subscribe.setFavid("uid_"+subscribe.getId());
						SubscribeManager.getInstance().saveSubscribeToTabOnline(getActivity(), subscribe);
						mTotalSubscribeList.add(subscribe);
					}
					setDataList(mTotalSubscribeList);
				}
			}
		}

		@Override
		public void onFailure(int code, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			TLog.d("called");
		}
	};

	protected TextHttpResponseHandler mHotSubscribeHandler = new TextHttpResponseHandler() {

		@Override
		public void onSuccess(int code, Header[] headers, String responseString) {
			// TODO Auto-generated method stub
			Type type = new TypeToken<ResultListBean<Subscribe>>() {
			}.getType();
			ResultListBean<Subscribe> resultListBean = AppContext.createGson()
					.fromJson(responseString, type);
			if (resultListBean != null && resultListBean.getItems() != null) {
				List<Subscribe> resultData = resultListBean.getItems();
				for(Subscribe subscribe : resultData){
					subscribe.setIdtype(SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID);
				}
				setDataList(resultListBean.getItems());
			}
		}

		@Override
		public void onFailure(int code, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			TLog.d("called");
		}
	};

	private String getCacheKey() {
		return "subscribe_list";
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_subscribe_layout;
	}

	@Override
	protected void initBundle(Bundle bundle) {
		// TODO Auto-generated method stub
		super.initBundle(bundle);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().getBackChinaMode().setSubscribeCallBack(this);
	}

	@Override
	protected void setupViews(View root) {
		// TODO Auto-generated method stub
		super.setupViews(root);

		mListView = (ListView) root.findViewById(R.id.lv_subscribe);

		mHeadView = View.inflate(getActivity(),
				R.layout.layout_subscribe_header, null);
		mFooterView = View.inflate(getActivity(),
				R.layout.layout_subscribe_footer, null);
		mHeadViewAddSubscribe = (LinearLayout) mHeadView
				.findViewById(R.id.ll_subscribe_header_container);
		mHeadViewAddSubscribe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.enterSubscribeActivity(getActivity());
			}
		});
		mListView.setOnItemClickListener(this);
		mListView.addHeaderView(mHeadView);
		mListView.addFooterView(mFooterView);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		isMySubscribeHandlerNone = false;
		super.initData();
		mAdapter = new SubscribeAdapter(this);
		mAdapter.setSubscribeListener(this);
		mListView.setAdapter(mAdapter);
		requestData();
	}

	private void requestData() {
		mTotalSubscribeList.clear();
		isMySubscribeHandlerNone = false;
		if (AppContext.getInstance().isLogin()) {
			BackChinaApi.getMySubscribeList(mMySubscribeHandler);
		}else{
			getLocalSubscribeList();
		}
		// BackChinaApi.getHotSubscribeList(mHandler);
	}

	private void requestHotSubscribeData() {
		mTotalSubscribeList.clear();
		BackChinaApi.getHotSubscribeList(mHotSubscribeHandler);
	}
	
	private void getLocalSubscribeList(){
		List<Subscribe> subscribes = SubscribeManager.getInstance().getSubscribeFromTabLocal(getActivity());
		if(subscribes !=null && subscribes.size() > 0){
			setDataList(subscribes);
		}else{
			requestHotSubscribeData();
		}
	}

	private void setDataList(List<Subscribe> subscribes) {
		mAdapter.clear();
		mAdapter.addItem(subscribes);
	}

	@Override
	public Date getSystemTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onTabReselect() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Subscribe currentSubscribe = (Subscribe) parent.getAdapter().getItem(
				position);
		if (currentSubscribe != null) {
			UIHelper.enterSubscribeDetailActivity(getActivity(),
					currentSubscribe);
		}
	}

	@Override
	public void onSubscribe(View v,final Subscribe subscribe) {
		// TODO Auto-generated method stub
		if (subscribe.getType() == SubscribeAdapter.TYPE_INFOLIST) {
			if (StringUtils.isEmpty(subscribe.getFavid())) {
				if (AppContext.getInstance().isLogin()) {
					BackChinaApi.subscribe(subscribe.getId(),
							new TextHttpResponseHandler() {

								@Override
								public void onSuccess(int code,
										Header[] headers, String responseString) {
									// TODO Auto-generated method stub
									handleSubscribeResponse(headers,
											responseString);
								}

								@Override
								public void onFailure(int code,
										Header[] headers,
										String responseString, Throwable arg3) {
									// TODO Auto-generated method stub
									ToastUtils.show(getContext(), R.string.toast_subscribe_failed);
								}
							});
				} else {
					subscribe.setFavid("local_"+subscribe.getId());
					subscribe.setIdtype(SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID);
					SubscribeManager.getInstance().saveSubscribeToTabLocal(getActivity(), subscribe);
					ToastUtils.show(getActivity(), R.string.toast_subscribe_sucessed);
					UIHelper.notifySubscribeDataChanged(getActivity());
				}
			} else {
				if (AppContext.getInstance().isLogin()) {
					BackChinaApi.cancelSubscribe(subscribe.getFavid(),
							new TextHttpResponseHandler() {

								@Override
								public void onSuccess(int code,
										Header[] headers, String responseString) {
									// TODO Auto-generated method stub
									handleCancelSubscribeResponse(subscribe,headers,
											responseString);
								}

								@Override
								public void onFailure(int code,
										Header[] headers,
										String responseString, Throwable arg3) {
									// TODO Auto-generated method stub
									ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_failed);
								}
							});
				} else {
					SubscribeManager.getInstance().deleteSubscribeToTabLocal(getActivity(), subscribe);
					ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_sucessed);
					UIHelper.notifySubscribeDataChanged(getActivity());
				}
			}
		} else if (subscribe.getType() == SubscribeAdapter.TYPE_SPACE) {
			BackChinaApi.cancleSubscribeBlog(subscribe.getId(),
					new TextHttpResponseHandler() {

						@Override
						public void onSuccess(int code, Header[] headers,
								String responseString) {
							// TODO Auto-generated method stub
							handleCancelSubscribeResponse(subscribe,headers,
									responseString);
						}

						@Override
						public void onFailure(int code, Header[] headers,
								String responseString, Throwable arg3) {
							// TODO Auto-generated method stub
							ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_sucessed);
						}
					});
		}
	}

	private void handleSubscribeResponse(Header[] headers, String response) {
    	Type type = new TypeToken<ActivitiesBean<Subscribe>>() {
        }.getType();
        ActivitiesBean<Subscribe> activitiesBean = AppContext.createGson().fromJson(response, type);
        Subscribe subscribe = activitiesBean.getActivities();
		if (subscribe.getStatus() == null) {
			if (subscribe.getFavid() != null) {
				ToastUtils.show(getActivity(), R.string.toast_subscribe_sucessed);
				SubscribeManager.getInstance().saveSubscribeToTabOnline(getActivity(), subscribe);
				UIHelper.notifySubscribeDataChanged(getActivity());
			}else{
				ToastUtils.show(getActivity(), R.string.toast_subscribe_failed);
			}
		} else {
			if (subscribe.getStatus().contains("repeat")) {
				ToastUtils.show(getActivity(), R.string.toast_subscribed);
			} else if (subscribe.getStatus().equals("-1")) {
				ToastUtils.show(getActivity(), R.string.toast_subscribe_failed);
			} else if (subscribe.getStatus().equals("-2")) {
				ToastUtils.show(getActivity(), R.string.toast_need_login);
			} else {
				ToastUtils.show(getActivity(), R.string.toast_subscribe_failed);
			}
		}
	}

	private void handleCancelSubscribeResponse(Subscribe subscribe,Header[] headers, String response) {
		Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
		}.getType();
		ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson()
				.fromJson(response, type);
		StatusBean statusBean = activitiesBean.getActivities();
		if (statusBean.getStatus().equals("1")) {
			ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_sucessed);
			SubscribeManager.getInstance().deleteSubscribeTabOnline(getActivity(), ""+subscribe.getId(),SubscribeManager.SUBSCRIBE_ID_TYPE_SEARCHID);
			UIHelper.notifySubscribeDataChanged(getActivity());
		} else if (statusBean.getStatus().equals("-1")) {
			ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_failed);
		} else if (statusBean.getStatus().equals("-2")) {
			ToastUtils.show(getContext(), R.string.toast_need_login);
		} else {
			ToastUtils.show(getContext(), R.string.toast_cancle_subscribe_failed);
		}
	}

	private boolean handleMySubscribeResponse(Header[] headers, String response) {
		Type type = new TypeToken<ActivitiesBean<StatusBean>>() {
		}.getType();
		ActivitiesBean<StatusBean> activitiesBean = AppContext.createGson()
				.fromJson(response, type);
		StatusBean statusBean = activitiesBean.getActivities();
		if (statusBean != null) {// && statusBean.getStatus().equals("0")
			return false;
		} else {
			return true;
		}
	}

	private void handleCookie(Header[] headers) {
		AsyncHttpClient client = ApiHttpClient.getHttpClient();
		HttpContext httpContext = client.getHttpContext();
		CookieStore cookies = (CookieStore) httpContext
				.getAttribute(ClientContext.COOKIE_STORE);
		if (cookies != null) {
			String tmpcookies = "";
			for (Cookie c : cookies.getCookies()) {
				TLog.d("cookie:" + c.getName() + " " + c.getValue());
				tmpcookies += (c.getName() + "=" + c.getValue()) + ";";
			}
			if (StringUtils.isEmpty(tmpcookies)) {

				if (headers != null) {
					for (Header header : headers) {
						String key = header.getName();
						String value = header.getValue();
						if (key.contains("Set-Cookie"))
							tmpcookies += value + ";";
					}
					if (tmpcookies.length() > 0) {
						tmpcookies = tmpcookies.substring(0,
								tmpcookies.length() - 1);
					}
				}
			}
			AppContext.getInstance().setProperty(AppConfig.CONF_COOKIE,
					tmpcookies);
			ApiHttpClient.setCookie(ApiHttpClient.getCookie(AppContext
					.getInstance()));
		}
	}

	@Override
	public void OnSubscribeDataChanged() {
		// TODO Auto-generated method stub
		requestData();
	}

}
