package com.frlib.basic.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.frlib.basic.R
import com.frlib.basic.databinding.FrlibLayoutEmptyBinding
import com.frlib.utils.ext.drawable

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @since 21/12/2020 16:31
 * @desc 空布局, iconMarginTop = 73dp, textMarginTop = 32dp
 */
class EmptyView(
    context: Context
) : FrameLayout(context) {

    companion object {

        @JvmStatic
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    private var icon: Int = R.drawable.frlib_icon_net_error
    private var text: String = ""
    private var iconMarginTop: Int = 0
    private var textMarginTop: Int = 0
    private var retryText: String = ""
    private var retryBackground: Drawable? = null
    private var retryVisibility: Boolean = false
    private var retryListener: OnClickListener? = null

    constructor(builder: Builder) : this(builder.context) {
        this.icon = builder.icon
        this.text = builder.text
        this.iconMarginTop = builder.iconMarginTop
        this.textMarginTop = builder.textMarginTop
        this.retryText = builder.retryText
        this.retryBackground = builder.retryBackground
        this.retryVisibility = builder.retryVisibility
        this.retryListener = builder.retryListener

        initEmptyView()
    }

    private fun initEmptyView() {
        val binding = FrlibLayoutEmptyBinding.inflate(LayoutInflater.from(context))
        addView(binding.root)

        if (iconMarginTop > 0) {
            val iconLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            iconLayoutParams.setMargins(0, iconMarginTop, 0, 0)
            binding.ivEmptyIcon.layoutParams = iconLayoutParams
        }

        binding.ivEmptyIcon.setImageDrawable(context.drawable(icon))

        if (textMarginTop > 0) {
            val textLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            textLayoutParams.setMargins(0, textMarginTop, 0, 0)
            binding.tvEmptyText.layoutParams = textLayoutParams
        }

        binding.tvEmptyText.text = text

        binding.btEmptyRetry.text = retryText
        if (retryBackground != null) {
            binding.btEmptyRetry.background = retryBackground
        }
        binding.btEmptyRetry.visibility = if (retryVisibility) View.VISIBLE else View.GONE
        retryListener?.let { binding.btEmptyRetry.setOnClickListener(it) }
    }

    class Builder {
        var icon: Int = R.drawable.frlib_icon_net_error
        var text: String = "空空如也~"
        var iconMarginTop: Int = 0
        var textMarginTop: Int = 0
        var retryText: String = "重试"
        var retryBackground: Drawable? = null
        var retryVisibility: Boolean = false
        var retryListener: OnClickListener? = null

        lateinit var context: Context

        fun context(context: Context) {
            this.context = context
        }

        fun build() = EmptyView(this)
    }
}