package com.frlib.basic.download

import com.frlib.basic.app.IAppComponent
import com.frlib.utils.FileUtil
import com.liulishuo.filedownloader.FileDownloader
import timber.log.Timber
import java.io.File

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 16/06/2021 10:23
 * @desc 保存图片到本地
 */
class DownloadImageService(
    private val appComponent: IAppComponent,
    private val imagesDir: String,
) {

    companion object {

        private var instances: DownloadImageService? = null

        @JvmStatic
        fun build(block: Builder.() -> Unit): DownloadImageService {
            Timber.i("build")
            if (instances == null) {
                instances = Builder().apply(block).build()
            }

            return instances!!

        }
    }

    private constructor(builder: Builder) : this(
        builder.app,
        builder.imagesDir,
    )

    fun downloadImage(imageUrl: String, downloadCallback: IDownloadCallback) {
        val imagePath = "${imagesDir}${File.separator}${imageUrl.hashCode()}.${
            FileUtil.getFileExtensionWithUrl(imageUrl)
        }"

        val imageFile = FileUtil.getFileByPath(imagePath)

        if (FileUtil.isFileExists(appComponent.application(), imageFile)) {
            Timber.i("图片已存在")
            downloadCallback.downloadCompleted(imageFile!!)
        } else {
            FileDownloader.setup(appComponent.application())

            FileDownloader.getImpl().create(imageUrl)
                .setListener(DownloadListenerImpl(downloadCallback))
                .setPath(imagePath).start()
        }
    }

    class Builder {
        /** 当前app */
        lateinit var app: IAppComponent

        /** 图片下载的目录 */
        lateinit var imagesDir: String

        fun build() = DownloadImageService(this)
    }
}