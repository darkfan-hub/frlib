package com.frlib.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.frlib.utils.ext.mkdirs
import java.io.File
import java.io.FileOutputStream


/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 01/06/2021 19:36
 * @desc 媒体相关工具类
 */
object MediaUtil {

    fun saveMediaImage(context: Context, dir: String, bitmap: Bitmap) {
        val displayName = "${TimeUtil.getNowTimeMills()}"
        if (SysUtil.isAndroid10()) {
            saveMediaImageAndroidQ(context, dir, displayName, bitmap)
        } else {
            saveMediaImageOther(context, dir, displayName, bitmap)
        }
    }

    private fun saveMediaImageAndroidQ(context: Context, dir: String, displayName: String, bitmap: Bitmap) {
        val path = "${Environment.DIRECTORY_PICTURES}/${dir}"

        val resolver = context.contentResolver
        val imageCollection =
            MediaStore.Images.Media.getContentUri(
                if (SysUtil.isAndroid10()) {
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                } else {
                    MediaStore.VOLUME_EXTERNAL
                }
            )
        val newImageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            if (SysUtil.isAndroid10()) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
                put(MediaStore.Images.Media.RELATIVE_PATH, path)
            }
        }
        val imageUri = resolver.insert(imageCollection, newImageDetails)
        imageUri?.let { uri ->
            resolver.openOutputStream(uri, "w").use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it!!)
            }

            newImageDetails.clear()
            if (SysUtil.isAndroid10()) {
                newImageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
            }
            resolver.update(uri, newImageDetails, null, null)
        }
    }

    private fun saveMediaImageOther(context: Context, dir: String, displayName: String, bitmap: Bitmap) {
        val path =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath}/${dir}"
        val imageFile = File(path.mkdirs(), "${displayName}.jpg")
        imageFile.createNewFile()
        val ops = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ops)
        ops.flush()
        CloseUtil.closeIO(ops)
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://${imageFile.absolutePath}")
            )
        )
    }
}