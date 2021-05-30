package com.frlib.basic.config

import android.content.Context
import com.scwang.smart.refresh.layout.api.RefreshHeader

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/05/2021 16:35
 * @desc 下拉刷新配置
 */
interface IRefreshConfig {
    /**
     * 刷新头部
     */
    fun refreshHeader(context: Context): RefreshHeader
}