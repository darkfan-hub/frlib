package com.android.frlib.app

import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.frlib.basic.config.ConfigModule
import com.frlib.basic.config.GlobalConfig

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 17:22
 * @desc frlib 配置
 */
class FrLibConfigModule : ConfigModule {
    override fun applyOptions(context: Context, builder: GlobalConfig.Builder) {
        builder.enableDebug = true
    }

    override fun injectActivityLifecycle(
        context: Context,
        lifecycleList: ArrayList<Application.ActivityLifecycleCallbacks>
    ) {

    }

    override fun injectFragmentLifecycle(
        context: Context,
        lifecycleList: ArrayList<FragmentManager.FragmentLifecycleCallbacks>
    ) {

    }
}