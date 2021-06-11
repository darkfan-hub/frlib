package com.frlib.basic.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.frlib.basic.data.entity.BasicApiEntity
import com.frlib.basic.data.entity.BasicApiPageEntity
import com.frlib.basic.lifecycle.SingleLiveEvent

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/05/2021 16:09
 * @desc list view model 基类
 */
abstract class BaseListViewModel<T>(
    application: Application
) : BaseViewModel(application) {

    val queryMap = HashMap<String, Any>()

    var dataList: ArrayList<T> = arrayListOf()

    /** 刷新数据 */
    val refreshDataLiveData by lazy { MutableLiveData<List<T>>() }

    /** 刷新结束 */
    val finishRefreshLiveData by lazy { SingleLiveEvent<Void>() }

    /** 没有更多 */
    val noMoreDataLiveData by lazy { SingleLiveEvent<Void>() }

    /** 加载更多数据 */
    val addDataLiveData by lazy { MutableLiveData<T>() }

    /** 加载更多数据 */
    val loadMoreDataLiveData by lazy { MutableLiveData<List<T>>() }

    /** 加载更多结束 */
    val finishLoadMoreLiveData by lazy { SingleLiveEvent<Void>() }

    /** 加载更多错误 */
    val loadMoreErrorLiveData by lazy { SingleLiveEvent<Void>() }

    /** 空布局 */
    val emptyLiveData by lazy { SingleLiveEvent<Void>() }

    /** 当前页下标 */
    open var pageIndex = 1

    /** 一共多少页 */
    open var totalPage = 1

    /** 每页数据大小 */
    open val pageSize = 15

    /** 是否刷新中 */
    private var isRefresh = false

    /**
     * 加载数据
     */
    abstract suspend fun loadData(): List<T>

    /**
     * 下拉刷新
     */
    open fun refresh(showDialog: Boolean = false) {
        launchOfResult(
            showLoading = showDialog,
            block = { onRefresh() },
            success = {
                refreshData(it)
                refreshDataLiveData.postValue(it)

                if (it.isEmpty()) {
                    emptyLiveData.call()
                }

                if (it.isEmpty() || it.size < pageSize || isLastPage()) {
                    noMoreDataLiveData.call()
                }
            },
            error = {
                defUI.toast.postValue(it.errMsg)
                emptyLiveData.call()
            },
            complete = {
                // 结束下拉刷新
                finishRefreshLiveData.call()
            }
        )
    }

    private suspend fun onRefresh(): List<T> {
        isRefresh = true
        pageIndex = 1
        queryMap["pageRow"] = pageSize
        queryMap["pageNo"] = pageIndex
        return loadData()
    }

    /**
     * 加载更多
     */
    open fun loadMore() {
        launchOfResult(
            block = {
                onLoadMore()
            },
            success = {
                if (it.isNotEmpty()) {
                    addData(it)
                    loadMoreDataLiveData.postValue(it)
                }

                finishLoadMoreLiveData.call()
                if (isLastPage()) {
                    noMoreDataLiveData.call()
                }
            },
            error = {
                loadMoreErrorLiveData.call()
            }
        )
    }

    suspend fun onLoadMore(): List<T> {
        isRefresh = false
        pageIndex++
        queryMap["pageNo"] = pageIndex
        return loadData()
    }

    /**
     * 添加数据到集合里
     */
    private fun addData(dataList: List<T>) {
        if (dataList.isNotEmpty()) {
            this.dataList.addAll(dataList)
        }
    }

    /**
     * 刷新数据
     */
    private fun refreshData(dataList: List<T>, clearOldData: Boolean = true) {
        if (clearOldData) {
            this.dataList.clear()
        }

        addData(dataList)
    }

    /**
     * 分页数据处理
     *
     * @return 数据集合
     */
    open fun handlerPageResult(result: BasicApiEntity<BasicApiPageEntity<T>>): List<T> {
        val pageResult = result.data
        return if (pageResult.objects.isNotEmpty()) pageResult.objects else mutableListOf()
    }

    /**
     * 查找当前item的下标
     */
    fun findIndex(t: T): Int {
        var index = -1

        dataList.forEachIndexed { i, it ->
            if (it == t) {
                index = i
                return@forEachIndexed
            }
        }
        return index
    }

    private fun isLastPage(): Boolean = pageIndex >= totalPage
}