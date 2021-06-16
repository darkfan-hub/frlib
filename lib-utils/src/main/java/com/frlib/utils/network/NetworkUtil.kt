package com.frlib.utils.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.frlib.utils.SysUtil

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 14/06/2021 23:41
 * @desc
 */
object NetworkUtil {

    fun networkType(context: Context): NetworkType {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (connectivityManager == null) {
            return NetworkType.NET_UNKNOWN
        }

        return NetworkType.NET_2G
    }

    /**
     * 是否是以太网
     */
    @SuppressLint("MissingPermission")
    fun isEthernet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager != null
    }
}