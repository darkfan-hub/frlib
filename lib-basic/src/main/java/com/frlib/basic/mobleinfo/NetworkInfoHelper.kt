package com.frlib.basic.mobleinfo

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import com.frlib.basic.mobleinfo.entity.NetworkInfoEntity
import com.frlib.utils.SysUtil
import com.frlib.utils.network.NetworkType
import com.frlib.utils.network.NetworkUtil

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/02/2021 11:27
 * @desc 网络信息
 */
object NetworkInfoHelper {

    @SuppressLint("MissingPermission")
    fun networkInfo(context: Context): NetworkInfoEntity {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkInfo = NetworkInfoEntity()
        val networkType = NetworkUtil.networkType(context)
        networkInfo.networkType = networkType.getValue()
        networkInfo.isWifi = networkType == NetworkType.NET_WIFI
        networkInfo.simOperator = NetworkUtil.getSimOperators(telephonyManager.simOperator)
        val subId = getDefaultDataSubId(context)
        networkInfo.imei = reflectSimInfo(telephonyManager, "getSubscriberIdGemini", subId)
        networkInfo.imsi = reflectSimInfo(telephonyManager, "getSubscriberIdGemini", subId)
        return networkInfo
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