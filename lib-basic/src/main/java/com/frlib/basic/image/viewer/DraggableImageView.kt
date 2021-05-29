package com.frlib.basic.image.viewer

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.frlib.basic.R
import com.frlib.basic.databinding.FrlibLayoutDraggableSimpleImageBinding
import com.frlib.utils.ConvertUtil
import com.frlib.utils.UIUtil
import io.reactivex.rxjava3.disposables.Disposable
import timber.log.Timber

/**
 * 单张图片查看器
 *
 * 1. 支持动画进入和退出
 * 2. 支持拖放退出
 *
 * */
class DraggableImageView : FrameLayout {

    interface ActionListener {
        fun onExit() // drag to exit
    }

    private var draggableImageInfo: DraggableImageInfo? = null
    var actionListener: ActionListener? = null
    private var currentLoadUrl = ""
    private var downloadDisposable: Disposable? = null
    private var draggableZoomCore: DraggableZoomCore? = null
    private var needFitCenter = true
    private var viewSelfWhRadio = 1f

    private lateinit var binding: FrlibLayoutDraggableSimpleImageBinding

    private var draggableZoomActionListener = object : DraggableZoomCore.ActionListener {
        override fun currentAlphaValue(alpha: Int) {
            background = ColorDrawable(Color.argb(alpha, 0, 0, 0))
        }

        override fun onExit() {
            actionListener?.onExit()
        }
    }

    private val exitAnimatorCallback = object : DraggableZoomCore.ExitAnimatorCallback {
        override fun onStartInitAnimatorParams() {
            binding.pvDraggableImage.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    private fun initView() {
        binding = FrlibLayoutDraggableSimpleImageBinding.inflate(LayoutInflater.from(context), this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        setOnClickListener {
            clickToExit()
        }

        binding.pvDraggableImage.setOnClickListener {
            clickToExit()
        }

        binding.tvDraggableOriginImage.setOnClickListener {
            loadImage(draggableImageInfo?.originImg ?: "", false)
        }
        binding.progressDraggableImage.indeterminateDrawable.setColorFilter(
            Color.parseColor("#ebebeb"),
            PorterDuff.Mode.MULTIPLY
        )
    }

    private fun clickToExit() {
        if (draggableZoomCore?.isAnimating == true) return
        binding.progressDraggableImage.visibility = View.GONE
        if (binding.pvDraggableImage.scale != 1f) {
            binding.pvDraggableImage.setScale(1f, true)
        } else {
            draggableZoomCore?.adjustScaleViewToCorrectLocation()
            draggableZoomCore?.exitWithAnimator(false)
            downloadDisposable?.dispose()
        }
    }

    /**
     * 拖拽支持
     * */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val isIntercept = super.onInterceptTouchEvent(ev)
        if (draggableZoomCore?.isAnimating == true) {
            return false
        }
        if (binding.pvDraggableImage.scale != 1f) {
            return false
        }
        if (!binding.pvDraggableImage.attacher.displyRectIsFromTop()) {
            return false
        }
        if (binding.progressDraggableImage.visibility == View.VISIBLE) {    // loading 时不允许拖拽退出
            return false
        }
        return draggableZoomCore?.onInterceptTouchEvent(isIntercept, ev) ?: false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        draggableZoomCore?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun showImageWithAnimator(paramsInfo: DraggableImageInfo) {
        draggableImageInfo = paramsInfo
        currentLoadUrl = ""
        refreshOriginImageInfo()
        GlideHelper.retrieveImageWhRadioFromMemoryCache(
            context,
            paramsInfo.thumbnailImg
        ) { inMemCache, whRadio, isGif ->
            draggableImageInfo?.draggableInfo?.scaledViewWhRadio = whRadio
            post {

                viewSelfWhRadio = (width * 1f / height)
                needFitCenter = whRadio > viewSelfWhRadio
                if (!paramsInfo.draggableInfo.isValid() || (isGif && !needFitCenter)) {
                    //退出的时候不要再开启动画
                    paramsInfo.draggableInfo = DraggableParamsInfo()
                    showImage(paramsInfo)
                    return@post
                }

                draggableZoomCore = DraggableZoomCore(
                    paramsInfo.draggableInfo,
                    binding.pvDraggableImage,
                    width,
                    height,
                    draggableZoomActionListener,
                    exitAnimatorCallback
                )
                draggableZoomCore?.adjustScaleViewToInitLocation()
                loadAvailableImage(true, inMemCache)
            }
        }
    }

    fun showImage(paramsInfo: DraggableImageInfo) {
        draggableImageInfo = paramsInfo
        currentLoadUrl = ""
        refreshOriginImageInfo()
        GlideHelper.retrieveImageWhRadioFromMemoryCache(
            context,
            paramsInfo.thumbnailImg
        ) { inMemCache, whRadio, isGif ->
            draggableImageInfo?.draggableInfo?.scaledViewWhRadio = whRadio
            post {
                viewSelfWhRadio = (width * 1f / height)
                needFitCenter = whRadio > viewSelfWhRadio

                draggableZoomCore = DraggableZoomCore(
                    paramsInfo.draggableInfo,
                    binding.pvDraggableImage,
                    width,
                    height,
                    draggableZoomActionListener,
                    exitAnimatorCallback
                )
                draggableZoomCore?.adjustScaleViewToCorrectLocation()
                loadAvailableImage(false, inMemCache)
            }
        }
    }

    private fun loadAvailableImage(startAnimator: Boolean, imgInMemCache: Boolean) {
        if ((context as? AppCompatActivity)?.isDestroyed == true || (context as? AppCompatActivity)?.isFinishing == true) {
            return
        }

        binding.pvDraggableImage.scaleType = ImageView.ScaleType.FIT_CENTER
        binding.pvDraggableImage.setImageResource(R.drawable.frlib_image_viewer_place_holder)

        val thumnailImg = draggableImageInfo!!.thumbnailImg
        val originImg = draggableImageInfo!!.originImg

        // val networkInfoEntity = NetworkInfoHelper.networkInfo(context)

        val originImgInCache = GlideHelper.imageIsInCache(context, originImg)

        // val targetUrl = if (networkInfoEntity.isWifi || originImgInCache) originImg else thumnailImg
        val targetUrl = originImg

        setViewOriginImageBtnVisible(targetUrl != originImg)

        if (imgInMemCache) {
            loadImage(thumnailImg, originImgInCache)
        }

        if (imgInMemCache && startAnimator) {  //只有缩略图在缓存中时，才播放缩放入场动画
            draggableZoomCore?.enterWithAnimator(object :
                DraggableZoomCore.EnterAnimatorCallback {
                override fun onEnterAnimatorStart() {
                    binding.pvDraggableImage.scaleType = ImageView.ScaleType.CENTER_CROP
                }

                override fun onEnterAnimatorEnd() {
                    if (needFitCenter) {
                        binding.pvDraggableImage.scaleType = ImageView.ScaleType.FIT_CENTER
                        draggableZoomCore?.adjustViewToMatchParent()
                    }
                    loadImage(targetUrl, originImgInCache)
                }
            })
        } else {
            loadImage(targetUrl, originImgInCache)
            if (needFitCenter) {
                draggableZoomCore?.adjustViewToMatchParent()
            }
        }
    }

    private fun loadImage(url: String, originIsInCache: Boolean) {

        if (url == currentLoadUrl) return

        if ((context is Activity) && ((context as Activity).isFinishing || (context as Activity).isDestroyed)) {
            return
        }

        currentLoadUrl = url

        if (url == draggableImageInfo?.originImg && !originIsInCache) {
            binding.progressDraggableImage.visibility = View.VISIBLE
        }

        val options = RequestOptions().priority(Priority.HIGH) //被查看的图片应该由最高的请求优先级

        Glide.with(context)
            .load(url)
            .apply(options)
            .into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    val isGif = resource is GifDrawable
                    binding.progressDraggableImage.visibility = View.GONE
                    val whRadio = resource.intrinsicWidth * 1f / resource.intrinsicHeight
                    val longImage = whRadio < viewSelfWhRadio

                    if (isGif) {
                        if (longImage) {
                            binding.pvDraggableImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
                        }
                        // TODO 替换为glide引擎加载图片，方便统一修改
                        Glide.with(context).load(url).into(binding.pvDraggableImage)
                    } else {
                        //普通图片已经做了缩放 -> 宽度缩放至屏幕的宽度
                        binding.pvDraggableImage.scaleType =
                            if (longImage) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
                        binding.pvDraggableImage.setImageBitmap(translateToFixedBitmap(resource))
                    }

                    if (url == draggableImageInfo?.originImg) {
                        binding.tvDraggableOriginImage.visibility = View.GONE
                    }
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    binding.progressDraggableImage.visibility = View.GONE
                }
            })
    }

