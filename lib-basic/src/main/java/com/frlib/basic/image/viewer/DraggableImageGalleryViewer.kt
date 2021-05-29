package com.frlib.basic.image.viewer

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.FrameLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.frlib.basic.databinding.FrlibLayoutImageViewrBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by susion on 2019/08/15
 * 可拖拽图片浏览容器
 */
class DraggableImageGalleryViewer(
    context: Context,
    private val pageChangeListener: ViewPager.OnPageChangeListener? = null
) : FrameLayout(context) {

    private val TAG_PREGIX = "DraggableImageGalleryViewer_"
    var actionListener: ActionListener? = null
    private val mImageList = ArrayList<DraggableImageInfo>()
    private var showWithAnimator: Boolean = true

    private var binding: FrlibLayoutImageViewrBinding =
        FrlibLayoutImageViewrBinding.inflate(LayoutInflater.from(context), this)

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        background = ColorDrawable(Color.TRANSPARENT)
        initAdapter()
        binding.mImageGalleryViewOriginDownloadImg.setOnClickListener {
            val currentImg = mImageList[binding.mImageViewerViewPage.currentItem]
            GlideHelper.downloadPicture(context, currentImg.originImg)
        }
    }

    fun showImagesWithAnimator(imageList: List<DraggableImageInfo>, index: Int = 0) {
        mImageList.clear()
        mImageList.addAll(imageList)
        binding.mImageViewerViewPage.adapter?.notifyDataSetChanged()
        setCurrentImgIndex(index)
    }

    private fun initAdapter() {
        binding.mImageViewerViewPage.adapter = object : PagerAdapter() {

            override fun getItemPosition(`object`: Any): Int {
                return POSITION_NONE
            }

            override fun isViewFromObject(view: View, any: Any) = view == any

            override fun getCount() = mImageList.size

            override fun instantiateItem(container: ViewGroup, position: Int): DraggableImageView {
                val imageInfo = mImageList[position]
                val imageView = getImageViewFromCacheContainer()
                container.addView(
                    imageView,
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                )
                if (showWithAnimator) {
                    showWithAnimator = false
                    imageView.showImageWithAnimator(imageInfo)
                } else {
                    imageView.showImage(imageInfo)
                }
                imageView.tag = "$TAG_PREGIX$position"
                binding.mImageGalleryViewOriginDownloadImg.visibility =
                    if (imageInfo.imageCanDown) View.VISIBLE else View.GONE
                return imageView
            }

            override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
                container.removeView(any as View)
            }
        }

        binding.mImageViewerViewPage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                setCurrentImgIndex(position)
            }
        })

        pageChangeListener?.let { binding.mImageViewerViewPage.addOnPageChangeListener(it) }
    }

    private fun setCurrentImgIndex(index: Int) {
        binding.mImageViewerViewPage.setCurrentItem(index, false)
        if (mImageList.size > 1) {
            binding.mImageViewerTvIndicator.text =
                String.format(Locale.getDefault(), "%d/%d", (index + 1), mImageList.size)
        } else {
            binding.mImageViewerTvIndicator.visibility = View.GONE
        }
    }

    private val vpContentViewList = ArrayList<DraggableImageView>()
    private fun getImageViewFromCacheContainer(): DraggableImageView {
        var availableImageView: DraggableImageView? = null
        if (vpContentViewList.isNotEmpty()) {
            vpContentViewList.forEach {
                if (it.parent == null) {
                    availableImageView = it
                }
            }
        }

        if (availableImageView == null) {
            availableImageView = DraggableImageView(context).apply {
                actionListener = object : DraggableImageView.ActionListener {
                    override fun onExit() {
                        this@DraggableImageGalleryViewer.actionListener?.closeViewer()
                    }
                }
            }
            vpContentViewList.add(availableImageView!!)
        }

        return availableImageView!!
    }

    fun closeWithAnimator(): Boolean {
        val currentView =
            findViewWithTag<DraggableImageView>("$TAG_PREGIX${binding.mImageViewerViewPage.currentItem}")
        val imageInfo = mImageList[binding.mImageViewerViewPage.currentItem]
        if (imageInfo.draggableInfo.isValid()) {
            currentView?.closeWithAnimator()
        } else {
            actionListener?.closeViewer()
        }
        return true
    }

    interface ActionListener {
        fun closeViewer()
    }
}