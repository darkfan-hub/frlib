package com.frlib.basic.fragment

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.frlib.basic.adapter.BaseAdapter
import com.frlib.basic.views.RecyclerViewDivider

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/05/2021 15:05
 * @desc list fragment 接口
 */
interface IListFragment<T> {

    /**
     * 列表item布局id
     */
    fun itemLayoutId(): Int

    /**
     * 列表头部布局id
     */
    fun headerViewLayoutId(): Int?

    /**
     * 列表尾部部布局id
     */
    fun footerViewLayoutId(): Int?

    /**
     * 列表布局管理(列表、网格、瀑布流)
     */
    fun layoutManager(): RecyclerView.LayoutManager

    /**
     * 列表item点击事件
     */
    fun itemClickListen(position: Int, item: T)

    /**
     * 绑定view
     */
    fun convertView(helper: BaseViewHolder, item: T)

    /**
     * 使用框架标题栏
     */
    fun useTitleBar(): Boolean

    /**
     * 列表空布局
     */
    fun emptyData()

    /**
     * 列表适配器
     */
    fun recyclerViewAdapter(): BaseAdapter<T, BaseViewHolder>

    /**
     * 列表item间隔
     */
    fun recyclerViewDivider(): RecyclerViewDivider?

    /**
     * 动态修改recycle view padding
     * 只有在下拉刷新时调用
     *
     * [isEmpty] 是否是空数据
     */
    fun changeRecyclePadding(isEmpty: Boolean = false)
}