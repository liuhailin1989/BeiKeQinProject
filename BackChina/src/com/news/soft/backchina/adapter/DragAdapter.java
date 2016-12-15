package com.news.soft.backchina.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.news.soft.backchina.R;
import com.news.soft.backchina.base.adapter.BaseDragAdapter;
import com.news.soft.backchina.bean.ChannelItem;

public class DragAdapter extends BaseDragAdapter<ChannelItem>{

    private ItemTouchHelper mItemTouchHelper;
    
    public DragAdapter() {
        
    }

    public void setItemTouchHelper(ItemTouchHelper helper){
        mItemTouchHelper = helper;
    }
    
    @Override
    public void onBindViewHolder(BaseViewHolder holder,final int position) {
        // TODO Auto-generated method stub
        if (holder != null && holder instanceof DragViewHolder) {
            DragViewHolder viewHolder = (DragViewHolder) holder;
            ChannelItem item = mDatas.get(position);
            viewHolder.mTextView.setText(item.getName());
            viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemRemoved(position);
                }
            });
        }
        super.onBindViewHolder(holder, position);
    }
    
    @Override
    public DragViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource = R.layout.layout_channel_item;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new DragViewHolder(itemView);
    }

    public static class DragViewHolder extends BaseViewHolder {

        private Context mContext;
        public LinearLayout mContainer;
        public TextView mTextView;
        public ImageView mDelete;

        public DragViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mContainer = (LinearLayout) itemView.findViewById(R.id.ll_channel_container);
            mTextView = (TextView) itemView.findViewById(R.id.tv_content);
            mDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            mDelete.setVisibility(View.GONE);
        }

        @Override
        public void onDrag() {
            mTextView.setTextColor(Color.RED);
        }

        @Override
        public void onDragFinished() {
            mTextView.setTextColor(Color.BLACK);
        }

        @Override
        public void onLongPressMode() {
            mDelete.setVisibility(View.VISIBLE);
            mContainer.setBackground(mContext.getResources().getDrawable(R.drawable.border_longpress));
        }

        @Override
        public void onNormalMode() {
            mDelete.setVisibility(View.GONE);
            mContainer.setBackground(mContext.getResources().getDrawable(R.drawable.border_normal));

        }
    }

    @Override
    public void onDragStart(ViewHolder viewHolder) {
        // TODO Auto-generated method stub
        if(mItemTouchHelper != null){
        mItemTouchHelper.startDrag(viewHolder);
        }
    }
}
