package com.frlib.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 01/06/2021 16:57
 * @desc 图片相关工具类
 */
object ImageUtil {

    /**
     * bitmap转byteArr
     *
     * @param bitmap bitmap对象
     * @param format 格式
     * @return 字节数组
     */
    @JvmStatic
    fun bitmap2Bytes(bitmap: Bitmap, format: Bitmap.CompressFormat): ByteArray {
        val output = ByteArrayOutputStream()
        bitmap.compress(format, 85, output)
        val result = output.toByteArray()
        CloseUtil.closeIO(output)
        return result
    }

    /**
     * bitmap转byteArr
     *
     * @param bitmap bitmap对象
     * @param format 格式
     * @param maxSize 最大的大小, 单位byte
     * @return 字节数组
     */
    @JvmStatic
    fun bitmap2Bytes(bitmap: Bitmap, format: Bitmap.CompressFormat, maxSize: Int): ByteArray {
        var quality = 100
        val output = ByteArrayOutputStream()

        do {
            output.reset()
            bitmap.compress(format, quality, output)
            quality -= 5
        } while (output.toByteArray().size > maxSize)

        val result = output.toByteArray()
        CloseUtil.closeIO(output)
        return result
    }

    /**
     * byteArr转bitmap
     *
     * @param bytes 字节数组
     * @return bitmap
     */
    @JvmStatic
    fun bytes2Bitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    /**
     * drawable转bitmap
     *
     * @param drawable drawable对象
     * @return bitmap
     */
    @JvmStatic
    fun drawable2Bitmap(drawable: Drawable): Bitmap {
        return (drawable as BitmapDrawable).bitmap
    }

    /**
     * bitmap转drawable
     *
     * @param res    resources对象
     * @param bitmap bitmap对象
     * @return drawable
     */
    @JvmStatic
    fun bitmap2Drawable(res: Resources, bitmap: Bitmap): Drawable {
        return BitmapDrawable(res, bitmap)
    }

    /**
     * drawable转byteArr
     *
     * @param drawable drawable对象
     * @param format   格式
     * @return 字节数组
     */
    fun drawable2Bytes(drawable: Drawable, format: Bitmap.CompressFormat): ByteArray {
        return bitmap2Bytes(drawable2Bitmap(drawable), format)
    }

    /**
     * byteArr转drawable
     *
     * @param res   resources对象
     * @param bytes 字节数组
     * @return drawable
     */
    fun bytes2Drawable(res: Resources, bytes: ByteArray): Drawable {
        return bitmap2Drawable(res, bytes2Bitmap(bytes))
    }

    /**
     * file转bitmap
     *
     * @param file file文件
     * @return bitmap
     */
    fun file2Bitmap(file: File, w: Int, h: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeFile(file.absolutePath, options)
        options.inJustDecodeBounds = false
        options.inPreferredConfig = Bitmap.Config.RGB_565

        val realW = options.outWidth
        val realH = options.outHeight

        var bi = 1
        if (realW > realH && realW > w) {
            bi = realW / w
        } else if (realW < realH && realH > h) {
            bi = realH / h
        }

        if (bi < 0) {
            bi = 1
        }

        return try {
            val desW = realW / bi
            val desH = realH / bi
            bitmap = BitmapFactory.decodeFile(file.absolutePath, options)
            Bitmap.createScaledBitmap(bitmap, desW, desH, true)
        } catch (e: FileNotFoundException) {
            bitmap
        }
    }

    /**
     * view转bitmap
     *
     * @param view view对象
     * @param width view宽
     * @param height view高
     * @return drawable
     */
    fun view2Bitmap(view: View, x: Int, y: Int, width: Int, height: Int): Bitmap {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
        )
        view.layout(x, y, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }

    /**
     * view转bitmap
     *
     * @param view view对象
     * @return drawable
     */
    fun view2Bitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return bitmap
    }
}