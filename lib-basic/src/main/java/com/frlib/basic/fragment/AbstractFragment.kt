package com.frlib.basic.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.frlib.basic.app.IAppComponent
import com.frlib.basic.defaultpages.DefaultPages
import com.frlib.basic.defaultpages.DefaultPagesContainer
import com.frlib.basic.defaultpages.Pages
import com.frlib.basic.defaultpages.state.EmptyState
import com.frlib.basic.defaultpages.state.SuccessState
import com.frlib.basic.ref.RefGenericSuperclass
import com.frlib.basic.toast.FrToasty
import com.frlib.basic.vm.BaseViewModel
import com.frlib.basic.vm.ViewModelFactory
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/05/2021 14:42
 * @desc
 */
abstract class AbstractFragment<DB : ViewDataBinding, VM : BaseViewModel> : Fragment(), IFragment {

    lateinit var selfContext: Context

    /** app 组件 */
    lateinit var appComponent: IAppComponent

    /** 数据绑定 */
    lateinit var dataBinding: DB

    /** fragment 布局 */
    lateinit var rootView: View

    /** ViewModel */
    lateinit var viewModel: VM

    /** loading popup */
    private var loading: LoadingPopupView? = null

    /** 缺省页容器 */
    var defaultPages: DefaultPagesContainer? = null

    /** 缺省页-空数据状态 */
    private val emptyState: EmptyState by lazy {
        EmptyState().apply {
            invokeView(emptyView)
        }
    }

    open val emptyView: View by lazy {
        appComponent.emptyDataView()
    }

    /** 缺省页-错误状态 */
    private val errorState: EmptyState by lazy {
        EmptyState().apply {
            invokeView(errorView)
        }
    }

    open val errorView: View by lazy {
        appComponent.emptyErrorView()
    }

    /** 缺省页-网络错误状态 */
    private val netErrorState: EmptyState by lazy {
        EmptyState().apply {
            invokeView(netErrorView)
        }
    }

    open val netErrorView: View by lazy {
        appComponent.emptyNetErrorView()
    }

    /** 缺省页-成功状态 */
    private val successState: SuccessState by lazy { SuccessState() }

    /** 数据加载状态 */
    private var dataLoaded = false

    override fun setupActivityComponent(appComponent: IAppComponent) {
        this.appComponent = appComponent
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        selfContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = setDataBinding(inflater, container)
        return rootView
    }

    /**
     * 设置data binding
     */
    private fun setDataBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        return dataBinding.root
    }

    /*private fun setDataBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): View {
        val useDataBinding = RefGenericSuperclass.findActualType(javaClass, ViewDataBinding::class.java)
        return if (useDataBinding) {
            dataBinding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
            dataBinding.root
        } else {
            inflater.inflate(layoutId(), container, false)
        }
    }*/

    /**
     * 页面布局id
     */
    abstract fun layoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        initView(savedInstanceState)
        initObservables()
        setClickListen()
        tryLoadData()
    }

    /**
     *  设置view model
     */
    private fun setViewModel() {
        val classVM = RefGenericSuperclass.findActualTypeClass(javaClass, BaseViewModel::class.java)
        if (classVM != null) {
            viewModel = ViewModelProvider(
                this,
                ViewModelFactory.default(appComponent.application())
            ).get((classVM as Class<VM>))
            viewModel.setupAppComponent(appComponent)
            lifecycle.addObserver(viewModel)
        }
    }

    override fun setData(data: Any?) {
    }

    override fun setClickListen() {
    }

    override fun initObservables() {
        viewModel.defUI.toast.observe(this, { FrToasty.normal(selfContext, it).show() })

        viewModel.defUI.showLoading.observe(this, {
            if (loading == null) {
                loading = XPopup.Builder(selfContext)
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .asLoading()
            }

            loading?.setTitle(it)
            loading?.show()
        })

        viewModel.defUI.hideLoading.observe(this, {
            loading?.dismiss()
        })

        viewModel.defUI.defaultPages.observe(this, {
            // 如果不需要使用缺省页直接return
            if (!useDefaultPages()) {
                return@observe
            }

            when (it!!) {
                Pages.EMPTY -> {
                    defaultPages().show(emptyState)
                }
                Pages.ERROR -> {
                    defaultPages().show(errorState)
                }
                Pages.NET_ERROR -> {
                    defaultPages().show(netErrorState)
                }
                Pages.LOADING -> {
                }
                Pages.SUCCESS -> {
                }
                Pages.CONTENT -> {
                    defaultPages().show(successState)
                }
            }
        })
    }

    private fun tryLoadData() {
        if (!dataLoaded) {
            initData()
            dataLoaded = true
        }
    }

    override fun initData() {
    }

    override fun defaultPages(): DefaultPagesContainer {
        if (defaultPages == null) {
            defaultPages = DefaultPages.bindDefaultPages(rootView)
        }

        return defaultPages!!
    }

    override fun useDefaultPages(): Boolean = true

    override fun useEventBus(): Boolean = false

    override fun onDestroy() {
        super.onDestroy()
        dataLoaded = false
        lifecycle.removeObserver(viewModel)
    }
}