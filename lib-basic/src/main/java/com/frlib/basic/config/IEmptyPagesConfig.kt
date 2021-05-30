package com.frlib.basic.config

import android.content.Context
import android.view.View

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/05/2021 16:35
 * @desc 缺省页新配置
 */
interface IEmptyPagesConfig {
    /**
     * 缺省页-空数据
     */
    fun emptyDataView(context: Context): View

    /**
     * 缺省页-错误数据
     */
    fun errorView(context: Context): View

    /**
     * 缺省页-网络错误数据
     */
    fun netErrorView(context: Context): View
}