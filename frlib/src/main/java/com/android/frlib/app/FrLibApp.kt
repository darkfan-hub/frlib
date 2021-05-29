package com.android.frlib.app

import com.frlib.basic.app.AbstractApp
import com.frlib.basic.app.AbstractAppInit
import com.frlib.basic.config.ConfigModule

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 17:19
 * @desc frlib app.
 */
class FrLibApp : AbstractApp() {

    private val appInit by lazy { FrLibInit() }

    override fun appInit(): AbstractAppInit = appInit

    private val appConfig by lazy { FrLibConfigModule() }

    override fun configModule(): ConfigModule = appConfig
}