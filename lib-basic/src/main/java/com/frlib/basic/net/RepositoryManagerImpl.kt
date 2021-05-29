package com.frlib.basic.net

import android.app.Application
import androidx.collection.LruCache
import com.frlib.basic.cache.CacheFactory
import retrofit2.Retrofit

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 13/02/2021 10:08
 * @desc 接口服务管理接口实现类
 */
class RepositoryManagerImpl(
    private val application: Application,
    private val retrofit: Retrofit
) : IRepositoryManager {

    private val retrofitServiceCache: LruCache<String, Any> by lazy {
        CacheFactory.creator(application, CacheFactory.retrofitService)
    }

    override fun <T> obtainRetrofitService(service: Class<T>): T {
        var retrofitService: T
        retrofitService = retrofitServiceCache.get(service.name) as T
        if (retrofitService == null) {
            retrofitService = retrofit.create(service)
            retrofitServiceCache.put(service.name, retrofitService!!)
        }

        return retrofitService
    }
}