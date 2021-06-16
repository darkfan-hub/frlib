package com.frlib.basic.mobleinfo

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import com.frlib.basic.mobleinfo.entity.NetworkInfoEntity
import com.frlib.utils.SysUtil
import com.frlib.utils.network.NetworkUtil

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/02/2021 11:27
 * @desc 网络信息
 */
object NetworkInfoHelper {

    fun registerNetworkCallback(context: Context, callback: ConnectivityManager.NetworkCallback) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (SysUtil.isAndroid7()) {
            connectivityManager.registerDefaultNetworkCallback(callback)
        }
    }

    @SuppressLint("MissingPermission")
    fun networkInfo(context: Context): NetworkInfoEntity {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkInfo = NetworkInfoEntity()
        // 网络类型
        if (SysUtil.isAndroid6()) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                networkInfo.networkType = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "WIFI|VPN"
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WIFI"
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> networkType(context, telephonyManager)
                    else -> "unknown"
                }
                networkInfo.isWifi = hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                networkInfo.networkType = when (type) {
                    ConnectivityManager.TYPE_WIFI -> "WIFI"
                    ConnectivityManager.TYPE_MOBILE -> networkType(context, telephonyManager)
                    else -> "unknown"
                }
            }
        }

        networkInfo.simOperator = NetworkUtil.getSimOperators(telephonyManager.simOperator)

        val subId = getDefaultDataSubId(context)
        networkInfo.imei = reflectSimInfo(telephonyManager, "getSubscriberIdGemini", subId)
        networkInfo.imsi = reflectSimInfo(telephonyManager, "getSubscriberIdGemini", subId)
        return networkInfo
    }

    @SuppressLint("MissingPermission")
    fun networkType(
        context: Context,
        telephonyManager: TelephonyManager
    ): String {
        return if (SysUtil.isAndroid10()) {
            try {
                when (telephonyManager.dataNetworkType) {
                    TelephonyManager.NETWORK_TYPE_GPRS,
                    TelephonyManager.NETWORK_TYPE_EDGE,
                    TelephonyManager.NETWORK_TYPE_CDMA,
                    TelephonyManager.NETWORK_TYPE_1xRTT,
                    TelephonyManager.NETWORK_TYPE_IDEN -> "2G"
                    TelephonyManager.NETWORK_TYPE_UMTS,
                    TelephonyManager.NETWORK_TYPE_EVDO_0,
                    TelephonyManager.NETWORK_TYPE_EVDO_A,
                    TelephonyManager.NETWORK_TYPE_HSDPA,
                    TelephonyManager.NETWORK_TYPE_HSUPA,
                    TelephonyManager.NETWORK_TYPE_HSPA,
                    TelephonyManager.NETWORK_TYPE_EVDO_B,
                    TelephonyManager.NETWORK_TYPE_EHRPD,
                    TelephonyManager.NETWORK_TYPE_HSPAP -> "3G"
                    TelephonyManager.NETWORK_TYPE_LTE -> "4G"
                    TelephonyManager.NETWORK_TYPE_NR -> "5G"
                    else -> "unknown"
                }
            } catch (e: Exception) {
                "unknown"
            }
        } else {
            NetworkUtil.networkTypeString(context)
        }
    }

    /**
     * 获取流量卡sub id
     */
    private fun getDefaultDataSubId(context: Context): Int {
        var subId = -1;

        if (SysUtil.isAndroidLollipopMr1()) {
            val subscriptions = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

            subId = try {
                subscriptionsSubId(subscriptions, "getDefaultDataSubId")
            } catch (e: Exception) {
                try {
                    subscriptionsSubId(subscriptions, "getDefaultDataSubscrptionId")
                } catch (e1: Exception) {
                    try {
                        subscriptionsSubId(subscriptions, "getDefaultDataPhoneId")
                    } catch (e2: Exception) {
                        -1
                    }
                }
            }
        }

        return subId
    }

    /**
     * 根据方法名反射获取流量卡sub id
     */
    private fun subscriptionsSubId(subscriptions: SubscriptionManager, methodName: String): Int {
        val defaultDataSubId = subscriptions.javaClass.getMethod(methodName)
        return defaultDataSubId.invoke(subscriptions) as Int
    }

    /**
     * 根据方法名反射获取手机卡信息
     */
    private fun reflectSimInfo(telephony: TelephonyManager, methodName: String, slotID: Int): String {
        return try {
            val telephonyClass = Class.forName(telephony.javaClass.name)
            val method = telephonyClass.getMethod(methodName, Int::class.java)
            val info = method.invoke(telephony, slotID)
            info?.toString() ?: "unknown"
        } catch (e: Exception) {
            "unknown"
        }
    }
}