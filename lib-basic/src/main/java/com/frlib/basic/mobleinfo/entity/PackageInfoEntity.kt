package com.frlib.basic.mobleinfo.entity

import android.graphics.drawable.Drawable

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/02/2021 10:27
 * @desc app 包信息
 */
data class PackageInfoEntity(
    val appName: String,
    val installTime: Long,
    val updateTime: Long,
    val packageName: String,
    val packageSign: String,
    val appVersionName: String,
    val appVersionCode: Long,
    val targetSdkVersion: Int,
    val minSdkVersion: Int,
    val icon: Drawable
)