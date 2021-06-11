package com.frlib.utils.ext

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 10/06/2021 16:18
 * @desc kotlin 集合扩展
 */

/**
 * 集合长度
 */
fun List<Any>?.length(): Int {
    return this?.size ?: 0
}
