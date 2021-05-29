package com.frlib.basic.cache

import android.content.Context

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 12/02/2021 16:38
 * @desc 为不同的模块构建不同的缓存策略
 */
interface ICacheType {

    /**
     * 返回框架内需要缓存的模块对应的 id
     */
    fun cacheTypeId(): Int

    /**
     * 计算对应模块需要的缓存大小
     *
     * @param context 上下文对象
     */
    fun calculateCacheSize(context: Context): Int
}