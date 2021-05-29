package com.frlib.basic.data.entity

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 14/02/2021 11:20
 * @desc api 分页接口实体类基类
 */
open class BasicApiPageEntity<T>(
    val countTotal: Boolean,
    val firstPage: Boolean,
    val lastPage: Boolean,
    val nextPageNo: Int,
    val offset: Int,
    val pageNo: Int,
    val pageRow: Int,
    val pageUrl: String,
    val prePage: Int,
    val slider: List<Int>,
    val total: Int,
    val totalPageNo: Int,
    val objects: List<T>
)