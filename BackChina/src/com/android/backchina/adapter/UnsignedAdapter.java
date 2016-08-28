package com.android.backchina.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.backchina.R;
import com.android.backchina.base.adapter.BaseDragAdapter;
import com.android.backchina.bean.ChannelItem;

public class UnsignedAdapter extends BaseDragAdapter<ChannelItem>{

	public UnsignedAdapter(){
		
	}

	@Override
	public void onBindViewHolder(BaseViewHolder holder,final int position) {
		// TODO Auto-generated method stub
		if(holder != null && holder instanceof UnsignedViewHolder){
			ChannelItem item = mDatas.get(position);
			UnsignedViewHolder viewHolder = (UnsignedViewHolder) holder;
			viewHolder.mTextView.setText(item.getName());
		}
		super.onBindViewHolder(holder, position);
	}
	
    @Override
    public UnsignedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource = R.layout.layout_channel_item;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new UnsignedViewHolder(itemView);
    }
    

	@Override
	public boolean onItemMove(int fromPosition, int toPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static class  UnsignedViewHolder extends BaseViewHolder {

	    private Context mContext;
	    public LinearLayout mContainer;
	    public TextView mTextView;
	    public ImageView mDelete;

	    public UnsignedViewHolder(View itemView) {
	        super(itemView);
	        mContext = itemView.getContext();
	        mContainer = (LinearLayout) itemView.findViewById(R.id.ll_channel_container);
	        mTextView = (TextView) itemView.findViewById(R.id.tv_content);
	        mDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
	        mDelete.setVisibility(View.GONE);
	    }

	    @Override
	    public void onDrag() {
	    }

	    @Override
	    public void onDragFinished() {
	    }

	    @Override
	    public void onLongPressMode() {
	    }

	    @Override
	    public void onNormalMode() {
	    }
	}

	@Override
	public void onDragStart(ViewHolder viewHolder) {
		// TODO Auto-generated method stub
		
	}

}