    //avoid oom
    private fun translateToFixedBitmap(originDrawable: Drawable): Bitmap? {
        val whRadio = originDrawable.intrinsicWidth * 1f / originDrawable.intrinsicHeight

        val screenWidth = UIUtil.getScreenWidth(context)

        var bpWidth = if (this@DraggableImageView.width != 0) {
            if (originDrawable.intrinsicWidth > this@DraggableImageView.width) {
                this@DraggableImageView.width
            } else {
                originDrawable.intrinsicWidth
            }
        } else {
            if (originDrawable.intrinsicWidth > screenWidth) {
                screenWidth
            } else {
                originDrawable.intrinsicWidth
            }
        }

        if (bpWidth > screenWidth) bpWidth = screenWidth

        val bpHeight = (bpWidth * 1f / whRadio).toInt()

        Timber.d("bpWidth : $bpWidth  bpHeight : $bpHeight")

        var bp = Glide.get(context).bitmapPool.get(
            bpWidth,
            bpHeight,
            if (bpHeight > 4000) Bitmap.Config.RGB_565 else Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bp)
        originDrawable.setBounds(0, 0, bpWidth, bpHeight)
        originDrawable.draw(canvas)

        return bp
    }

    private fun refreshOriginImageInfo() {
        if (draggableImageInfo!!.imageSize > 0) {
            binding.tvDraggableOriginImage.text =
                "查看原图(${ConvertUtil.byte2FitMemorySize(draggableImageInfo?.imageSize ?: 0)})"
        } else {
            binding.tvDraggableOriginImage.text = "查看原图"
        }
    }

    private fun setViewOriginImageBtnVisible(visible: Boolean) {
        binding.tvDraggableOriginImage.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun closeWithAnimator() {
        draggableZoomCore?.adjustScaleViewToCorrectLocation()
        draggableZoomCore?.exitWithAnimator(false)
        downloadDisposable?.dispose()
    }

}