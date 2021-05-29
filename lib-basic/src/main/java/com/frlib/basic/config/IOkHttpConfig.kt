package com.frlib.basic.config

import android.content.Context
import okhttp3.OkHttpClient

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 11:44
 * @desc
 */
interface IOkHttpConfig {
    fun okHttpConfig(context: Context, builder: OkHttpClient.Builder)
}