package com.frlib.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import androidx.core.content.FileProvider
import java.io.File

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/12/2020 17:58
 * @desc 系统相关工具类
 */
object SysUtil {

    /**
     * 是否是 Android 4.1 及以上版本
     */
    @JvmStatic
    fun isAndroidJelly(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }

    /**
     * 是否是 Android 4.4 及以上版本
     */
    @JvmStatic
    fun isAndroid4(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    /**
     * 是否是 Android 5.0 及以上版本
     */
    @JvmStatic
    fun isAndroid5(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    /**
     * 是否是 Android 5.1 及以上版本
     */
    @JvmStatic
    fun isAndroidLollipopMr1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1
    }

    /**
     * 是否是 Android 6.0 及以上版本
     */
    @JvmStatic
    fun isAndroid6(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    /**
     * 是否是 Android 7.0 及以上版本
     */
    @JvmStatic
    fun isAndroid7(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    /**
     * 是否是 Android 8.0 及以上版本
     */
    @JvmStatic
    fun isAndroid8(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    /**
     * 是否是 Android 9.0 及以上版本
     */
    @JvmStatic
    fun isAndroid9(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    /**
     * 是否是 Android 10.0 及以上版本
     */
    @JvmStatic
    fun isAndroid10(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    /**
     * 是否是 Android 11.0 及以上版本
     */
    @JvmStatic
    fun isAndroid11(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    /**
     * 获取cpu可用核心数目
     */
    @JvmStatic
    fun cpuCore(): Int = Runtime.getRuntime().availableProcessors()

    @JvmStatic
    fun defaultThreadPoolSize(): Int {
        return defaultThreadPoolSize(8)
    }

    @JvmStatic
    fun defaultThreadPoolSize(max: Int): Int {
        val availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1
        return if (availableProcessors > max) max else availableProcessors
    }

    /**
     * 判断当前进程是否是主进程
     */
    @JvmStatic
    fun isMainProcess(context: Context): Boolean {
        val packageName = context.packageName
        val processName = getProcessName(context, Process.myPid())
        return packageName == processName
    }

    /**
     * 根据进程 ID 获取进程名.
     */
    @JvmStatic
    fun getProcessName(context: Context, pid: Int): String {
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfoList =
            am.runningAppProcesses ?: return ""
        for (processInfo in processInfoList) {
            if (processInfo.pid == pid) {
                return processInfo.processName
            }
        }
        return ""
    }

    @JvmStatic
    fun currentProcessName(context: Context): String {
        return getProcessName(context, Process.myPid())
    }

    @JvmStatic
    fun callPhone(context: Context, phone: String) {
        if (phone.isBlank()) return
        val intent = Intent(Intent.ACTION_DIAL)
        val data: Uri = Uri.parse("tel:$phone")
        intent.data = data
        context.startActivity(intent)
    }

    @JvmStatic
    fun installApp(context: Context, file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val fileUri: Uri
        if (isAndroid7()) {
            fileUri = FileProvider.getUriForFile(context, context.packageName + ".fileProvider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            fileUri = Uri.fromFile(file)
        }

        intent.setDataAndType(fileUri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}