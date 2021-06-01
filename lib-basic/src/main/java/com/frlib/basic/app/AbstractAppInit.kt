package com.frlib.basic.app

import android.app.Activity
import androidx.work.OneTimeWorkRequestBuilder
import com.alibaba.android.arouter.launcher.ARouter
import com.billy.android.swipe.SmartSwipeBack
import com.frlib.basic.activity.IActivity
import com.frlib.basic.helper.MmkvHelper
import me.jessyan.autosize.AutoSize
import timber.log.Timber

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 11:07
 * @desc
 */
abstract class AbstractAppInit : IAppInit {

    override fun allInit(app: IApp) {
    }

    override fun mainInit(app: IApp) {
        if (app.appComponent().enableDebug()) {
            ARouter.openLog()
            ARouter.openDebug()

            Timber.plant(Timber.DebugTree())
        }

        // auto size
        AutoSize.initCompatMultiProcess(app.appComponent().application())

        // 默认初始化mmkv
        MmkvHelper.initialize(app.appComponent().application())

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
    }

    override fun threadInit(app: IApp) {
        val initWork = OneTimeWorkRequestBuilder<AppInitWork>()
            .build()
        app.appComponent().workManager().enqueue(initWork)
    }
}