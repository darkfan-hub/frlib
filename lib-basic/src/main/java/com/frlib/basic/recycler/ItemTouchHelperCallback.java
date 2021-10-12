package com.frlib.basic.recycler;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import timber.log.Timber;

/**
 * @author lining
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private DragAdapter mAdapter;

    public ItemTouchHelperCallback(DragAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 只允许上下拖动
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        // 长按启用拖拽
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        // 不启用拖拽删除
        return false;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        // 通过接口传递拖拽交换数据的起始位置和目标位置的ViewHolder
        mAdapter.onItemMove(source, target);
        return true;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Timber.d("onSwiped:");
        // 移动删除回调,如果不用可以不用理
        // mAdapter.onItemDissmiss(viewHolder);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        Timber.d("actionState:" + actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            // 当滑动或者拖拽view的时候通过接口返回该ViewHolder
            mAdapter.onItemSelect(viewHolder);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        Timber.d("clearView:" + !recyclerView.isComputingLayout());
        if (!recyclerView.isComputingLayout()) {
            Timber.d("clearView2:" + !recyclerView.isComputingLayout());
            // 当需要清除之前在onSelectedChanged或者onChildDraw,onChildDrawOver设置的状态或者动画时通过接口返回该ViewHolder
            mAdapter.onItemClear(viewHolder);
        }
    }
}