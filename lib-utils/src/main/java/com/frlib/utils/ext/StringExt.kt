package com.frlib.utils.ext

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 08/04/2021 19:06
 * @desc 字符串扩展
 */

/**
 * 判断字符串是否为null或全为空格
 */
fun String?.isBlack(): Boolean {
    return (null == this || trimString().length() == 0)
}

/**
 * 判断字符串是否有效, 如果为空默认返回长度为0的字符串
 */
fun String?.invalid(invalid: String = ""): String {
    return if (isBlack()) {
        invalid
    } else {
        trimString()
    }
}

/**
 * 字符串去掉空格, 如果字符串为空返回长度为0的字符串
 */
fun String?.trimString(): String {
    return this?.trim() ?: ""
}

/**
 * 字符串长度, 如果字符串为空返回0
 */
fun String?.length(): Int {
    return this?.length ?: 0
}