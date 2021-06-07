package com.frlib.basic.views.form

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.frlib.basic.R
import com.frlib.basic.image.displayImage
import com.frlib.basic.views.CircleImageView
import com.frlib.utils.ext.*

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 03/06/2021 09:49
 * @desc 表单view
 */
abstract class AbstractFormView : ISuperFormView {

    private var leftText: AppCompatTextView? = null

    override fun formLeftView(context: Context, ta: TypedArray): View {
        val minHeight = ta.getDimensionPixelSize(R.styleable.SuperFormView_formMinHeight, context.dp2px(54f))

        val leftView = RelativeLayout(context)
        leftView.layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, minHeight)

        // 左侧icon
        var iconId = 0
        if (ta.hasValue(R.styleable.SuperFormView_formLeftIcon) || ta.hasValue(R.styleable.SuperFormView_formLeftIconUrl)) {
            val circleImage = ta.getBoolean(R.styleable.SuperFormView_formLeftIconCircle, false)
            val icon = createIcon(
                context,
                circleImage,
                ta.getString(R.styleable.SuperFormView_formLeftIconUrl),
                ta.getDrawable(R.styleable.SuperFormView_formLeftIcon)
            )
            iconId = icon.id

            val iconParams =
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            iconParams.addRule(RelativeLayout.CENTER_VERTICAL)
            iconParams.marginStart = ta.getDimensionPixelSize(R.styleable.SuperFormView_formLeftIconMargin, context.dp2px(10f))
            icon.layoutParams = iconParams

            leftView.addView(icon)
        }

        // 左侧必填
        val mustFill = ta.getBoolean(R.styleable.SuperFormView_formMustFill, false)
        var mustViewId = 0
        if (mustFill) {
            val must = createText(
                context,
                ta.getString(R.styleable.SuperFormView_formMustText).invalid(),
                Gravity.CENTER,
                ta.getColor(
                    R.styleable.SuperFormView_formMustTextColor,
                    context.color(R.color.color_cc)
                ),
                ta.getDimensionPixelSize(R.styleable.SuperFormView_formMustTextSize, context.sp2Px(17f))
            )
            mustViewId = must.id

            val mustParams =
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, minHeight)
            mustParams.addRule(RelativeLayout.CENTER_VERTICAL)
            if (iconId != 0) {
                mustParams.addRule(RelativeLayout.END_OF, iconId)
            }
            mustParams.marginStart = ta.getDimensionPixelSize(R.styleable.SuperFormView_formMustTextMargin, context.dp2px(15f))

            must.layoutParams = mustParams
            leftView.addView(must)
        }

        // 左侧文字
        if (ta.hasValue(R.styleable.SuperFormView_formLeftText)) {
            leftText = createText(
                context,
                ta.getString(R.styleable.SuperFormView_formLeftText).invalid(),
                Gravity.CENTER,
                ta.getColor(
                    R.styleable.SuperFormView_formLeftTextColor,
                    context.color(R.color.color_33)
                ),
                ta.getDimensionPixelSize(R.styleable.SuperFormView_formLeftTextSize, context.sp2Px(17f))
            )

            val leftTextParams =
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, minHeight)
            leftTextParams.addRule(RelativeLayout.CENTER_VERTICAL)
            if (mustViewId != 0) {
                leftTextParams.addRule(RelativeLayout.END_OF, mustViewId)
            } else if (iconId != 0) {
                leftTextParams.addRule(RelativeLayout.END_OF, iconId)
            }

            leftTextParams.marginStart = ta.getDimensionPixelSize(R.styleable.SuperFormView_formLeftTextMargin, context.dp2px(2f))

            leftText!!.layoutParams = leftTextParams
            leftView.addView(leftText!!)
        }

        return leftView
    }

    fun setLeftText(text: String) {
        leftText?.text = text
    }

    open fun createIcon(context: Context, isCircle: Boolean, url: String?, drawable: Drawable?): AppCompatImageView {
        val icon: AppCompatImageView = if (isCircle) {
            CircleImageView(context)
        } else {
            AppCompatImageView(context)
        }
        icon.id = View.generateViewId()
        if (url.length() > 0) {
            icon.displayImage(url.invalid())
        } else {
            icon.setImageDrawable(drawable)
        }

        return icon
    }

    open fun createText(
        context: Context,
        textString: String,
        gravity: Int,
        textColor: Int,
        textSize: Int
    ): AppCompatTextView {
        val text = AppCompatTextView(context)
        text.id = View.generateViewId()
        text.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
        text.text = textString
        text.gravity = gravity
        text.setTextColor(textColor)
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        return text
    }

    open fun createEditText(
        context: Context,
        isEdit: Boolean,
        textString: String,
        textSize: Int,
        textColor: Int,
        hintText: String,
        hintColor: Int,
    ): AppCompatEditText {
        val edit = AppCompatEditText(context)
        edit.id = View.generateViewId()
        edit.isEnabled = isEdit
        edit.setText(textString)
        edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        edit.setTextColor(textColor)
        edit.hint = hintText
        edit.setHintTextColor(hintColor)
        return edit
    }
}