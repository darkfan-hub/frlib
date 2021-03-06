package com.frlib.basic.download

import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import timber.log.Timber
import java.io.File
import java.lang.ref.WeakReference

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 07/05/2021 17:56
 * @desc 下载进度监听
 */
class DownloadListenerImpl(
    downloadCallback: IDownloadCallback
) : FileDownloadListener() {

    private var downloadCallbackRef: WeakReference<IDownloadCallback> = WeakReference(downloadCallback)

    override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
        Timber.d("下载pending")
    }

    override fun started(task: BaseDownloadTask?) {
        Timber.d("下载started")
        downloadCallbackRef.get()?.downloadStart()
    }

    override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
        val progress = (soFarBytes * 1.0f / totalBytes) * 100
        Timber.d("下载进度 -> $progress")
        downloadCallbackRef.get()?.downloadProgress(progress.toInt())
    }

    override fun completed(task: BaseDownloadTask) {
        Timber.i("下载completed")
        downloadCallbackRef.get()?.downloadCompleted(File(task.path))
    }

    override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
        Timber.w("下载paused")
    }

    override fun error(task: BaseDownloadTask, e: Throwable) {
        Timber.e(e)
        downloadCallbackRef.get()?.downloadError(e)
    }

    override fun warn(task: BaseDownloadTask?) {
        Timber.w("下载warn")
    }
}