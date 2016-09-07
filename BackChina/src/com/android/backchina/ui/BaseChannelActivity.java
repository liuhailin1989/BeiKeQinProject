package com.android.backchina.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.android.backchina.R;
import com.android.backchina.adapter.DragAdapter;
import com.android.backchina.adapter.UnsignedAdapter;
import com.android.backchina.base.BaseActivity;
import com.android.backchina.bean.ChannelItem;
import com.android.backchina.interf.DragItemTouchHelperCallBack;
import com.android.backchina.listener.OnItemClickListener;
import com.android.backchina.listener.OnItemLongPressListener;
import com.android.backchina.listener.OnItemRemovedListener;
import com.android.backchina.manager.FullyGridLayoutManager;
import com.android.backchina.utils.TLog;
import com.android.backchina.widget.DragRecyclerView;
import com.android.backchina.widget.UnsignedRecyclerView;

public abstract class BaseChannelActivity extends BaseActivity {
	
	public final static String BUNDLE_KEY_DATA_CHANGED = "BUNDLE_KEY_DATA_CHANGED";
	
	public final static int RESULT_CODE_OK = 1000;
	
    public static String TAG = "ChannelActivity";
    
    private ImageView mBtnClose;
    
    private UnsignedRecyclerView mUnsignedView;

    private DragRecyclerView mDragView;

    private DragAdapter mDragAdapter;

    private UnsignedAdapter mUnsignedAdapter;

    private ItemTouchHelper mItemTouchHelper;

    private FullyGridLayoutManager mDragGridManager;

    private FullyGridLayoutManager mUnsignedGridManager;

    private int mKeepItemCount = 1;

    private Context mContext;

    private Button mBtnDeleteChannel;

    private static boolean sIsDeleteFinish = true;
    
    private List<ChannelItem> myChannelData = new ArrayList<ChannelItem>();
    
    private List<ChannelItem> moreChannelData = new ArrayList<ChannelItem>();
    
    private static boolean isDataChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_manager);
        mContext = this;
        sIsDeleteFinish = true;
        isDataChanged = false;
        setupViews();
        init();
        requeseData();
    }

    private void setupViews() {
        mBtnClose = (ImageView) findViewById(R.id.btn_close);
        mBtnClose.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CloseChannelActivity();
            }
        });
        mDragView = (DragRecyclerView) findViewById(R.id.myChanel);
        mUnsignedView = (UnsignedRecyclerView) findViewById(R.id.moreChanel);
        mBtnDeleteChannel = (Button) findViewById(R.id.btn_delete_channel);
        mBtnDeleteChannel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (sIsDeleteFinish) {
                    sIsDeleteFinish = false;
                    mBtnDeleteChannel.setText("完成");
                    mDragAdapter.requestLongPressMode();
                } else {
                    sIsDeleteFinish = true;
                    mBtnDeleteChannel.setText("排序删除");
                    mDragAdapter.quitLongPressMode();
                }
            }
        });
    }

    private void init() {
//        mDragGridManager = new DragGridLayoutManager(mContext, 4);
//        mUnsignedGridManager = new DragGridLayoutManager(mContext, 4);
        //WrapHeightGridLayoutManager
        mDragGridManager = new FullyGridLayoutManager(mContext, 4);
        
        mUnsignedGridManager = new FullyGridLayoutManager(mContext, 4);
        
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
            	 sIsDeleteFinish = false;
                 mBtnDeleteChannel.setText("完成");
                 mDragAdapter.requestLongPressMode();
            }
        });
        mDragAdapter.setOnItemRemovedListener(new OnItemRemovedListener<ChannelItem>() {

            @Override
            public void onItemRemoved(int position, ChannelItem removedItem) {
                // TODO Auto-generated method stub
                mUnsignedAdapter.onItemInsert(position, removedItem);
                moreChannelData.add(removedItem);
                myChannelData.remove(removedItem);
                isDataChanged = true;
            }
        });
        mDragView.setLayoutManager(mDragGridManager);
        mDragView.setAdapter(mDragAdapter);
        DragItemTouchHelperCallBack callBack = new DragItemTouchHelperCallBack(mDragAdapter,
                mKeepItemCount);
        mItemTouchHelper = new ItemTouchHelper(callBack);
        mItemTouchHelper.attachToRecyclerView(mDragView);
        mDragAdapter.setItemTouchHelper(mItemTouchHelper);

        mUnsignedAdapter = new UnsignedAdapter();
        mUnsignedAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(ViewHolder holder, int position) {
                // TODO Auto-generated method stub
                mUnsignedAdapter.onItemRemoved(position);
                isDataChanged = true;
            }
        });

        mUnsignedAdapter.setOnItemRemovedListener(new OnItemRemovedListener<ChannelItem>() {

            @Override
            public void onItemRemoved(int position, ChannelItem removedItem) {
                // TODO Auto-generated method stub
                mDragAdapter.onItemInsert(position, removedItem);
                myChannelData.add(removedItem);
                moreChannelData.remove(removedItem);
                isDataChanged = true;
            }
        });

        mUnsignedView.setLayoutManager(mUnsignedGridManager);
        mUnsignedView.setAdapter(mUnsignedAdapter);
       
    }

    private void requeseData() {
        myChannelData.clear();
        moreChannelData.clear();
        myChannelData = getLocalChannelList();
        List<ChannelItem> data = getAllChannelList();
        for(ChannelItem item : data){
            if(isInMyChannelData(item) ){
               continue;
            }else{
                moreChannelData.add(item); 
            }
        }
        setDragListData(myChannelData);
        setUnsignedListData(moreChannelData);
    }
    
    protected List<ChannelItem> getMyChannelData(){
    	return myChannelData;
    }
    
    private boolean isInMyChannelData(ChannelItem item){
        if(myChannelData == null || myChannelData.size() <= 0){
            return false;
        }
        for(ChannelItem data : myChannelData){
            if(data.getName().equals(item.getName())){
                return true;
            }
        }
        return false;
    }
    
    private void setUnsignedListData(List<ChannelItem> dataList) {
        TLog.d("called dataList size =" + dataList.size());
        if (mUnsignedAdapter != null && dataList != null) {
            mUnsignedAdapter.setDatas(dataList);
        }
    }

    private void setDragListData(List<ChannelItem> dataList) {
        TLog.d("called dataList size =" + dataList.size());
        if (mDragAdapter != null && dataList != null) {
            mDragAdapter.setDatas(dataList);
        }
    }

    
    private void CloseChannelActivity(){
    	saveSelectChannelList();
        Intent intent=new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(BUNDLE_KEY_DATA_CHANGED, isDataChanged);
        intent.putExtras(bundle);
        setResult(RESULT_CODE_OK, intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
//    	super.onBackPressed();
    	CloseChannelActivity();
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
    protected abstract List<ChannelItem> getLocalChannelList();
    protected abstract List<ChannelItem> getAllChannelList();
    protected abstract void saveSelectChannelList();
}
