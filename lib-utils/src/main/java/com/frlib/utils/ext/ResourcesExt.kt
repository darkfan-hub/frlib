package com.frlib.utils.ext

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.frlib.utils.ConvertUtil
import com.frlib.utils.SysUtil

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @since 08/12/2020 14:16
 * @desc 资源扩展
 */

/**
 * 根据颜色资源id获取颜色
 */
fun Context.color(@ColorRes colorResId: Int): Int {
    return ContextCompat.getColor(this, colorResId)
}

/**
 * 根据字符串资源id获取字符串
 */
fun Context.string(@StringRes stringId: Int): String {
    return this.getString(stringId)
}

/**
 * 根据drawable资源id获取drawable
 */
fun Context.drawable(@DrawableRes drawableResId: Int): Drawable? {
    return AppCompatResources.getDrawable(this, drawableResId)
}

fun Context.attrValue(attrId: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.resourceId
}

fun View.backgroundExt(drawable: Drawable) {
    if (SysUtil.isAndroidJelly()) {
        this.background = drawable
    } else {
        this.setBackgroundDrawable(drawable)
    }
}

fun Drawable.tintIcon(@ColorInt tintColor: Int): Drawable {
    this.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
    return this
}

/**
 * 单位转换, dp转px
 */
fun Context.dp2px(dp: Float): Int {
    return ConvertUtil.dp2px(this, dp)
}
