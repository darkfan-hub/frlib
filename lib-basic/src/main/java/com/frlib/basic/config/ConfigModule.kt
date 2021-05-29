package com.frlib.basic.config

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 11:16
 * @desc 配置模块
 */
interface ConfigModule {

    /**
     * 使用 [GlobalConfig.Builder] 给框架配置一些配置参数
     *
     * @param context [Context]
     * @param builder [GlobalConfig.Builder]
     */
    fun applyOptions(context: Context, builder: GlobalConfig.Builder)

    /**
     * 使用 [Application.ActivityLifecycleCallbacks] 在 [Activity] 的生命周期中注入一些操作
     *
     * @param context    [Context]
     * @param lifecycleList [Activity] 的生命周期容器, 可向框架中添加多个 [Activity] 的生命周期类
     */
    fun injectActivityLifecycle(
        context: Context,
        lifecycleList: ArrayList<Application.ActivityLifecycleCallbacks>
    )

    /**
     * 使用 [FragmentManager.FragmentLifecycleCallbacks] 在 [Fragment] 的生命周期中注入一些操作
     *
     * @param context    [Context]
     * @param lifecycleList [Fragment] 的生命周期容器, 可向框架中添加多个 [Fragment] 的生命周期类
     */
    fun injectFragmentLifecycle(
        context: Context,
        lifecycleList: ArrayList<FragmentManager.FragmentLifecycleCallbacks>
    )
}