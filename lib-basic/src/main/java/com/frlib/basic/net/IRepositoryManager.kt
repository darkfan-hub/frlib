package com.frlib.basic.net

import androidx.annotation.NonNull

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 13/02/2021 10:07
 * @desc 接口服务管理接口
 */
interface IRepositoryManager {

    @NonNull
    fun <T> obtainRetrofitService(@NonNull service: Class<T>): T
}