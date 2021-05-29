package com.frlib.basic.app

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 24/05/2021 23:00
 * @desc application 接口
 */
interface IApp {

    /**
     * app 组件
     */
    fun appComponent(): IAppComponent
}