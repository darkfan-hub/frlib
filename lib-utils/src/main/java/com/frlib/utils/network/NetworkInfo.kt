package com.frlib.utils.network

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 14/06/2021 23:44
 * @desc
 */

enum class NetworkType(private val code: Int, private val codeName: String) {

    /** 网络类型 以太网 */
    NET_ETHERNET(0, "ETHERNET"),

    /** 网络类型 WiFi */
    NET_WIFI(1, "WIFI"),

    /** 网络类型 5g */
    NET_5G(2, "5G"),

    /** 网络类型 4g */
    NET_4G(3, "4G"),

    /** 网络类型 3g */
    NET_3G(4, "3G"),

    /** 网络类型 2g */
    NET_2G(5, "2G"),

    /** 网络类型 未知 */
    NET_UNKNOWN(6, "UNKNOWN"),

    /** 网络类型 无连接 */
    NET_DISABLED(7, "DISABLED"),;

    fun getValue(): String {
        return codeName
    }
}