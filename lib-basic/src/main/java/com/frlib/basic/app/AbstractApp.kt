package com.frlib.basic.app

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import com.frlib.basic.activity.ActivityLifecycle
import com.frlib.basic.config.ConfigModule
import com.frlib.basic.config.GlobalConfig
import com.frlib.basic.helper.MmkvHelper
import com.frlib.utils.BuildConfig
import com.frlib.utils.SysUtil

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 11:20
 * @desc 框架Application基类
 */
abstract class AbstractApp : Application(), IApp, Configuration.Provider {

    /**
     * Worker自定义配置
     */
    override fun getWorkManagerConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .build()
        } else {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.ERROR)
                .build()
        }
    }

    private lateinit var application: Application
    private lateinit var appComponent: IAppComponent

    /** activity生命周期监听 */
    private val activityLifecycleList by lazy { ArrayList<ActivityLifecycleCallbacks>() }
    private lateinit var activityLifecycle: ActivityLifecycle

    /**
     * app初始化
     */
    abstract fun appInit(): AbstractAppInit

    /**
     * 配置模块
     */
    abstract fun configModule(): ConfigModule

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        base?.let { configModule().injectActivityLifecycle(base, activityLifecycleList) }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        if (SysUtil.isMainProcess(application)) {
            // 优先异步初始化MmkvHelper
            MmkvHelper.initialize(application)

            appComponent = AppComponentImpl.build {
                app = application
                globalConfig = globalConfig(application)
            }

            appComponent.extras().put(ConfigModule::javaClass.name, configModule())

            appInit().mainInit(this)

            // 注册activity生命周期监听
            activityLifecycle = ActivityLifecycle(appComponent)
            registerActivityLifecycleCallbacks(activityLifecycle)
            activityLifecycleList.forEach {
                registerActivityLifecycleCallbacks(it)
            }

            appInit().threadInit(this)
        }

        appInit().allInit(this)
    }

    private fun globalConfig(context: Context): GlobalConfig {
        return GlobalConfig.build {
            configModule().applyOptions(context, this)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(activityLifecycle)
        activityLifecycleList.forEach {
            unregisterActivityLifecycleCallbacks(it)
        }
    }

    override fun appComponent(): IAppComponent = appComponent
}