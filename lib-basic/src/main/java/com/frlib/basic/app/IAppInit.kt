package com.frlib.basic.app

import android.app.Application

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 10:54
 * @desc app 初始化相关接口
 */
interface IAppInit {

    /**
     * 所有进程初始化
     * 一般用于三方推送等需要所有进程都初始化
     */
    fun allInit(app: Application)

    /**
     * 主进程初始化
     */
    fun mainInit(app: IApp)

    /**
     * 多线程初始化
     */
    fun threadInit(app: IApp)
}