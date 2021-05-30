package com.frlib.basic.app

import android.app.Application
import android.view.View
import androidx.collection.LruCache
import com.frlib.basic.net.IRepositoryManager
import com.google.gson.Gson
import com.scwang.smart.refresh.layout.api.RefreshHeader
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.ExecutorService


/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 24/05/2021 17:57
 * @desc app 相关组件
 */
interface IAppComponent {

    /**
     * 是否是debug模式
     */
    fun enableDebug(): Boolean

    /**
     * application 实例
     */
    fun application(): Application

    /**
     * app 缓存文件目录
     */
    fun cacheDir(): String

    /**
     * app 文件目录
     */
    fun fileDir(): String

    /**
     * 下拉刷新头
     */
    fun refreshHeader(): RefreshHeader

    /**
     * 缺省页-空数据状态
     */
    fun emptyDataView(): View

    /**
     * 缺省页-错误状态
     */
    fun emptyErrorView(): View

    /**
     * 缺省页-网络错误状态
     */
    fun emptyNetErrorView(): View

    /**
     * Json 序列化库
     *
     * @return [Gson]
     */
    fun gson(): Gson

    /**
     * 网络请求框架
     *
     * @return [OkHttpClient]
     */
    fun okHttpClient(): OkHttpClient

    /**
     * 网络请求框架
     *
     * @return [Retrofit]
     */
    fun retrofitClient(): Retrofit

    /**
     * 用于管理网络请求层, 以及数据缓存层
     *
     * @return [IRepositoryManager]
     */
    fun repositoryManager(): IRepositoryManager

    /**
     * 用来存取一些整个 App 公用的数据, 切勿大量存放大容量数据, 这里的存放的数据和 [Application] 的生命周期一致
     *
     * @return [LruCache]
     */
    fun extras(): LruCache<String, Any>

    /**
     * 返回一个全局公用的线程池,适用于大多数异步需求。
     * 避免多个线程池创建带来的资源消耗。
     *
     * @return [ExecutorService]
     */
    fun executorService(): ExecutorService
}