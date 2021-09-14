package com.frlib.basic.activity

import android.content.res.Resources
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
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
import com.frlib.utils.KeyboardUtil
import com.frlib.utils.network.INetworkStateChangeListener
import com.frlib.utils.network.NetworkType
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import me.jessyan.autosize.AutoSizeCompat
import timber.log.Timber

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

    /** 网络状态监听回调 */
    private var networkStateChangeListener: INetworkStateChangeListener? = null

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
            viewModel = ViewModelProvider(
                self,
                ViewModelFactory.default(appComponent.application())
            ).get((classVM as Class<VM>))
            viewModel.setupAppComponent(appComponent)
            lifecycle.addObserver(viewModel)
        }
    }

    override fun initObservables() {
        viewModel.defUI.toast.observe(self, { FrToasty.normal(this, it).show() })


        viewModel.defUI.showLoading.observe(self, {
            if (loading == null) {
                loading = XPopup.Builder(self)
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(false)
                    .asLoading()
            }

            loading?.setTitle(it)
            loading?.show()
        })

        viewModel.defUI.hideLoading.observe(self, {
            loading?.dismiss()
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
        findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT).setOnClickListener {
            KeyboardUtil.hideSoftInput(self)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Timber.i("dispatchTouchEvent -> ${ev.action}")
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val currentView = currentFocus
            currentView?.let {
                it.clearFocus()
                KeyboardUtil.hideSoftInput(self, it)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun initData() {
    }

    override fun networkChangeListener(): INetworkStateChangeListener {
        if (networkStateChangeListener == null) {
            networkStateChangeListener = object : INetworkStateChangeListener {
                override fun onConnected(networkType: NetworkType) {
                    if (useDefaultPages()) {
                        viewModel.defaultPages(Pages.CONTENT)
                    }
                }

                override fun onDisconnected() {
                    if (useDefaultPages()) {
                        viewModel.defaultPages(Pages.NET_ERROR)
                    }
                }
            }
        }

        return networkStateChangeListener!!
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
        KeyboardUtil.hideSoftInput(self)
        lifecycle.removeObserver(viewModel)
    }

    /** auto size 相关*/
    /*override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return sizeInDp()
    }*/

    open fun resetResources(): Boolean {
        return true
    }

    open fun sizeInHeightDp(): Float {
        return 812f
    }

    override fun getResources(): Resources {
        if (resetResources()) {
            AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()))
            AutoSizeCompat.autoConvertDensity(super.getResources(), sizeInHeightDp(), false)
        }
        return super.getResources()
    }
}