package com.frlib.basic.download

import java.io.File

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 07/05/2021 17:53
 * @desc
 */
interface IDownloadCallback {

    /**
     * 开始下载
     */
    fun downloadStart()

    /**
     * 下载进度
     */
    fun downloadProgress(progress: Int)

    /**
     * 下载完成
     */
    fun downloadCompleted(file: File)

    /**
     * 下载错误
     */
    fun downloadError(e: Throwable)
}