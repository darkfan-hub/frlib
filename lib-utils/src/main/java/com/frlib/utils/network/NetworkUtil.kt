package com.frlib.utils.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.*
import androidx.annotation.RequiresApi
import com.frlib.utils.SysUtil


/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 14/06/2021 23:41
 * @desc
 */
object NetworkUtil {

    private const val CM_MOBILE1 = "46000"
    private const val CM_MOBILE2 = "46002"
    private const val CM_MOBILE3 = "46004"
    private const val CM_MOBILE4 = "46007"
    private const val CU_MOBILE1 = "46001"
    private const val CU_MOBILE2 = "46006"
    private const val CU_MOBILE3 = "46009"
    private const val CT_MOBILE1 = "46003"
    private const val CT_MOBILE2 = "46005"
    private const val CT_MOBILE3 = "46011"

    @SuppressLint("MissingPermission")
    fun networkType(context: Context): NetworkType {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (SysUtil.isAndroid7()) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return if (networkCapabilities == null) {
                NetworkType.NET_DISABLED
            } else {
                networkType(context, networkCapabilities)
            }
        } else {
            /*if (isEthernet(context)) {
                return NetworkType.NET_ETHERNET
            }*/
            val netInfo = activeNetworkInfo(connectivityManager)

            return if (netInfo != null) {
                when (netInfo.type) {
                    ConnectivityManager.TYPE_WIFI -> NetworkType.NET_WIFI
                    ConnectivityManager.TYPE_MOBILE -> {
                        networkType(netInfo.subtype, netInfo.subtypeName)
                    }
                    else -> NetworkType.NET_UNKNOWN
                }
            } else {
                NetworkType.NET_DISABLED
            }
        }
    }

    /**
     * 兼容Android 10 Android 11
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingPermission")
    fun networkType(context: Context, networkCapabilities: NetworkCapabilities): NetworkType {
        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return NetworkType.NET_ETHERNET
        }

        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return NetworkType.NET_WIFI
        }

        if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return networkType(tm.dataNetworkType)
        }

        return NetworkType.NET_DISABLED
    }

    fun isEthernet(context: Context): Boolean {
        return isEthernet(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }

    /**
     * 是否是以太网连接
     *
     * Deprecated in API level 23
     */
    @SuppressLint("MissingPermission")
    fun isEthernet(cm: ConnectivityManager): Boolean {
        val netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET) as NetworkInfo?
        return if (netInfo == null) {
            false
        } else {
            val state = netInfo.state
            if (state == null) {
                false
            } else {
                state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING
            }
        }
    }

    /**
     * 获取活动网络信息
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     * Deprecated in API level 29
     * @return NetworkInfo
     */
    @SuppressLint("MissingPermission")
    fun activeNetworkInfo(context: Context): NetworkInfo? {
        return activeNetworkInfo(context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    }

    /**
     * 获取活动网络信息
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     * Deprecated in API level 29
     * @return NetworkInfo
     */
    @SuppressLint("MissingPermission")
    fun activeNetworkInfo(cm: ConnectivityManager): NetworkInfo? {
        return cm.activeNetworkInfo
    }

    private fun networkType(networkType: Int, typeName: String = ""): NetworkType {
        return when (networkType) {
            NETWORK_TYPE_GSM,
            NETWORK_TYPE_GPRS,
            NETWORK_TYPE_CDMA,
            NETWORK_TYPE_EDGE,
            NETWORK_TYPE_1xRTT,
            NETWORK_TYPE_IDEN -> NetworkType.NET_2G
            NETWORK_TYPE_TD_SCDMA,
            NETWORK_TYPE_EVDO_A,
            NETWORK_TYPE_UMTS,
            NETWORK_TYPE_EVDO_0,
            NETWORK_TYPE_HSDPA,
            NETWORK_TYPE_HSUPA,
            NETWORK_TYPE_HSPA,
            NETWORK_TYPE_EVDO_B,
            NETWORK_TYPE_EHRPD,
            NETWORK_TYPE_HSPAP -> NetworkType.NET_3G
            NETWORK_TYPE_IWLAN,
            NETWORK_TYPE_LTE -> NetworkType.NET_4G
            NETWORK_TYPE_NR -> NetworkType.NET_5G
            else -> {
                if (typeName.equals("TD-SCDMA", ignoreCase = true)
                    || typeName.equals("WCDMA", ignoreCase = true)
                    || typeName.equals("CDMA2000", ignoreCase = true)
                ) {
                    NetworkType.NET_3G
                } else {
                    NetworkType.NET_UNKNOWN
                }
            }
        }
    }


    /**
     * 获取网络运营商，CM是移动，CU是联通，CT是电信
     *
     * @param data str
     * @return str
     */
    fun getSimOperators(data: String): String {
        return when (data) {
            CM_MOBILE1, CM_MOBILE2, CM_MOBILE3, CM_MOBILE4 -> "CMCC"
            CU_MOBILE1, CU_MOBILE2, CU_MOBILE3 -> "CUCC"
            CT_MOBILE1, CT_MOBILE2, CT_MOBILE3 -> "CTCC"
            else -> "unknown"
        }
    }

    fun networkTypeString(context: Context): String {
        val info: NetworkInfo? = activeNetworkInfo(context)
        return if (info != null && info.isAvailable) {
            when (info.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    "WIFI"
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    when (info.subtype) {
                        NETWORK_TYPE_GSM,
                        NETWORK_TYPE_GPRS,
                        NETWORK_TYPE_CDMA,
                        NETWORK_TYPE_EDGE,
                        NETWORK_TYPE_1xRTT,
                        NETWORK_TYPE_IDEN -> "2G"
                        NETWORK_TYPE_TD_SCDMA,
                        NETWORK_TYPE_EVDO_A,
                        NETWORK_TYPE_UMTS,
                        NETWORK_TYPE_EVDO_0,
                        NETWORK_TYPE_HSDPA,
                        NETWORK_TYPE_HSUPA,
                        NETWORK_TYPE_HSPA,
                        NETWORK_TYPE_EVDO_B,
                        NETWORK_TYPE_EHRPD,
                        NETWORK_TYPE_HSPAP -> "3G"
                        NETWORK_TYPE_IWLAN,
                        NETWORK_TYPE_LTE -> "4G"
                        NETWORK_TYPE_NR -> "5G"
                        else -> {
                            val subtypeName = info.subtypeName
                            if (subtypeName.equals("TD-SCDMA", ignoreCase = true)
                                || subtypeName.equals("WCDMA", ignoreCase = true)
                                || subtypeName.equals("CDMA2000", ignoreCase = true)
                            ) {
                                "3G"
                            } else {
                                "unknown"
                            }
                        }
                    }
                }
                else -> {
                    "unknown"
                }
            }
        } else "unknown"
    }
}