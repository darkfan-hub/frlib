package com.frlib.basic.mobleinfo.entity

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/02/2021 15:01
 * @desc 网络信息
 */
data class NetworkInfoEntity(
    var networkType: String = "unknown",
    var simOperator: String = "unknown",
    var isWifi: Boolean = false,
    var imei: String = "unknown",
    var imsi: String = "unknown"
)