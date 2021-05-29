package com.frlib.basic.config

import android.content.Context
import retrofit2.Retrofit

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 11:43
 * @desc Retrofit配置接口
 */
interface IRetrofitConfig {
    fun retrofitConfig(context: Context, builder: Retrofit.Builder)
}