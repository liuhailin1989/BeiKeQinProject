package com.news.soft.backchina.base.adapter;

import com.news.soft.backchina.interf.IDragItem;
import com.news.soft.backchina.interf.OnItemChangeListener;
import com.news.soft.backchina.listener.OnItemClickListener;
import com.news.soft.backchina.listener.OnItemLongPressListener;
import com.news.soft.backchina.listener.OnItemRemovedListener;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public abstract class BaseDragAdapter<T> extends BaseChannelRecyclerAdapter<T> implements OnItemChangeListener<T>,IDragItem{

    public static final int KEEP = Integer.MIN_VALUE;

    /**
     * Tag to judge current item mode
     */
    private boolean isLongPressMode = false;
    /**
     * Item Count that can not change
     */
    private int mKeepItemCount = 1;

    private OnItemClickListener mOnItemClickListener;
    /**
     * Listener for users to do with views outside RecyclerView when enter LongPress Mode
     */
    private OnItemLongPressListener mOnLongPressListener;
    
    private OnItemRemovedListener<T> mOnItemRemovedListener;

    public BaseDragAdapter() {
    	
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= mKeepItemCount - 1) {
            return KEEP;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder.getItemViewType() != KEEP) {
            //change Background for different state
            if (isLongPressMode) {
                holder.onLongPressMode();
            } else {
                holder.onNormalMode();
            }
        }
        holder.itemView.setOnTouchListener(
                new OnItemTouchListener(holder));
    }

    @Override
    public void onItemRemoved(int position) {
        if (null != mOnItemRemovedListener) {
            mOnItemRemovedListener.onItemRemoved(position, mDatas.get(position));
        }
        mDatas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size());
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        moveDataInList(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    private void moveDataInList(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }
        T item = mDatas.get(fromPosition);
        mDatas.remove(item);
        mDatas.add(toPosition, item);
    }

    @Override
    public void onItemInsert(int position, T data) {
        //auto put the new item at the end
        mDatas.add(data);
        notifyItemInserted(mDatas.size() - 1);
        notifyItemRangeChanged(mDatas.size() - 1, mDatas.size());
    }

    public void setKeepItemCount(int keepItemCount) {
        mKeepItemCount = keepItemCount;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnLongPressListener(OnItemLongPressListener onLongPressListener) {
        mOnLongPressListener = onLongPressListener;
    }

    public void setOnItemRemovedListener(OnItemRemovedListener<T> onItemRemovedListener) {
        mOnItemRemovedListener = onItemRemovedListener;
    }

    /**
     * Change LongPressMode to Normal
     */
    public void quitLongPressMode() {
        if (isLongPressMode) {
            isLongPressMode = false;
            notifyDataSetChanged();
        }
    }
    
    public void requestLongPressMode(){
    	if (!isLongPressMode) {
            isLongPressMode = true;
            notifyDataSetChanged();
        }
    }

    /**
     * @return if current mode is LongPressMode or not
     */
    public boolean getLongPressMode() {
        return isLongPressMode;
    }

    private class OnItemTouchListener implements View.OnTouchListener {

        /**
         * Distance to judge whether ACTION_MOVE act on or not
         */
        private float mDownY;
        private float mDownX;
        private boolean isMoved = false;
        private BaseViewHolder mViewHolder;

        private Runnable mLongPressRunnable = new Runnable() {
            @Override
            public void run() {
                isLongPressMode = true;
                notifyDataSetChanged();
                onDragStart(mViewHolder);
                if (null != mOnLongPressListener) {
                    mOnLongPressListener.onLongPress();
                }
            }
        };

        public OnItemTouchListener(BaseViewHolder holder) {
            mViewHolder = holder;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isLongPressMode) {
                        //if current item state is longPressMode, start dragging immediately
                        onDragStart(mViewHolder);
                    } else {
                        mDownX = event.getX();
                        mDownY = event.getY();
                        isMoved = false;
                        v.postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isLongPressMode) {
                        if (isMoved) break;
                        if (Math.abs(event.getX() - mDownX) > 5 ||
                                Math.abs(event.getY() - mDownY) > 5) {
                            isMoved = true;
                            v.removeCallbacks(mLongPressRunnable);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isLongPressMode) {
                        v.removeCallbacks(mLongPressRunnable);
                        if (null != mOnItemClickListener) {
                            mOnItemClickListener.onItemClick(mViewHolder, mViewHolder.getAdapterPosition());
                        }
                    }
                    break;
            }
            return true;
        }
    }
}
