package com.news.soft.backchina.ui;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.news.soft.backchina.AppContext;
import com.news.soft.backchina.AppOperator;
import com.news.soft.backchina.R;
import com.news.soft.backchina.adapter.CityListAdapter;
import com.news.soft.backchina.api.remote.BackChinaApi;
import com.news.soft.backchina.base.BaseActivity;
import com.news.soft.backchina.bean.City;
import com.news.soft.backchina.bean.ZoneListBean;
import com.news.soft.backchina.bean.base.ResultBean;
import com.news.soft.backchina.bean.base.ResultListBean;
import com.news.soft.backchina.bean.base.StateBean;
import com.news.soft.backchina.cache.CacheManager;
import com.news.soft.backchina.utils.PingYinUtil;
import com.news.soft.backchina.utils.TLog;
import com.news.soft.backchina.widget.LetterListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class CityListActivity extends BaseActivity implements
		OnItemClickListener {

	public final static String BUNDLE_KEY_SELECT_CITY = "BUNDLE_KEY_SELECT_CITY";
	private EditText etCityName;
	
	private Button mBtnCalcle;

	private ListView mListView;

	private ListView mSearchListView;

	private TextView mSearchError;

	private LetterListView mLetterList;

	protected TextHttpResponseHandler mHandler;

	private ResultListBean<City> mAllCityData;
	
	private ResultListBean<City> mHistoryCityData;
	
	private List<City> mSearchCityData = new ArrayList<City>();

	private CityListAdapter mCityListAdapter;
	
	private CityListAdapter mSearchCityListAdapter;

	private Context mContext;
	
	private City noHistroyData = null;

	public static void show(Context context, Fragment fragment) {
		Intent intent = new Intent(context, CityListActivity.class);
		fragment.startActivityForResult(intent, Activity.RESULT_FIRST_USER);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_main);
		mContext = this;
		setupViews();
		initData();
	}

	private void setupViews() {
		mBtnCalcle = (Button) findViewById(R.id.btn_cancle);
		mBtnCalcle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		etCityName = (EditText) findViewById(R.id.et_city_name);
		mListView = (ListView) findViewById(R.id.list_view);
		mSearchListView = (ListView) findViewById(R.id.search_result);
		mSearchError = (TextView) findViewById(R.id.tv_noresult);
		mLetterList = (LetterListView) findViewById(R.id.letter_listView);
		
		//
		etCityName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (s.toString() == null || "".equals(s.toString())) {
					mLetterList.setVisibility(View.VISIBLE);
					mListView.setVisibility(View.VISIBLE);
					mSearchListView.setVisibility(View.GONE);
					mSearchError.setVisibility(View.GONE);
				}else{
					mSearchCityData.clear();
					mLetterList.setVisibility(View.GONE);
					mListView.setVisibility(View.GONE);
					getSearchCityList(s.toString());
					if (mSearchCityData.size() <= 0) {
						mSearchError.setVisibility(View.VISIBLE);
						mSearchListView.setVisibility(View.GONE);
					}else{
						mSearchError.setVisibility(View.GONE);
						mSearchListView.setVisibility(View.VISIBLE);
						if(mSearchCityListAdapter != null){
							mSearchCityListAdapter.notifyDataSetChanged();
						}
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void getSearchCityList(String keyword){
		List<City> allList = mAllCityData.getItems();
		for(City city : allList){
			if(city.getListItemType() == CityListAdapter.TYPE_NORMAL){
//				String pingyinFirst = PingYinUtil.converterToFirstSpell(city.getTitle());
//				String pingyinAll = PingYinUtil.converterToFirstSpell(city.getTitle());
//				TLog.d(city.getTitle()+":"+"pingyinFirst =" +pingyinFirst +"//"+"pingyinAll" + pingyinAll);
				if(city.getTitle().toLowerCase().contains(keyword.toLowerCase())){
					mSearchCityData.add(city);
					TLog.d(city.getTitle() + "add into search = " +keyword);
				}
			}
		}
		if (mSearchCityData != null && mSearchCityData.size() > 0) {
			mSearchCityListAdapter.setData(null, mSearchCityData);
		}
	}

	private String getCacheKey() {
		return "city_list_all";
	}
	
	private String getHistoryCacheKey() {
		return "city_history_list";
	}

	private void initData() {
		mAllCityData = new ResultListBean<City>();
		//
		noHistroyData = new City();
		noHistroyData.setTitle("暂无数据");
		noHistroyData.setListItemType(CityListAdapter.TYPE_NORMAL);
		//
		mHistoryCityData = new ResultListBean<City>();
		City historyCityGroup = new City();
		historyCityGroup.setTitle("最近访问城市");
		historyCityGroup.setListItemType(CityListAdapter.TYPE_CITY_GROUP);
		mHistoryCityData.getItems().add(0,historyCityGroup);
		//
		mCityListAdapter = new CityListAdapter(this);
		mListView.setAdapter(mCityListAdapter);
		mListView.setOnItemClickListener(this);
		//
		mSearchCityListAdapter = new CityListAdapter(this);
		mSearchListView.setAdapter(mSearchCityListAdapter);
		mSearchListView.setOnItemClickListener(this);
		//
		mHandler = new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				TLog.d("called");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
				if (handleResponse(responseString)) {
					// mCityListAdapter.setData(mAllCityListData);
					if(mHistoryCityData.getItems().size() == 1){
						mHistoryCityData.getItems().add(noHistroyData);
					}else{
						if (noHistroyData != null && isInHistory(noHistroyData)) {
							mHistoryCityData.getItems().remove(noHistroyData);
						}
					}
					mCityListAdapter.setData(mHistoryCityData.getItems(),mAllCityData.getItems());
					mCityListAdapter.notifyDataSetChanged();
					saveCache(mAllCityData,getCacheKey());
				} else {

				}
			}
		};
		requestData();
	}

	private void saveCache(final Serializable ser, final String cachekey){
		AppOperator.runOnThread(new Runnable() {
			@Override
			public void run() {
				CacheManager.saveObject(mContext, ser,cachekey);
			}
		});
	}
	
	private Serializable getCache(final String cachekey){
		return CacheManager.readObject(mContext,cachekey);
	}
	
	private void requestData() {
		ResultListBean<City> allCityBean = (ResultListBean<City>)getCache(getCacheKey());
		ResultListBean<City> historyCityBean = (ResultListBean<City>) getCache(getHistoryCacheKey());
		if (allCityBean != null) {
			mAllCityData = allCityBean;
			if (historyCityBean != null) {
				mHistoryCityData = historyCityBean;
			}
			if(mHistoryCityData.getItems().size() == 1){
				mHistoryCityData.getItems().add(noHistroyData);
			}else{
				if (noHistroyData != null && isInHistory(noHistroyData)) {
					mHistoryCityData.getItems().remove(noHistroyData);
				}
			}
			mCityListAdapter.setData(mHistoryCityData.getItems(),mAllCityData.getItems());
			mCityListAdapter.notifyDataSetChanged();
		} else {
			String url = "http://www.21uscity.com/forum.php?mod=local&action=showmap&appxml=1&json=1";
			BackChinaApi.getHttp(url, mHandler);
		}
	}

	private boolean handleResponse(String response) {
		try {
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(response);
			JsonObject asJsonObject = element.getAsJsonObject();
			JsonObject resultObject = asJsonObject.getAsJsonObject("result");
			//
			JsonArray bigcityArray = resultObject.getAsJsonArray("bigcity");
			Type bigcityType = new TypeToken<List<City>>() {
			}.getType();
			List<City> bigcityListBean = AppContext.createGson().fromJson(
					bigcityArray, bigcityType);
			City hotCity = new City();
			hotCity.setTitle("热门城市");
			hotCity.setListItemType(CityListAdapter.TYPE_CITY_CAT);
			mAllCityData.getItems().add(hotCity);
			for (City temp : bigcityListBean) {
				temp.setListItemType(CityListAdapter.TYPE_HOT);
				mAllCityData.getItems().add(temp);
			}
			//
			JsonObject stateObject = resultObject.getAsJsonObject("state");
			Set<Entry<String, JsonElement>> entrySet = stateObject.entrySet();
			Type stateType = new TypeToken<ZoneListBean<City>>() {
			}.getType();
			for (Entry<String, JsonElement> entry : entrySet) {
				JsonObject result = entry.getValue().getAsJsonObject();
				ZoneListBean<City> zoneListBean = AppContext.createGson()
						.fromJson(result, stateType);
				City city = new City();
				city.setId(zoneListBean.getId());
				city.setCitle(zoneListBean.getCitle());
				city.setFtitle(zoneListBean.getFtitle());
				city.setListItemType(CityListAdapter.TYPE_CITY_GROUP);
				city.setTitle(zoneListBean.getTitle());
				city.setUrl(zoneListBean.getUrl());
				city.setUrlapi(zoneListBean.getUrlapi());
				mAllCityData.getItems().add(city);
				for (City temp : zoneListBean.getCity()) {
					temp.setListItemType(CityListAdapter.TYPE_NORMAL);
					mAllCityData.getItems().add(temp);
				}
			}
			TLog.d("called");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		City city = (City) parent.getAdapter().getItem(position);
		TLog.d("city name = " + city.getTitle());
		if (city.getListItemType() == CityListAdapter.TYPE_NORMAL ||city.getListItemType() == CityListAdapter.TYPE_HOT) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable(BUNDLE_KEY_SELECT_CITY, city);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			//
			if (noHistroyData != null && isInHistory(noHistroyData)) {
				mHistoryCityData.getItems().remove(noHistroyData);
			}
			
			if (!isInHistory(city)) {
				mHistoryCityData.getItems().add(city);
				saveCache(mHistoryCityData, getHistoryCacheKey());
			}
			//
			finish();
		}
	}
	
	private boolean isInHistory(City city){
		if (mHistoryCityData != null) {
			for (City temp : mHistoryCityData.getItems()) {
              if(temp.getTitle().equals(city.getTitle())){
            	  return true;
              }
			}
		}
		return false;
	}
}
