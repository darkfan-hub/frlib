package com.frlib.utils

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.Surface
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @since 14/10/2020 18:50
 * @desc UI相关工具类
 */
object UIUtil {

    fun getResolutionRatio(context: Context): String {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var width: Int
        var height: Int

        if (SysUtil.isAndroid11()) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            width = windowMetrics.bounds.width() - insets.left - insets.right
            height = windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics() // 创建了一张白纸
            windowManager.defaultDisplay.getMetrics(displayMetrics) // 给白纸设置宽高
            width = displayMetrics.widthPixels
            height = displayMetrics.heightPixels
        }

        return "$width*$height"
    }

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return 屏幕宽px
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (SysUtil.isAndroid11()) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics() // 创建了一张白纸
            windowManager.defaultDisplay.getMetrics(displayMetrics) // 给白纸设置宽高
            displayMetrics.widthPixels
        }
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高px
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (SysUtil.isAndroid11()) {
            val windowMetrics = windowManager.currentWindowMetrics
            val insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics() // 创建了一张白纸
            windowManager.defaultDisplay.getMetrics(displayMetrics) // 给白纸设置宽高
            displayMetrics.heightPixels
        }
    }

    /**
     * 设置屏幕为横屏
     *
     * 还有一种就是在Activity中加属性android:screenOrientation="landscape"
     *
     * 不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次
     *
     * 设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次
     *
     * 设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法
     *
     * @param activity activity
     */
    @JvmStatic
    fun setLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /**
     * 设置屏幕为竖屏
     *
     * @param activity activity
     */
    @JvmStatic
    fun setPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 判断是否横屏
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isLandscape(context: Context): Boolean {
        return context.resources
            .configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    /**
     * 判断是否竖屏
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmStatic
    fun isPortrait(context: Context): Boolean {
        return context.resources
            .configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    /**
     * 获取屏幕旋转角度
     *
     * @param activity activity
     * @return 屏幕旋转角度
     */
    @JvmStatic
    fun getScreenRotation(activity: Activity): Int {
        return when (activity.display?.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    @JvmStatic
    fun captureWithStatusBar(
        activity: Activity,
        config: Bitmap.Config = Bitmap.Config.RGB_565
    ): Bitmap? {
        val view: View = activity.window.decorView
        val viewBitmap = Bitmap.createBitmap(view.width, view.height, config)
        val canvas = Canvas(viewBitmap)

        val viewBackground = view.background
        if (viewBackground != null) {
            viewBackground.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        return viewBitmap
    }

    @JvmStatic
    fun statusBarHeight(context: Context): Int {
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return context.resources.getDimensionPixelOffset(resId)
    }

    /**
     * 是否在屏幕右侧
     *
     * @param mContext 上下文
     * @param xPos     位置的x坐标值
     * @return true：是。
     */
    @JvmStatic
    fun isInRight(mContext: Context, xPos: Int): Boolean {
        return xPos > getScreenWidth(mContext) / 2
    }

    /**
     * 是否在屏幕左侧
     *
     * @param mContext 上下文
     * @param xPos     位置的x坐标值
     * @return true：是。
     */
    @JvmStatic
    fun isInLeft(mContext: Context, xPos: Int): Boolean {
        return xPos < getScreenWidth(mContext) / 2
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    @JvmStatic
    fun dp2px(context: Context, dpValue: Float): Int {
        return ConvertUtil.dp2px(context, dpValue)
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    @JvmStatic
    fun px2dp(context: Context, pxValue: Float): Int {
        return ConvertUtil.px2dp(context, pxValue)
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    @JvmStatic
    fun sp2px(context: Context, spValue: Float): Int {
        return ConvertUtil.sp2px(context, spValue)
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    @JvmStatic
    fun px2sp(context: Context, pxValue: Float): Int {
        return ConvertUtil.px2sp(context, pxValue)
    }
}