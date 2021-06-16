package com.frlib.utils

import android.content.Context
import android.net.Uri
import com.frlib.utils.ext.length
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 16/06/2021 10:36
 * @desc 文件工具类
 */
object FileUtil {

    fun getFileByPath(filePath: String?): File? {
        return if (filePath == null) {
            null
        } else {
            File(filePath)
        }
    }

    fun isFileExists(context: Context, file: File?): Boolean {
        if (file == null) {
            return false
        }

        if (file.exists()) {
            return true
        }

        return isFileExists(context, file.absolutePath)
    }

    fun isFileExists(context: Context, filePath: String): Boolean {
        val file = getFileByPath(filePath)
        if (file == null) {
            return false
        }

        if (file.exists()) {
            return true
        }

        return isFileExistsApi29(context, filePath)
    }

    private fun isFileExistsApi29(context: Context, filePath: String): Boolean {
        return if (SysUtil.isAndroid10()) {
            try {
                val uri = Uri.parse(filePath)
                val afd = context.contentResolver.openAssetFileDescriptor(uri, "r")
                if (afd == null) {
                    false
                }

                try {
                    afd?.close()
                } catch (e: IOException) {
                }
                true
            } catch (e: FileNotFoundException) {
                false
            }
        } else {
            false
        }
    }

    /**
     * 根据url获取url后缀名，没有返回空字符串
     * @return png or other
     */
    fun getFileExtensionWithUrl(url: String): String {
        if (url.length() <= 0) {
            return ""
        }

        val lastPoi = url.lastIndexOf(".")
        if (lastPoi == -1) {
            return ""
        }

        return url.substring(lastPoi + 1)
    }
}