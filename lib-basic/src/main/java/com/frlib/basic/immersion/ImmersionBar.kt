package com.frlib.basic.immersion

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/02/2021 10:26
 * @desc 沉浸式状态栏
 */
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class ImmersionBar(
    /** 状态栏颜色, 默认透明色 */
    val statusBarColor: String = "#00000000",
    /** 状态栏字体是否需要显示黑色 */
    val statusBarDarkFont: Boolean = false,
    /** paddingTop与status bar */
    val fitsSystemWindows: Boolean = false,
    /** 全屏 */
    val fullScreen: Boolean = false,
)