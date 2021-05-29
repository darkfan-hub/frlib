package com.frlib.basic.config

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:23
 * @desc 一些常量
 */
object AppConstants {

    /** 双击时间间隔 */
    @JvmField
    var double_click_time = 500

    /** 接口超时时间 */
    @JvmField
    var http_time_out = 30L

    /** 接口成功状态码 */
    @JvmField
    var api_state_success = 1

    /** 接口token失效 */
    @JvmField
    var api_state_token_invalid = 401
}
