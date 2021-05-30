package com.frlib.basic.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.frlib.basic.R
import com.frlib.basic.app.IAppComponent
import com.frlib.basic.defaultpages.DefaultPages
import com.frlib.basic.defaultpages.DefaultPagesContainer
import com.frlib.basic.defaultpages.Pages
import com.frlib.basic.defaultpages.state.EmptyState
import com.frlib.basic.defaultpages.state.SuccessState
import com.frlib.basic.vm.BaseViewModel
import com.frlib.basic.vm.ViewModelFactory
import com.frlib.basic.ref.RefGenericSuperclass
import com.frlib.basic.toast.FrToasty
import com.frlib.basic.views.EmptyView
import com.frlib.utils.ext.string
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 17:55
 * @desc
 */
abstract class AbstractActivity<DB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(), IActivity {

    /** activity 本身 */
    lateinit var self: AppCompatActivity

    /** app 组件 */
    private lateinit var appComponent: IAppComponent

    /** 数据绑定 */
    lateinit var dataBinding: DB

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

    override fun setupActivityComponent(appComponent: IAppComponent) {
        this.appComponent = appComponent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        self = this

        setDataBinding()
        supportActionBar?.hide()
        setViewModel()
        initView(savedInstanceState)
        initObservables()
        setClickListen()
        initData()
    }

    /**
     * 设置data binding
     */
    private fun setDataBinding() {
        val useDataBinding = RefGenericSuperclass.findActualType(javaClass, ViewDataBinding::class.java)
        if (useDataBinding) {
            dataBinding = DataBindingUtil.setContentView(self, layoutId())
        } else {
            setContentView(layoutId())
        }
    }

    /**
     * 页面布局id
     */
    abstract fun layoutId(): Int

    /**
     *  设置view model
     */
    private fun setViewModel() {
        val classVM = RefGenericSuperclass.findActualTypeClass(javaClass, BaseViewModel::class.java)
        if (classVM != null) {
            viewModel = ViewModelProvider(self, ViewModelFactory.default(appComponent)).get((classVM as Class<VM>))
            lifecycle.addObserver(viewModel)
        }
    }

    override fun initObservables() {
        viewModel.defUI.toast.observe(self, { FrToasty.normal(this, it).show() })

        viewModel.defUI.loading.observe(self, {
            if (loading == null) {
                loading = XPopup.Builder(self)
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .asLoading("正在加载中...")
            }

            if (it) {
                loading?.show()
            } else {
                loading?.dismiss()
            }
        })

        viewModel.defUI.defaultPages.observe(self, {
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

    override fun setClickListen() {
    }

    override fun initData() {
    }

    override fun defaultPages(): DefaultPagesContainer {
        if (defaultPages == null) {
            defaultPages = DefaultPages.bindDefaultPages(self)
        }
        return defaultPages!!
    }

    override fun useFragment(): Boolean = false

    override fun useDefaultPages(): Boolean = true

    override fun useSwipe(): Boolean = true

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}