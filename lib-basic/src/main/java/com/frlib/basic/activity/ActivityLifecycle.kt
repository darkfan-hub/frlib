package com.frlib.basic.activity

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.frlib.basic.app.AppManager
import com.frlib.basic.app.IAppComponent
import com.frlib.basic.config.ConfigModule
import com.frlib.basic.fragment.FragmentLifecycle
import timber.log.Timber

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 11:29
 * @desc 默认activity生命周期监听回调
 */
class ActivityLifecycle(
    private val appComponent: IAppComponent
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Timber.i("${activity.javaClass.simpleName} Created!")
        AppManager.addActivity(activity)

        if (activity is IActivity) {
            activity.setupActivityComponent(appComponent)

            registerFragmentCallbacks(activity)
        }
    }

    /**
     * 注册fragment生命周期回调
     */
    private val fragmentLifecycle by lazy { FragmentLifecycle(appComponent) }
    private val fragmentLifecycleList by lazy { ArrayList<FragmentManager.FragmentLifecycleCallbacks>() }

    private fun registerFragmentCallbacks(activity: IActivity) {
        if (activity is FragmentActivity && activity.useFragment()) {
            val fragmentManager = activity.supportFragmentManager

            val configModule = appComponent.extras()[ConfigModule::javaClass.name] as ConfigModule
            configModule.injectFragmentLifecycle(activity, fragmentLifecycleList)
            fragmentLifecycleList.forEach {
                fragmentManager.registerFragmentLifecycleCallbacks(it, true)
            }
            fragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycle, true)
        }
    }

    private fun unregisterFragmentCallbacks(activity: IActivity) {
        if (activity is FragmentActivity && activity.useFragment()) {
            val fragmentManager = activity.supportFragmentManager

            fragmentLifecycleList.forEach {
                fragmentManager.unregisterFragmentLifecycleCallbacks(it)
            }
            fragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycle)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.i("${activity.javaClass.simpleName} Started!")
    }

    override fun onActivityResumed(activity: Activity) {
        Timber.i("${activity.javaClass.simpleName} Resumed!")
    }

    override fun onActivityPaused(activity: Activity) {
        Timber.i("${activity.javaClass.simpleName} Paused!")
    }

    override fun onActivityStopped(activity: Activity) {
        Timber.i("${activity.javaClass.simpleName} Stopped!")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Timber.i("${activity.javaClass.simpleName} SaveInstanceState!")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Timber.i("${activity.javaClass.simpleName} Destroyed!")
        if (activity is IActivity) {
            unregisterFragmentCallbacks(activity)
        }
        AppManager.removeActivity(activity)
    }
}