package com.frlib.basic.net

import com.frlib.basic.config.AppConstants
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 28/12/2020 14:21
 * @desc http client
 */
object HttpClient {

    @JvmStatic
    fun okHttpBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(AppConstants.http_time_out, TimeUnit.SECONDS)
            .writeTimeout(AppConstants.http_time_out, TimeUnit.SECONDS)
            .readTimeout(AppConstants.http_time_out, TimeUnit.SECONDS)
    }

    @JvmStatic
    fun retrofitBuilder(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
    }
}