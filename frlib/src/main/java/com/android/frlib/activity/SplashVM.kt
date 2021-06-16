package com.android.frlib.activity

import android.app.Application
import android.content.Context
import android.os.Environment
import com.frlib.basic.download.DownloadImageService
import com.frlib.basic.download.IDownloadCallback
import com.frlib.basic.vm.BaseViewModel
import com.frlib.utils.SysUtil
import java.io.File

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 16/06/2021 10:51
 * @desc
 */
class SplashVM(
    application: Application
) : BaseViewModel(application) {

    fun downloadImage() {
        val url = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3378665999,2087198621&fm=26&gp=0.jpg"

        val imageDirPath = imagesPath(appComponent.application())
        val imageDir = File(imageDirPath)
        if (!imageDir.exists()) {
            imageDir.mkdirs()
        }

        DownloadImageService.build {
            app = appComponent
            imagesDir = imagesPath(appComponent.application())
        }.downloadImage(url, object :
            IDownloadCallback{
            override fun downloadStart() {

            }

            override fun downloadProgress(progress: Int) {

            }

            override fun downloadCompleted(file: File) {

            }

            override fun downloadError(e: Throwable) {

            }
        })
    }

    fun imagesPath(context: Context): String {
        return if (SysUtil.isAndroid6()) {
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath + File.separator + "images"
        } else {
            Environment.getExternalStorageDirectory().absolutePath + File.separator + context.packageName + File.separator + "images"
        }
    }
}