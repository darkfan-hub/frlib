package com.frlib.basic.config

import android.content.Context
import com.google.gson.GsonBuilder

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 11:43
 * @desc gson配置接口
 */
interface IGsonConfig {
    fun gsonConfig(context: Context, builder: GsonBuilder)
}