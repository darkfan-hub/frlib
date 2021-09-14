package com.frlib.basic.ext

import android.app.Activity
import android.view.View
import com.frlib.basic.config.AppConstants
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader
import timber.log.Timber

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
    this.setEnableAutoLoadMore(false) // 是否启用列表惯性滑动到底部时自动加载更多
    this.setDisableContentWhenRefresh(true) // 在刷新的时候禁止列表的操作
    this.setDisableContentWhenLoading(true) // 在加载的时候禁止列表的操作
    this.setEnableLoadMore(enableLoadMore) // 是否启用上拉加载功能
    this.setOnRefreshListener { refreshBlock() }
    this.setOnLoadMoreListener { loadMoreBlock() }
    /*if (autoLoadMore) {
        // 自动加载
        this.autoLoadMore()
    }*/
    this.setRefreshHeader(refreshHeader)
    this.setRefreshFooter(ClassicsFooter(this.context))
}

/**
 * 沉浸式状态栏全屏
 */
fun Activity.fullScreen() {
    ImmersionBar.with(this)
        .fullScreen(true)
        .init()
}

/**
 * 沉浸式状态栏默认样式
 */
fun Activity.statusBarStyle(
    statusBarColor: String = "#00000000",
    navigationBarColor: String = "#FFFFFFFF",
    statusBarDarkFont: Boolean = true,
    navigationBarDarkIcon: Boolean = true,
    fitsSystemWindows: Boolean = false
) {
    ImmersionBar.with(this)
        .statusBarColor(if (statusBarColor.startsWith("#")) statusBarColor else "#$statusBarColor")
        .statusBarDarkFont(statusBarDarkFont)
        .fitsSystemWindows(fitsSystemWindows)
        .navigationBarColor(navigationBarColor)
        .navigationBarDarkIcon(navigationBarDarkIcon)
        .init()
}

private var lastClickTime = 0L
private var lastClickTag = 0

fun View.click(block: () -> Unit) {
    this.setOnClickListener {
        val currentTag = this.id
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime < AppConstants.double_click_time && lastClickTag == currentTag) {
            Timber.i("%s 毫秒内发生快速点击：%d", AppConstants.double_click_time, currentTag)
            return@setOnClickListener
        }
        lastClickTime = currentTimeMillis
        lastClickTag = currentTag
        block()
    }
}

fun View.isFast(block: () -> Unit) {
    val currentTag = this.id
    val currentTimeMillis = System.currentTimeMillis()
    if (currentTimeMillis - lastClickTime < AppConstants.double_click_time && lastClickTag == currentTag) {
        Timber.i("%s 毫秒内发生快速点击：%d", AppConstants.double_click_time, currentTag)
        return
    }
    lastClickTime = currentTimeMillis
    lastClickTag = currentTag
    block()
}