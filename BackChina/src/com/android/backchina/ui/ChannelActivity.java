
package com.android.backchina.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.android.backchina.bean.base.ChannelBean;
import com.android.backchina.interf.DragItemTouchHelperCallBack;
import com.android.backchina.listener.OnItemClickListener;
import com.android.backchina.listener.OnItemLongPressListener;
import com.android.backchina.listener.OnItemRemovedListener;
import com.android.backchina.manager.ChannelManager;
import com.android.backchina.manager.DragGridLayoutManager;
import com.android.backchina.utils.TLog;
import com.android.backchina.widget.DragRecyclerView;
import com.android.backchina.widget.UnsignedRecyclerView;
import com.google.gson.reflect.TypeToken;

/**
 * 频道管理
 */
public class ChannelActivity extends BaseActivity {
    public static String TAG = "ChannelActivity";

    private ImageView mBtnClose;
    
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
    
    private List<ChannelItem> myChannelData = new ArrayList<ChannelItem>();
    
    private List<ChannelItem> moreChannelData = new ArrayList<ChannelItem>();

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
                moreChannelData.add(removedItem);
                myChannelData.remove(removedItem);
            }
        });
        mDragView.setAdapter(mDragAdapter);
        mDragView.setLayoutManager(mDragGridManager);
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
            }
        });

        mUnsignedAdapter.setOnItemRemovedListener(new OnItemRemovedListener<ChannelItem>() {

            @Override
            public void onItemRemoved(int position, ChannelItem removedItem) {
                // TODO Auto-generated method stub
                mDragAdapter.onItemInsert(position, removedItem);
                myChannelData.add(removedItem);
                moreChannelData.remove(removedItem);
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

    private void requeseData() {
        myChannelData.clear();
        moreChannelData.clear();
        myChannelData = ChannelManager.getInstance().getNewsChannelItemsFromTabLocal(mContext);
        List<ChannelItem> data = ChannelManager.getInstance().getNewsChannelItemsFromTabAll(mContext);
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

    private boolean isInMyChannelData(ChannelItem item){
        if(myChannelData == null || myChannelData.size() <= 0){
            return false;
        }
        for(ChannelItem data : myChannelData){
            if(data.getId() == item.getId()){
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
        ChannelManager.getInstance().saveNewsChannelItemToTabLocal(mContext, myChannelData);
        finish();
    }
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
    
}
