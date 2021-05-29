package com.frlib.basic.ext

import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/05/2021 16:30
 * @desc View ktx扩展
 */

/**
 * 下拉刷新配置
 */
fun SmartRefreshLayout.init(
    enableRefresh: Boolean = true,
    enableLoadMore: Boolean = true,
    autoLoadMore: Boolean = true,
    refreshHeader: RefreshHeader,
    refreshBlock: () -> Unit = {},
    loadMoreBlock: () -> Unit = {}
) {
    this.setEnableRefresh(enableRefresh) // 启用下拉刷新功能
    this.setEnableOverScrollBounce(true) // 启用越界回弹
    this.setEnableOverScrollDrag(true) // 启用越界拖动
    this.setDisableContentWhenRefresh(true) // 在刷新的时候禁止列表的操作
    this.setDisableContentWhenLoading(true) // 在加载的时候禁止列表的操作
    this.setEnableLoadMore(enableLoadMore) // 是否启用上拉加载功能
    if (autoLoadMore) {
        // 自动加载
        this.autoLoadMore()
    }
    this.setRefreshHeader(refreshHeader)
    this.setOnRefreshListener { refreshBlock() }
    this.setOnLoadMoreListener { loadMoreBlock() }
}