package com.frlib.basic.config

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:23
 * @desc 一些常量
 */
object AppConstants {

    /** app host 相关 */
    const val KEY_HOST_TAG = "key_host_tag"
    const val KEY_HOST_URL = "key_host_url"

    /** app token */
    const val KEY_TOKEN = "token"

    /** app 登录状态 */
    const val KEY_LOGGED_IN = "logged_in"

    /** app 安装版本号 */
    const val KEY_INSTALL_CODE = "app_install_code"
    
    /** app 协议点击 */
    const val KEY_PROTOCOL_CLICK = "protocol_click"

    /** 缓存key APP信息 */
    const val EXTRAS_KEY_APP_INFO = "app_info"

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
