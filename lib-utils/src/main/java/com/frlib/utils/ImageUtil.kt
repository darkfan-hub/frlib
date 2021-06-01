package com.frlib.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import java.io.ByteArrayOutputStream

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
        bitmap.compress(format, 100, output)
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
     * view转bitmap
     *
     * @param view view对象
     * @param width view宽
     * @param height view高
     * @return drawable
     */
    fun view2Bitmap(view: View, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, width, height)
        view.draw(canvas)
        return bitmap
    }
}