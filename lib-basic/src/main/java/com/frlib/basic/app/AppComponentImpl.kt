package com.frlib.basic.app

import android.app.Application
import android.view.View
import androidx.collection.LruCache
import com.frlib.basic.R
import com.frlib.basic.cache.CacheFactory
import com.frlib.basic.config.GlobalConfig
import com.frlib.basic.net.HttpClient
import com.frlib.basic.net.IRepositoryManager
import com.frlib.basic.net.RepositoryManagerImpl
import com.frlib.basic.net.RequestInterceptor
import com.frlib.basic.views.EmptyView
import com.frlib.utils.ext.string
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshHeader
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 14:12
 * @desc
 */
class AppComponentImpl(
    private val application: Application,
    private val globalConfig: GlobalConfig
) : IAppComponent {

    companion object {

        @JvmStatic
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    private constructor(builder: Builder) : this(builder.app, builder.globalConfig)

    override fun enableDebug(): Boolean = globalConfig.enableDebug

    override fun application(): Application = application

    override fun cacheDir(): String = globalConfig.cacheRootDir

    override fun fileDir(): String = globalConfig.fileRootDir

    override fun refreshHeader(): RefreshHeader {
        return if (globalConfig.refreshConfig == null) {
            val timeFormat =
                SimpleDateFormat(application.getString(R.string.frlib_text_header_update), Locale.getDefault())
            ClassicsHeader(application).setTimeFormat(timeFormat)
        } else {
            globalConfig.refreshConfig.refreshHeader(application)
        }
    }

    override fun emptyDataView(): View {
        return if (globalConfig.emptyPagesConfig == null) {
            EmptyView.build {
                context = application
                icon = R.drawable.frlib_icon_empty
                text = application.string(R.string.frlib_text_empty_text)
            }
        } else {
            globalConfig.emptyPagesConfig.emptyDataView(application)
        }
    }

    override fun emptyErrorView(): View {
        return if (globalConfig.emptyPagesConfig == null) {
            EmptyView.build {
                context = application
                icon = R.drawable.frlib_icon_waring
                text = application.string(R.string.frlib_text_empty_error)
            }
        } else {
            globalConfig.emptyPagesConfig.errorView(application)
        }
    }

    override fun emptyNetErrorView(): View {
        return if (globalConfig.emptyPagesConfig == null) {
            EmptyView.build {
                context = application
                icon = R.drawable.frlib_icon_net_error
                text = application.string(R.string.frlib_text_empty_net_error)
            }
        } else {
            globalConfig.emptyPagesConfig.netErrorView(application)
        }
    }

    private var gson: Gson? = null
    override fun gson(): Gson {
        if (gson == null) {
            synchronized(AppComponentImpl) {
                if (gson == null) {
                    val builder = GsonBuilder()
                        // 支持序列化null的参数
                        .serializeNulls()
                        // 支持将序列化key为object的map,默认只能序列化key为string的map
                        .enableComplexMapKeySerialization()
                    globalConfig.gsonConfig?.gsonConfig(application, builder)
                    gson = builder.create()
                }
            }
        }

        return gson!!
    }

    private var okHttpClient: OkHttpClient? = null
    override fun okHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            synchronized(AppComponentImpl) {
                if (okHttpClient == null) {
                    val builder = HttpClient.okHttpBuilder()
                    builder.addInterceptor { chain ->
                        chain.proceed(
                            globalConfig.httpHandler.onHttpRequestBefore(chain, chain.request())
                        )
                    }

                    globalConfig.okHttpConfig?.okHttpConfig(application, builder)
                    globalConfig.interceptorList.forEach {
                        builder.addInterceptor(it)
                    }
                    builder.addInterceptor(RequestInterceptor(globalConfig.httpHandler))
                    okHttpClient = builder.build()
                }
            }
        }

        return okHttpClient!!
    }

    private var retrofit: Retrofit? = null
    override fun retrofitClient(): Retrofit {
        if (retrofit == null) {
            synchronized(AppComponentImpl) {
                if (retrofit == null) {
                    val builder = HttpClient.retrofitBuilder(gson())
                    globalConfig.hostUrl?.let { builder.baseUrl(it) }
                    globalConfig.retrofitConfig?.retrofitConfig(application, builder)
                    builder.client(okHttpClient())
                    retrofit = builder.build()
                }
            }
        }

        return retrofit!!
    }

    private var repositoryManager: IRepositoryManager? = null
    override fun repositoryManager(): IRepositoryManager {
        if (repositoryManager == null) {
            synchronized(AppComponentImpl) {
                if (repositoryManager == null) {
                    repositoryManager = RepositoryManagerImpl(application, retrofitClient())
                }
            }
        }

        return repositoryManager!!
    }

    private var extras: LruCache<String, Any>? = null
    override fun extras(): LruCache<String, Any> {
        if (extras == null) {
            synchronized(AppComponentImpl) {
                if (extras == null) {
                    extras = CacheFactory.creator(application, CacheFactory.extras)
                }
            }
        }

        return extras!!
    }

    override fun executorService(): ExecutorService {
        TODO("Not yet implemented")
    }

    class Builder {
        lateinit var app: Application
        lateinit var globalConfig: GlobalConfig
        fun build() = AppComponentImpl(this)
    }
}