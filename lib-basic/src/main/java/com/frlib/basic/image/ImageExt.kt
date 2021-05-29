package com.frlib.basic.image

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
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

inline fun ImageView.displayImage(
    url: String,
    placeholder: Int = R.drawable.frlib_iamge_placeholder
) {
    displayImage(this.context, url, placeholder)
}

inline fun ImageView.displayImage(
    context: Context,
    url: String,
    placeholder: Int = R.drawable.frlib_iamge_placeholder
) {
    val options = RequestOptions().placeholder(placeholder).centerCrop()

    Glide.with(context)
        .load(url)
        .apply(options)
        .into(this)
}

inline fun ImageView.displayRadiusImage(
    url: String,
    radius: Int,
    placeholder: Int = R.drawable.frlib_iamge_placeholder
) {
    displayRadiusImage(this.context, url, radius, placeholder)
}

inline fun ImageView.displayRadiusImage(
    context: Context,
    url: String,
    radius: Int,
    placeholder: Int = R.drawable.frlib_iamge_placeholder
) {
    val options = RequestOptions().placeholder(placeholder).transform(
        CenterCrop(),
        RoundedCornersTransformation(
            radius,
            0,
            RoundedCornersTransformation.CornerType.ALL
        )
    )

    Glide.with(context)
        .load(url)
        .apply(options)
        .into(this)
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
        .isEnableCrop(maxSize == 1)
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
        .isCompress(isCompress)
        .isEnableCrop(enableCrop)
        .cutOutQuality(85) // 裁剪质量, 默认90
        .withAspectRatio(1, 1)
        .forResult(callback)
}