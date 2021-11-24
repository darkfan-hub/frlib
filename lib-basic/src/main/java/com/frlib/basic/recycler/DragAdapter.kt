package com.frlib.basic.recycler

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.frlib.basic.adapter.BaseAdapter
import java.util.*

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 04/10/2021 21:35
 * @desc
 */
open class DragAdapter<T>(
    layoutId: Int
) : BaseAdapter<T, BaseViewHolder>(layoutId), ItemTouchHelperAdapter {

    var onItemClearListener: OnItemClearListener? = null

    var canItemMove = true

    var itemMove = false

    override fun onItemMove(source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) {
        if (!canItemMove) return
        val fromPosition = source.adapterPosition
        val toPosition = target.adapterPosition

        if (fromPosition < data.size && toPosition < data.size) {
            // 交换数据位置
            Collections.swap(data, fromPosition, toPosition)
            itemMove = true
            // 刷新位置交换
            notifyItemMoved(fromPosition, toPosition)
        }
    }

    override fun onItemDismiss(source: RecyclerView.ViewHolder) {
        if (!canItemMove) return
        val position = source.adapterPosition
        // 移除数据
        data.removeAt(position)
        // 刷新数据移除
        notifyItemRemoved(position)
    }

    override fun onItemSelect(source: RecyclerView.ViewHolder?) {
    }

    override fun onItemClear(source: RecyclerView.ViewHolder?) {
        if (!canItemMove) return
        if (itemMove) {
            onItemClearListener?.onItemClear(source)
        }
    }

    interface OnItemClearListener {
        fun onItemClear(viewHolder: RecyclerView.ViewHolder?)
    }
}