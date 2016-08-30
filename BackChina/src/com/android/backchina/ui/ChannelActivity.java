package com.android.backchina.ui;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.backchina.AppContext;
import com.android.backchina.R;
import com.android.backchina.adapter.DragAdapter;
import com.android.backchina.adapter.UnsignedAdapter;
import com.android.backchina.api.remote.BackChinaApi;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.bean.News;
import com.android.backchina.bean.base.ChannelBean;
import com.android.backchina.bean.base.PageBean;
import com.android.backchina.bean.base.ResultBean;
import com.android.backchina.interf.DragItemTouchHelperCallBack;
import com.android.backchina.listener.OnItemClickListener;
import com.android.backchina.listener.OnItemLongPressListener;
import com.android.backchina.listener.OnItemRemovedListener;
import com.android.backchina.manager.DragGridLayoutManager;
import com.android.backchina.utils.TLog;
import com.android.backchina.widget.DragRecyclerView;
import com.android.backchina.widget.UnsignedRecyclerView;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.auth.ChallengeState;

/**
 * 频道管理
 */
public class ChannelActivity extends BaseActivity {
	public static String TAG = "ChannelActivity";

	private UnsignedRecyclerView mUnsignedView;
	private DragRecyclerView mDragView;
	
	private DragAdapter mDragAdapter;
	private UnsignedAdapter mUnsignedAdapter;
	private ItemTouchHelper mItemTouchHelper;
	
	private DragGridLayoutManager mDragGridManager;
	
	private DragGridLayoutManager mUnsignedGridManager;
	
    private int mKeepItemCount = 1;
    
    private Context mContext;
    
    private Button mBtnDeleteChannel;
    
    private static boolean sIsDeleteFinish = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_manager);

		mContext = this;
		sIsDeleteFinish = true;
		setupViews();
		init();
		requeseData();
	}

	private void setupViews() {
		mDragView = (DragRecyclerView) findViewById(R.id.myChanel);
		mUnsignedView = (UnsignedRecyclerView) findViewById(R.id.moreChanel);
		mBtnDeleteChannel = (Button) findViewById(R.id.btn_delete_channel);
		mBtnDeleteChannel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(sIsDeleteFinish){
					sIsDeleteFinish = false;
					mBtnDeleteChannel.setText("完成");
					mDragAdapter.requestLongPressMode();
				}else{
					sIsDeleteFinish = true;
					mBtnDeleteChannel.setText("排序删除");
					mDragAdapter.quitLongPressMode();
				}
			}
		});
	}

	private void init() {
		mDragGridManager = new DragGridLayoutManager(mContext, 4);
		mUnsignedGridManager = new DragGridLayoutManager(mContext, 4);
		mDragAdapter = new DragAdapter();
		mDragAdapter.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(ViewHolder holder, int position) {
				// TODO Auto-generated method stub
				
			}
		});
		mDragAdapter.setOnLongPressListener(new OnItemLongPressListener() {
			
			@Override
			public void onLongPress() {
				// TODO Auto-generated method stub
				
			}
		});
		mDragAdapter.setOnItemRemovedListener(new OnItemRemovedListener<ChannelItem>() {
			
			@Override
			public void onItemRemoved(int position, ChannelItem removedItem) {
				// TODO Auto-generated method stub
				mUnsignedAdapter.onItemInsert(position, removedItem);
			}
		});
		mDragView.setAdapter(mDragAdapter);
		mDragView.setLayoutManager(mDragGridManager);
		DragItemTouchHelperCallBack callBack = new DragItemTouchHelperCallBack(mDragAdapter, mKeepItemCount);
	    mItemTouchHelper = new ItemTouchHelper(callBack);
	    mItemTouchHelper.attachToRecyclerView(mDragView);
	    mDragAdapter.setItemTouchHelper(mItemTouchHelper);
		
		mUnsignedAdapter = new UnsignedAdapter();
		mUnsignedAdapter.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(ViewHolder holder, int position) {
				// TODO Auto-generated method stub
				mUnsignedAdapter.onItemRemoved(position);
			}
		});
		
		mUnsignedAdapter.setOnItemRemovedListener(new OnItemRemovedListener<ChannelItem>() {
			
			@Override
			public void onItemRemoved(int position, ChannelItem removedItem) {
				// TODO Auto-generated method stub
				mDragAdapter.onItemInsert(position, removedItem);
			}
		});
		
		mUnsignedView.setAdapter(mUnsignedAdapter);
		mUnsignedView.setLayoutManager(mUnsignedGridManager);
	}
	
	protected Type getType() {
		// TODO Auto-generated method stub
		 return new TypeToken<ChannelBean<ChannelItem>>() {
	        }.getType();
	}
	
	private void requeseData(){
		
		BackChinaApi.getChannelList( new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] headers, String responseString) {
				// TODO Auto-generated method stub
				TLog.d("called");
				ChannelBean<ChannelItem> channelBean = AppContext.createGson().fromJson(responseString, getType());
				if(channelBean != null && channelBean.getItems() != null){
					setListData(channelBean.getItems());
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] headers, String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				TLog.d("called");
			}
		});
	}
	
	private void setListData(List<ChannelItem> dataList){
		TLog.d("called dataList size =" +dataList.size());
		if(mUnsignedAdapter != null){
			mUnsignedAdapter.setDatas(dataList);
		}
	}

}
