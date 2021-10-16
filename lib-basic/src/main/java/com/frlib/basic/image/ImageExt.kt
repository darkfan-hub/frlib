package com.frlib.basic.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.frlib.basic.R
import com.frlib.basic.image.transformations.RoundedCornersTransformation
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/05/2021 13:35
 * @desc 图片ktx扩展
 */

inline fun ImageView.displayImage(url: String) {
    displayImage(this.context, url, null)
}

inline fun ImageView.displayImage(
    url: String,
    placeholder: Drawable
) {
    displayImage(this.context, url, placeholder)
}

inline fun ImageView.displayImage(
    context: Context,
    url: String,
    placeholder: Drawable?
) {
    val options = RequestOptions()
    if (placeholder == null) {
        options.placeholder(R.drawable.frlib_iamge_placeholder)
    } else {
        options.placeholder(placeholder)
    }
    options.centerCrop()

    FrGlideApp.with(context)
        .load(url)
        .apply(options)
        .into(this)
}

inline fun ImageView.displayRadiusImage(
    url: String,
    radius: Int,
    overrideSize: Int = 0,
) {
    displayRadiusImage(this.context, url, radius, null, overrideSize)
}

inline fun ImageView.displayRadiusImage(
    url: String,
    radius: Int,
    placeholder: Drawable,
    overrideSize: Int = 0,
) {
    displayRadiusImage(this.context, url, radius, placeholder, overrideSize)
}

inline fun ImageView.displayRadiusImage(
    context: Context,
    url: String,
    radius: Int,
    placeholder: Drawable?,
    overrideSize: Int = 0,
) {
    val options = RequestOptions().transform(
        CenterCrop(),
        RoundedCornersTransformation(
            radius,
            0,
            RoundedCornersTransformation.CornerType.ALL
        )
    )

    if (overrideSize > 0) {
        options.override(overrideSize)
    }

    FrGlideApp.with(context)
        .load(url)
        .apply(options)
        .thumbnail(glideRequestBuilder(context, options, placeholder))
        .into(this)
}

inline fun ImageView.displayCircleImage(url: String) {
    displayCircleImage(this.context, url, null)
}

inline fun ImageView.displayCircleImage(
    url: String,
    placeholder: Drawable
) {
    displayCircleImage(this.context, url, placeholder)
}

inline fun ImageView.displayCircleImage(
    context: Context,
    url: String,
    placeholder: Drawable?
) {
    val options = RequestOptions().transform(CenterCrop(), CircleCrop())

    FrGlideApp.with(context)
        .load(url)
        .apply(options)
        .thumbnail(glideRequestBuilder(context, options, placeholder))
        .into(this)
}

fun glideRequestBuilder(
    context: Context,
    options: RequestOptions,
    placeholder: Drawable?
): RequestBuilder<Drawable> {
    return if (placeholder == null) {
        FrGlideApp.with(context).load(R.drawable.frlib_iamge_placeholder).apply(options)
    } else {
        FrGlideApp.with(context).load(placeholder).apply(options)
    }
}

/**
 * 单选图片
 */
inline fun FragmentActivity.singlePictureSelector(
    themeId: Int = R.style.picture_WeChat_style,
    isCompress: Boolean = true,
    enableCrop: Boolean = true,
    callback: OnResultCallbackListener<LocalMedia>? = null
) {
    PictureSelector.create(this)
        // 只显示图片
        .openGallery(PictureMimeType.ofImage())
        .imageEngine(GlideEngine.default())
        .theme(themeId)
        .isWeChatStyle(themeId == R.style.picture_WeChat_style)
        .selectionMode(PictureConfig.SINGLE)
        .isCompress(isCompress)
        .isEnableCrop(enableCrop)
        // 裁剪质量, 默认90
        .cutOutQuality(85)
        .synOrAsy(false)
        // 小于100kb的图片不压缩
        .minimumCompressSize(100)
        .maxSelectNum(1)
        .withAspectRatio(1, 1)
        .forResult(callback)
}

/**
 * 多选图片, 默认选择9张
 */
inline fun FragmentActivity.multiplePictureSelector(
    maxSize: Int = 9,
    themeId: Int = R.style.picture_WeChat_style,
    isCompress: Boolean = true,
    selectionData: List<LocalMedia> = listOf(),
    callback: OnResultCallbackListener<LocalMedia>? = null
) {
    PictureSelector.create(this)
        // 只显示图片
        .openGallery(PictureMimeType.ofImage())
        .imageEngine(GlideEngine.default())
        .theme(themeId)
        .isWeChatStyle(themeId == R.style.picture_WeChat_style)
        .selectionMode(if (maxSize == 1) PictureConfig.SINGLE else PictureConfig.MULTIPLE)
        .isCompress(isCompress)
        .selectionData(selectionData)
        // .isEnableCrop(maxSize == 1)
        // 裁剪质量, 默认90
        .cutOutQuality(85)
        .synOrAsy(false)
        // 小于100kb的图片不压缩
        .minimumCompressSize(100)
        .maxSelectNum(maxSize)
        .withAspectRatio(1, 1)
        .forResult(callback)
}

/**
 * 拍照
 */
inline fun FragmentActivity.takePhoto(
    isCompress: Boolean = true,
    enableCrop: Boolean = true,
    callback: OnResultCallbackListener<LocalMedia>? = null
) {
    PictureSelector.create(this)
        .openCamera(PictureMimeType.ofImage()) // 只显示图片
        .imageEngine(GlideEngine.default())
        .isCompress(isCompress)
        .isEnableCrop(enableCrop)
        .cutOutQuality(85) // 裁剪质量, 默认90
        .withAspectRatio(1, 1)
        .forResult(callback)
}