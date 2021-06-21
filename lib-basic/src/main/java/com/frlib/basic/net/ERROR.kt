package com.frlib.basic.net

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @since 26/11/2020 17:13
 */
enum class ERROR(private val code: Int, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, "未知错误, 请重试"),

    /**
     * 解析错误
     */
    PARSE_ERROR(1001, "解析错误, 请重试"),

    /**
     * 网络错误
     */
    NETWORD_ERROR(1002, "网络错误, 请重试"),

    /**
     * 协议出错
     */
    HTTP_ERROR(1003, "请求接口错误, 请重试"),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, "证书出错, 请重试"),

    /**
     * 请求接口出错
     */
    HTTP_API_ERROR(1005, "请求接口出错, 请重试"),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, "连接超时, 请重试"),

    /**
     * token 过期
     */
    TOKEN_ERROR(1007, "登录已失效, 请重新登录"),

    /**
     * 404
     */
    HTTP_NOT_FOUND(1008, "请求接口找不到了, 请重试");

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }
}