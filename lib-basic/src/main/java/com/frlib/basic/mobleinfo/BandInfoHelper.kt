package com.frlib.basic.mobleinfo

import android.os.Build

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 24/02/2021 20:43
 * @desc 手机Band基本信息
 */
object BandInfoHelper {

    // private val SYSTEM_PROPERTIES = "android.os.SystemProperties"

    /**
     * 设备名
     */
    fun deviceName(): String {
        //  放弃获取别名了
        /*return try {
            val systemPropertiesClass = Class.forName(SYSTEM_PROPERTIES)
            val systemProperties = systemPropertiesClass.newInstance()

            val deviceNameMethod = systemPropertiesClass.getDeclaredMethod("get", String::class.java)
            val deviceName = deviceNameMethod.invoke(systemProperties, "persist.sys.device_name") as String
            return if (deviceName.isBlank()) Build.MODEL else deviceName
        } catch (e: Exception) {
            Build.MODEL
        }*/

        return Build.MODEL
    }

    /**
     * Android 系统版本
     */
    fun androidRelease(): String {
        return Build.VERSION.RELEASE
    }
}