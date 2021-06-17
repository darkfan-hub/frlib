package com.frlib.basic.views.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.setPadding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.frlib.basic.R
import com.frlib.basic.adapter.BaseAdapter
import com.frlib.basic.databinding.FrlibLayoutListBinding
import com.frlib.basic.ext.init
import com.frlib.basic.fragment.IListFragment
import com.frlib.basic.views.RecyclerViewDivider
import com.frlib.basic.views.TitleBar
import com.frlib.basic.vm.BaseListViewModel
import com.frlib.utils.UIUtil
import com.frlib.utils.ext.color
import com.frlib.utils.ext.dp2px
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshHeader
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 30/05/2021 14:49
 * @desc
 */
abstract class AbstractFrListView<T>(
    context: Context,
    private val owner: LifecycleOwner
) : FrameLayout(context), IListFragment<T> {

    private val binding: FrlibLayoutListBinding by lazy {
        FrlibLayoutListBinding.inflate(LayoutInflater.from(context))
    }

    lateinit var listAdapter: BaseAdapter<T, BaseViewHolder>
    lateinit var recyclerView: RecyclerView
    lateinit var titleBar: TitleBar

    init {
        initView()
    }

    private fun initView() {
        addView(binding.root)

        setTitleBar()
        setSmartRefreshLayout()
        setRecyclerView()
        initObservables()
    }

    /**
     * 如果需要TitleBar, 重写这个方法
     */
    open fun setTitleBar() {
        if (useTitleBar()) {
            titleBar = binding.tbListTitle
            titleBar.visibility = View.VISIBLE
        }
    }

    /**
     * 设置刷新layout
     */
    open fun setSmartRefreshLayout() {
        binding.smartRefresh.init(
            enableRefresh = enableRefresh(),
            enableLoadMore = enableLoadMore(),
            autoLoadMore = true,
            refreshHeader = refreshHeader(),
            refreshBlock = { viewModel().refresh() },
            loadMoreBlock = { viewModel().loadMore() })
    }

    /**
     * 设置recycler view
     */
    open fun setRecyclerView() {
        recyclerView = binding.rvList
        with(binding.rvList) {
            layoutManager = layoutManager()
            listAdapter = recyclerViewAdapter()
            adapter = listAdapter
            recyclerViewDivider()?.let { addItemDecoration(it) }
        }
    }

    /**
     * view model
     */
    abstract fun viewModel(): BaseListViewModel<T>

    open fun initObservables() {
        viewModel().refreshDataLiveData.observe(owner, {
            if (it.isNotEmpty()) {
                changeRecyclePadding()
            }

            recyclerView.postDelayed({ listAdapter.setList(it) }, 10)
        })

        viewModel().loadMoreDataLiveData.observe(owner, { listAdapter.addData(it) })

        viewModel().emptyLiveData.observe(owner, { emptyData() })

        viewModel().networkErrorLiveData.observe(owner, { networkError() })

        viewModel().finishRefreshLiveData.observe(owner, { binding.smartRefresh.finishRefresh() })

        viewModel().finishLoadMoreLiveData.observe(owner, { binding.smartRefresh.finishLoadMore() })

        viewModel().loadMoreErrorLiveData.observe(owner, { binding.smartRefresh.finishLoadMore(false) })

        viewModel().noMoreDataLiveData.observe(owner, { binding.smartRefresh.finishLoadMoreWithNoMoreData() })
    }

    /**
     * recycler view layoutManager
     */
    override fun layoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    /**
     * recycler view adapter
     */
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

    /**
     * item点击事件
     */
    override fun itemClickListen(position: Int, item: T) {
    }

    /**
     * item 分割线
     */
    override fun recyclerViewDivider(): RecyclerViewDivider? {
        return RecyclerViewDivider(
            context,
            RecyclerViewDivider.HORIZONTAL_LIST,
            UIUtil.dp2px(context, 10f),
            context.color(R.color.color_transparent)
        )
    }

    /**
     * 修改recycler view的内间距，处理一些细节需求
     */
    override fun changeRecyclePadding(isEmpty: Boolean) {
        recyclerView.setPadding(if (isEmpty) 0 else recyclePadding())
    }

    /**
     * 修改recycler 内间距
     */
    open fun recyclePadding(): Int {
        return context.dp2px(10f)
    }

    /**
     * 空数据
     */
    override fun emptyData() {
        if (!listAdapter.hasEmptyView()) {
            listAdapter.setEmptyView(emptyView())
        }

        changeRecyclePadding(true)
        listAdapter.setList(null)
    }

    override fun networkError() {
        listAdapter.removeEmptyView()
        listAdapter.setEmptyView(networkErrorView())

        changeRecyclePadding(true)
        listAdapter.setList(null)
    }

    /**
     * 空页面
     */
    abstract fun emptyView(): View

    /**
     * 网络错误页面
     */
    abstract fun networkErrorView(): View

    /**
     * 刷新header
     */
    open fun refreshHeader(): RefreshHeader {
        val timeFormat =
            SimpleDateFormat(context.getString(R.string.frlib_text_header_update), Locale.getDefault())
        return ClassicsHeader(context).setTimeFormat(timeFormat)
    }

    override fun headerViewLayoutId(): Int? = -1

    override fun footerViewLayoutId(): Int? = -1

    /**
     * 默认不使用titlebar
     */
    override fun useTitleBar(): Boolean = false

    /**
     * 默认开启下拉刷新
     */
    open fun enableRefresh(): Boolean = true

    /**
     * 默认开启加载更多
     */
    open fun enableLoadMore(): Boolean = true
}