package com.frlib.basic.config

import com.frlib.basic.net.GlobalHttpHandler
import okhttp3.HttpUrl
import okhttp3.Interceptor

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:25
 * @desc app全局配置
 */
class GlobalConfig(
    val enableDebug: Boolean,
    val cacheRootDir: String,
    val fileRootDir: String,
    val refreshConfig: IRefreshConfig?,
    val emptyPagesConfig: IEmptyPagesConfig?,
    val gsonConfig: IGsonConfig?,
    val okHttpConfig: IOkHttpConfig?,
    val retrofitConfig: IRetrofitConfig?,
    val hostUrl: HttpUrl?,
    val interceptorList: List<Interceptor>,
    val httpHandler: GlobalHttpHandler
) {

    companion object {

        @JvmStatic
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    constructor(builder: Builder) : this(
        builder.enableDebug,
        builder.cacheRootDir,
        builder.fileRootDir,
        builder.refreshConfig,
        builder.emptyPagesConfig,
        builder.gsonConfig,
        builder.okHttpConfig,
        builder.retrofitConfig,
        builder.hostUrl,
        builder.interceptorList,
        builder.httpHandler,
    )

    class Builder {

        /** 开启debug模式 */
        var enableDebug: Boolean = false

        /** 缓存文件目录 */
        var cacheRootDir: String = ""

        /** 文件目录 */
        var fileRootDir: String = ""

        /** 下拉刷新配置 */
        var refreshConfig: IRefreshConfig? = null

        /** 缺省页配置 */
        var emptyPagesConfig: IEmptyPagesConfig? = null

        /** gson配置 */
        var gsonConfig: IGsonConfig? = null

        /** okhttp配置 */
        var okHttpConfig: IOkHttpConfig? = null

        /** retrofit配置 */
        var retrofitConfig: IRetrofitConfig? = null

        /** api host 地址 */
        var hostUrl: HttpUrl? = null

        /** okhttp拦截器集合 */
        var interceptorList: List<Interceptor> = arrayListOf()

        /** http 接口全局处理 */
        var httpHandler: GlobalHttpHandler = GlobalHttpHandler.defaultHandler

        fun build() = GlobalConfig(this)
    }
}