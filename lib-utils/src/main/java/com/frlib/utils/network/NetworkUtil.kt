package com.frlib.utils.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager.*


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
        val info: NetworkInfo? = getActiveNetworkInfo(context)
        return if (info != null && info.isAvailable) {
            if (info.type == ConnectivityManager.TYPE_WIFI) {
                "WIFI"
            } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
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
            } else {
                "unknown"
            }
        } else "unknown"
    }

    /**
     * 获取活动网络信息
     *
     * 需添加权限 `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>`
     *
     * @return NetworkInfo
     */
    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }
}