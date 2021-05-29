package com.frlib.basic.data.entity

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 28/05/2021 16:32
 * @desc api bean 基类
 */
data class BasicApiEntity<T>(
    val status: Int,
    val code: String,
    val message: String,
    val data: T
)
