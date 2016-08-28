package com.android.backchina.interf;

import com.android.backchina.base.adapter.BaseDragAdapter;
import com.android.backchina.base.adapter.BaseRecyclerAdapter.BaseViewHolder;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


public class DragItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private OnItemChangeListener mOnItemChangeListener;
    
    private int mKeepItemCount = 1;

    public DragItemTouchHelperCallBack(OnItemChangeListener onItemChangeListener, int keepItemCount) {
        mOnItemChangeListener = onItemChangeListener;
        mKeepItemCount = keepItemCount;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == BaseDragAdapter.KEEP) {
            return makeMovementFlags(0, 0);
        }
        int dragFlags = ItemTouchHelper.UP
                | ItemTouchHelper.DOWN
                | ItemTouchHelper.LEFT
                | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            //the viewTypes between the source and the target are not same
            //can't move
            return false;
        }
        mOnItemChangeListener.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mOnItemChangeListener.onItemRemoved(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            //itemView is being dragged
            if (viewHolder instanceof BaseViewHolder) {
                ((BaseViewHolder) viewHolder).onDrag();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setAlpha(1.0f);
        if (viewHolder instanceof BaseViewHolder) {
            ((BaseViewHolder) viewHolder).onDragFinished();
        }
    }
}
