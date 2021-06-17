package com.frlib.basic.activity

import android.os.Bundle
import android.util.LruCache
import com.frlib.basic.app.IAppComponent
import com.frlib.basic.defaultpages.DefaultPagesContainer
import com.frlib.utils.network.INetworkStateChangeListener

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 11:22
 * @desc
 */
interface IActivity {

    /**
     * 提供 AppComponent (提供所有的单例对象) 给实现类, 进行 Component 依赖
     *
     * @param appComponent
     */
    fun setupActivityComponent(appComponent: IAppComponent)

    /**
     * 初始化 View, 如果 {@link #initView(Bundle)} 返回 0, 框架则不会调用 {@link Activity#setContentView(int)}
     *
     * @param savedInstanceState
     * @return
     */
    fun initView(savedInstanceState: Bundle?)

    /**
     * 初始化数据
     */
    fun initData()

    /**
     * 设置点击事件
     */
    fun setClickListen()

    /**
     * 初始化Observable
     */
    fun initObservables()

    /**
     * 网络状态改变监听
     */
    fun networkChangeListener(): INetworkStateChangeListener

    /**
     * 这个 Activity 是否会使用 Fragment,框架会根据这个属性判断是否注册 [FragmentManager.FragmentLifecycleCallbacks]
     * 如果返回`false`,那意味着这个 Activity 不需要绑定 Fragment,那你再在这个 Activity 中绑定继承于 [BaseFragment] 的 Fragment 将不起任何作用
     *
     * @return
     * @see ActivityLifecycle.registerFragmentCallbacks
     */
    fun useFragment(): Boolean

    /**
     * 是否使用缺省页
     *
     * 统一管理缺省页
     */
    fun useDefaultPages(): Boolean

    /**
     * 缺省页容器
     */
    fun defaultPages(): DefaultPagesContainer

    /**
     * 使用侧滑返回
     *
     * @return 返回 `true`, 会自动开启侧滑返回
     */
    fun useSwipe(): Boolean
}