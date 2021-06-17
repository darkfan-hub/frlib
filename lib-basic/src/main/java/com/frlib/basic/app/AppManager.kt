package com.frlib.basic.app

import android.app.Activity
import android.os.Process
import com.frlib.utils.network.INetworkStateChangeListener
import com.frlib.utils.network.NetworkType
import java.util.*

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 17:13
 * @desc app 页面管理
 */
object AppManager {

    private val activityList: LinkedList<Activity> by lazy {
        LinkedList<Activity>()
    }

    /**
     * 获取最近启动的一个 [Activity], 此方法不保证获取到的 [Activity] 正处于前台可见状态
     * 即使 App 进入后台或在这个 [Activity] 中打开一个之前已经存在的 [Activity], 这时调用此方法
     * 还是会返回这个最近启动的 [Activity], 因此基本不会出现 {@return null} 的情况
     * 比较适合大部分的使用场景, 如 startActivity
     *
     * Tips: mActivityList 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     *
     * @return
     */
    fun getTopActivity(): Activity? {
        val size = activityList.size
        if (size > 0) {
            return activityList[size - 1]
        }

        return null
    }

    /**
     * 添加 [Activity] 到集合
     */
    fun addActivity(activity: Activity) {
        synchronized(AppManager) {
            activityList.add(activity)
        }
    }

    /**
     * 获取指定 [Activity] class 的实例,没有则返回 null(同一个 activity class 有多个实例,则返回最早创建的实例)
     *
     * @param activityClass
     */
    fun findActivity(activityClass: Class<*>): Activity? {
        activityList.forEach { activity ->
            if (activity.javaClass == activityClass) {
                return activity
            }
        }

        return null
    }

    /**
     * 删除集合里的指定的 [Activity] 实例
     *
     * @param activity
     */
    fun removeActivity(activity: Activity) {
        synchronized(AppManager) {
            if (activityList.contains(activity)) {
                activityList.remove(activity)
            }
        }
    }

    /**
     * 删除集合里的指定位置的 [Activity]
     *
     * @param position
     */
    fun removeActivity(position: Int) {
        if (position < 0 || position > activityList.size) return

        synchronized(AppManager) {
            activityList.removeAt(position)
        }
    }

    /**
     * 关闭指定的 [Activity] class 的所有的实例
     *
     * @param activityClass
     */
    fun killActivity(activityClass: Class<*>) {
        synchronized(AppManager) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()

                if (next.javaClass == activityClass) {
                    iterator.remove()
                    next.finish()
                }
            }
        }
    }

    /**
     * 关闭所有 [Activity]
     */
    fun killAll() {
        synchronized(AppManager) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                iterator.remove()
                next.finish()
            }
        }
    }

    /**
     * 关闭所有 [Activity],排除指定的 [Activity]
     *
     * @param excludeActivityClasses activity class
     */
    fun killAll(vararg excludeActivityClasses: Class<*>) {
        val excludeList = listOf(excludeActivityClasses) as List<*>
        synchronized(AppManager) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (excludeList.contains(next.javaClass)) {
                    continue
                }

                iterator.remove()
                next.finish()
            }
        }
    }

    /**
     * 关闭所有 [Activity],排除指定的 [Activity]
     *
     * @param excludeActivityName activity 的完整全路径
     */
    fun killAll(vararg excludeActivityName: String) {
        val excludeList = listOf(excludeActivityName) as List<*>
        synchronized(AppManager) {
            val iterator = activityList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (excludeList.contains(next.javaClass.name)) {
                    continue
                }

                iterator.remove()
                next.finish()
            }
        }
    }

    /**
     * 退出应用程序
     *
     * 此方法经测试在某些机型上并不能完全杀死 App 进程, 几乎试过市面上大部分杀死进程的方式, 但都发现没卵用, 所以此
     * 方法如果不能百分之百保证能杀死进程, 就不能贸然调用 [release()] 释放资源, 否则会造成其他问题, 如果您
     * 有测试通过的并能适用于绝大多数机型的杀死进程的方式, 望告知
     */
    fun appExit() {
        try {
            killAll()
            Process.killProcess(Process.myPid())
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 网络状态监听
     */
    private val networkStateChangeListenerList: LinkedList<INetworkStateChangeListener> by lazy {
        LinkedList<INetworkStateChangeListener>()
    }

    /**
     * 添加一条网络监听
     */
    fun addNetworkStateChangeListener(listener: INetworkStateChangeListener) {
        if (!networkStateChangeListenerList.contains(listener)) {
            networkStateChangeListenerList.add(listener)
        }
    }

    /**
     * 移除一条网络监听
     */
    fun removeNetworkStateChangeListener(listener: INetworkStateChangeListener) {
        if (networkStateChangeListenerList.contains(listener)) {
            networkStateChangeListenerList.remove(listener)
        }
    }

    /**
     * 网络已经连接
     */
    fun netWorkConnected(networkType: NetworkType) {
        networkStateChangeListenerList.forEach {
            it.onConnected(networkType)
        }
    }

    /**
     * 网络已经失去连接
     */
    fun netWorkDisconnected() {
        networkStateChangeListenerList.forEach {
            it.onDisconnected()
        }
    }
}