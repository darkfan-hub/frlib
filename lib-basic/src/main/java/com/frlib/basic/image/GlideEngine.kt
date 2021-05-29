package com.frlib.basic.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.ImageViewTarget
import com.frlib.basic.R
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.tools.MediaUtils
import com.luck.picture.lib.widget.longimage.ImageSource
import com.luck.picture.lib.widget.longimage.ImageViewState
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 15/02/2021 15:05
 * @desc Glide加载引擎
 */
class GlideEngine : ImageEngine {

    companion object {
        private var instances: GlideEngine? = null

        fun default(): GlideEngine {
            if (null == instances) {
                synchronized(GlideEngine::class.java) {
                    if (null == instances) {
                        instances = GlideEngine()
                    }
                }
            }

            return instances!!
        }
    }

    override fun loadImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context).load(url).placeholder(R.drawable.frlib_iamge_placeholder).centerCrop().into(imageView)
    }

    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?,
        callback: OnImageCompleteCallback?
    ) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .into(object : ImageViewTarget<Bitmap>(imageView) {
                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    callback?.onShowLoading()
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    callback?.onHideLoading()
                }

                override fun setResource(resource: Bitmap?) {
                    callback?.onHideLoading()

                    resource?.let {
                        val eqLongImage = MediaUtils.isLongImg(resource.width, resource.height)
                        longImageView?.visibility = if (eqLongImage) View.VISIBLE else View.GONE
                        imageView.visibility = if (eqLongImage) View.GONE else View.VISIBLE
                        if (eqLongImage) {
                            longImageView?.apply {
                                // 加载长图
                                isQuickScaleEnabled = true
                                isZoomEnabled = true
                                setDoubleTapZoomDuration(100)
                                setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
                                setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
                                setImage(
                                    ImageSource.bitmap(resource),
                                    ImageViewState(0f, PointF(0f, 0f), 0)
                                )
                            }
                        } else {
                            imageView.setImageBitmap(it)
                        }
                    }
                }
            })
    }

    @Deprecated(message = "弃用请调用 @ loadImage(context, url, imageView, longImageView, callback)方法")
    override fun loadImage(
        context: Context,
        url: String,
        imageView: ImageView,
        longImageView: SubsamplingScaleImageView?
    ) {}

    /**
     * 加载相册目录
     *
     * @param [context]   上下文
     * @param [url]       图片路径
     * @param [imageView] 承载图片ImageView
     */
    override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
        val options = RequestOptions().placeholder(R.drawable.frlib_iamge_placeholder)
            .override(180, 180)
            .sizeMultiplier(0.5f)
            .centerCrop()

        Glide.with(context)
            .asBitmap()
            .load(url)
            .apply(options)
            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
                    circularBitmapDrawable.cornerRadius = 8f
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
    }

    override fun loadAsGifImage(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .asGif()
            .load(url)
            .into(imageView)
    }

    override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
        val options = RequestOptions().placeholder(R.drawable.frlib_iamge_placeholder)
            .override(200, 200)
            .centerCrop()

        Glide.with(context)
            .load(url)
            .apply(options)
            .into(imageView)
    }
}