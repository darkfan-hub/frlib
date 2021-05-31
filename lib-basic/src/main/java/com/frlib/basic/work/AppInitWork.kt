package com.frlib.basic.work

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.frlib.basic.app.IApp
import com.frlib.basic.mobleinfo.AppInfoHelper
import timber.log.Timber

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 31/05/2021 22:23
 * @desc app 初始化work
 */
class AppInitWork(
    private val context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val app = context.applicationContext
        if (app is IApp) {
            val appComponent = app.appComponent()
            val info = AppInfoHelper.packageInfo(context)
            Timber.i("$info")
            appComponent.extras().put("appInfo", info)
        }
        return Result.success()
    }
}