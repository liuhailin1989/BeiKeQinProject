package com.android.backchina.ui;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.adapter.CityListAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.bean.City;
import com.android.backchina.bean.ZoneListBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.bean.base.StateBean;
import com.android.backchina.utils.TLog;
import com.android.backchina.widget.LetterListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class CityListActivity extends BaseActivity implements OnItemClickListener{

	
	public final static String BUNDLE_KEY_SELECT_CITY = "BUNDLE_KEY_SELECT_CITY";
	private EditText etCityName;
	
	private ListView mListView;
	
	private ListView mSearchListView;
	
	private TextView mSearchError;
	
	private LetterListView mLetterList;
	
	
	protected TextHttpResponseHandler mHandler;
	
	private List<City> mAllCityListData = new ArrayList<City>();
	
	private List<City> mBigCityListData = new ArrayList<City>();
	
	private CityListAdapter mCityListAdapter;
	
    public static void show(Context context,Fragment fragment) {
        Intent intent = new Intent(context, CityListActivity.class);
        fragment.startActivityForResult(intent, Activity.RESULT_FIRST_USER);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_city_main);
    	setupViews();
    	initData();
    }
    
    private void setupViews(){
    	etCityName = (EditText) findViewById(R.id.et_city_name);
    	mListView = (ListView) findViewById(R.id.list_view);
    	mSearchListView = (ListView) findViewById(R.id.search_result);
    	mSearchError = (TextView) findViewById(R.id.tv_noresult);
    	mLetterList = (LetterListView) findViewById(R.id.letter_listView);
    }
    
	protected Type getType() {
		// TODO Auto-generated method stub
		return new TypeToken<ResultBean<StateBean<ZoneListBean<City>>>>() {
		}.getType();
	}
	
    private void initData(){
    	mCityListAdapter = new CityListAdapter(this);
    	mListView.setAdapter(mCityListAdapter);
    	mListView.setOnItemClickListener(this);
		mHandler = new TextHttpResponseHandler() {
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				TLog.d("called");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String responseString) {
                  if(handleResponse(responseString)){
                	  mCityListAdapter.setData(mAllCityListData);
                	  mCityListAdapter.notifyDataSetChanged();
                  }else{
                	  
                  }
			}
		};
		requestData();
    }
    
    private void requestData(){
    	String url = "http://www.21uscity.com/forum.php?mod=local&action=showmap&appxml=1&json=1";
    	BackChinaApi.getHttp(url,mHandler);
    }
    
	private boolean handleResponse(String response) {
		try {
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(response);
			JsonObject asJsonObject = element.getAsJsonObject();
			JsonObject resultObject = asJsonObject.getAsJsonObject("result");
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
				mAllCityListData.add(city);
				for(City temp : zoneListBean.getCity()){
					temp.setListItemType(CityListAdapter.TYPE_NORMAL);
					mAllCityListData.add(temp);
				}
			}
			//
			JsonArray bigcityArray = resultObject.getAsJsonArray("bigcity");
			Type bigcityType = new TypeToken<List<City>>() {
			}.getType();
			List<City> bigcityListBean = AppContext.createGson().fromJson(
					bigcityArray, bigcityType);
			mBigCityListData.addAll(bigcityListBean);
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
		TLog.d("city name = " +city.getTitle());
		if(city.getListItemType() == CityListAdapter.TYPE_NORMAL){
		    Intent intent=new Intent();
	        Bundle bundle = new Bundle();
	        bundle.putSerializable(BUNDLE_KEY_SELECT_CITY, city);
	        intent.putExtras(bundle);
	        setResult(RESULT_OK, intent);
	        finish();
		}
	}
}
