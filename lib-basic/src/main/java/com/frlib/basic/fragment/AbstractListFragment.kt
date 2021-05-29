package com.frlib.basic.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.frlib.basic.R
import com.frlib.basic.adapter.BaseAdapter
import com.frlib.basic.databinding.FrlibFragmentListBinding
import com.frlib.basic.ext.init
import com.frlib.basic.views.RecyclerViewDivider
import com.frlib.basic.views.TitleBar
import com.frlib.basic.vm.BaseListViewModel
import com.frlib.utils.UIUtil
import com.frlib.utils.ext.color
import com.frlib.utils.ext.dp2px

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/05/2021 15:07
 * @desc list fragment
 */
abstract class AbstractListFragment<T, VM : BaseListViewModel<T>> :
    AbstractFragment<FrlibFragmentListBinding, VM>(), IListFragment<T> {

    lateinit var listAdapter: BaseAdapter<T, BaseViewHolder>
    lateinit var recyclerView: RecyclerView
    lateinit var titleBar: TitleBar

    override fun layoutId(): Int = R.layout.frlib_fragment_list

    override fun initView(savedInstanceState: Bundle?) {
        setTitleBar()
        setSmartRefreshLayout()
        setRecyclerView()
    }

    /**
     * 如果需要TitleBar, 重写这个方法
     */
    open fun setTitleBar() {
        if (useTitleBar()) {
            titleBar = dataBinding.tbListTitle
            titleBar.visibility = View.VISIBLE
        }
    }

    /**
     * 设置刷新layout
     */
    open fun setSmartRefreshLayout() {
        dataBinding.smartRefresh.init(
            enableRefresh = enableRefresh(),
            enableLoadMore = enableLoadMore(),
            autoLoadMore = true,
            refreshHeader = appComponent.refreshHeader(),
            refreshBlock = { viewModel.refresh() },
            loadMoreBlock = { viewModel.loadMore() })
    }

    /**
     * 设置recycler view
     */
    open fun setRecyclerView() {
        recyclerView = dataBinding.rvList
        with(recyclerView) {
            layoutManager = layoutManager()
            listAdapter = recyclerViewAdapter()
            adapter = listAdapter
            recyclerViewDivider()?.let { addItemDecoration(it) }
        }
    }

    override fun initObservables() {
        super.initObservables()

        viewModel.refreshDataLiveData.observe(this, {
            if (it.isNotEmpty()) {
                changeRecyclePadding()
            }

            recyclerView.postDelayed({ listAdapter.setList(it) }, 10)
        })

        viewModel.loadMoreDataLiveData.observe(this, { listAdapter.addData(it) })

        viewModel.emptyLiveData.observe(this, { emptyData() })

        viewModel.finishRefreshLiveData.observe(this, { dataBinding.smartRefresh.finishRefresh() })

        viewModel.finishLoadMoreLiveData.observe(this, { dataBinding.smartRefresh.finishLoadMore() })

        viewModel.loadMoreErrorLiveData.observe(this, { dataBinding.smartRefresh.finishLoadMore(false) })

        viewModel.noMoreDataLiveData.observe(this, { dataBinding.smartRefresh.finishLoadMoreWithNoMoreData() })
    }

    override fun initData() {
        viewModel.refresh(showDialog = true)
    }

    override fun recyclerViewAdapter(): BaseAdapter<T, BaseViewHolder> {
        return BaseAdapter<T, BaseViewHolder>(itemLayoutId()).apply {

            callback = object : BaseAdapter.ConvertCallback<T> {
                override fun convert(holder: BaseViewHolder, item: T) {
                    convertView(holder, item)
                }
            }

            setOnItemClickListener { adapter, _, position ->
                val item = adapter.data[position] as T
                itemClickListen(position, item)
            }
        }
    }

    override fun emptyData() {

    }

    override fun recyclerViewDivider(): RecyclerViewDivider? {
        return RecyclerViewDivider(
            selfContext,
            RecyclerViewDivider.HORIZONTAL_LIST,
            UIUtil.dp2px(selfContext, 10f),
            selfContext.color(R.color.color_transparent)
        )
    }

    override fun layoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(selfContext)
    }

    override fun changeRecyclePadding(isEmpty: Boolean) {
        recyclerView.setPadding(if (isEmpty) 0 else selfContext.dp2px(10f))
    }

    override fun headerViewLayoutId(): Int? = -1

    override fun footerViewLayoutId(): Int? = -1

    override fun useTitleBar(): Boolean = false

    open fun enableRefresh(): Boolean = true

    open fun enableLoadMore(): Boolean = true
}