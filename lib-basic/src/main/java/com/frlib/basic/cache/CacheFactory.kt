package com.frlib.basic.cache

import android.app.ActivityManager
import android.content.Context
import androidx.collection.LruCache

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 12/02/2021 16:48
 * @desc
 */
object CacheFactory {

    private const val cache_type_extras = 0
    private const val cache_type_activity = 1
    private const val cache_type_fragment = 2
    private const val cache_type_retrofit_service = 3

    val extras = object : ICacheType {
        private val MAX_SIZE = 500
        private val MAX_SIZE_MULTIPLIER = 0.005f

        override fun cacheTypeId(): Int = cache_type_extras

        override fun calculateCacheSize(context: Context): Int {
            return calculateCacheSize(context, MAX_SIZE_MULTIPLIER, MAX_SIZE)
        }
    }

    val activity = object : ICacheType {
        private val MAX_SIZE = 80
        private val MAX_SIZE_MULTIPLIER = 0.0008f

        override fun cacheTypeId(): Int = cache_type_activity

        override fun calculateCacheSize(context: Context): Int {
            return calculateCacheSize(context, MAX_SIZE_MULTIPLIER, MAX_SIZE)
        }
    }

    val fragment = object : ICacheType {
        private val MAX_SIZE = 80
        private val MAX_SIZE_MULTIPLIER = 0.0008f

        override fun cacheTypeId(): Int = cache_type_fragment

        override fun calculateCacheSize(context: Context): Int {
            return calculateCacheSize(context, MAX_SIZE_MULTIPLIER, MAX_SIZE)
        }
    }

    val retrofitService = object : ICacheType {
        private val MAX_SIZE = 150
        private val MAX_SIZE_MULTIPLIER = 0.0002f

        override fun cacheTypeId(): Int = cache_type_retrofit_service

        override fun calculateCacheSize(context: Context): Int {
            return calculateCacheSize(context, MAX_SIZE_MULTIPLIER, MAX_SIZE)
        }
    }

    private fun calculateCacheSize(context: Context, multiplier: Float, maxSize: Int): Int {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val targetMemoryCacheSize = activityManager.memoryClass * multiplier * 1024
        return if (targetMemoryCacheSize >= maxSize) maxSize else targetMemoryCacheSize.toInt()
    }

    @JvmStatic
    fun creator(context: Context, cacheType: ICacheType): LruCache<String, Any> {
        return LruCache(cacheType.calculateCacheSize(context))
    }
}