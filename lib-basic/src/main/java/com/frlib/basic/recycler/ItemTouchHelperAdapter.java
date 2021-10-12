package com.frlib.basic.recycler;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperAdapter {

    /**
     * 数据交换
     *
     * @param source
     * @param target
     */
    void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);

    /**
     * 数据删除
     * @param source
     */
    void onItemDismiss(RecyclerView.ViewHolder source);

    /**
     * drag或者swipe选中
     * @param source
     */
    void onItemSelect(RecyclerView.ViewHolder source);

    /**
     * 状态清除
     * @param source
     */
    void onItemClear(RecyclerView.ViewHolder source);
}