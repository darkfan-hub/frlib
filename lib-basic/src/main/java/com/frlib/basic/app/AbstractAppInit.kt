package com.frlib.basic.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.work.OneTimeWorkRequestBuilder
import com.alibaba.android.arouter.launcher.ARouter
import com.billy.android.swipe.SmartSwipeBack
import com.frlib.basic.activity.IActivity
import com.frlib.utils.SysUtil
import com.frlib.utils.network.NetworkUtil
import me.jessyan.autosize.AutoSize
import timber.log.Timber

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 11:07
 * @desc
 */
abstract class AbstractAppInit : IAppInit {

    override fun allInit(app: Application) {
    }

    override fun mainInit(app: IApp) {
        if (app.appComponent().enableDebug()) {
            ARouter.openLog()
            ARouter.openDebug()

            Timber.plant(Timber.DebugTree())
        }

        // auto size
        AutoSize.initCompatMultiProcess(app.appComponent().application())

        // 初始化ARouter
        ARouter.init(app.appComponent().application())

        // 全局侧滑返回
        SmartSwipeBack.activitySlidingBack(
            app.appComponent().application(),
            object : SmartSwipeBack.ActivitySwipeBackFilter {
                override fun onFilter(activity: Activity): Boolean {
                    if (activity is IActivity) {
                        return activity.useSwipe()
                    }

                    return false
                }
            })

        // 注册网络监听
        val connectivityManager =
            app.appComponent().application().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (SysUtil.isAndroid7()) {
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Timber.i("onAvailable -> $network")
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Timber.i("onLost -> $network")
                }

                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val validated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    Timber.i("onCapabilitiesChanged -> validated = $validated")
                    val internet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    Timber.i("onCapabilitiesChanged -> internet = $internet")

                    if (validated && internet) {
                        val networkType = NetworkUtil.networkType(app.appComponent().application())
                        Timber.i("networkType.getValue() -> ${networkType.getValue()}")
                        AppManager.netWorkConnected(networkType)
                    }
                    Timber.i("=========================================")
                }
            })
        }
    }

    override fun threadInit(app: IApp) {
        val initWork = OneTimeWorkRequestBuilder<AppInitWork>()
            .build()
        app.appComponent().workManager().enqueue(initWork)
    }
}