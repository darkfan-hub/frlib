package com.frlib.basic.views.form

import android.content.Context
import android.content.res.TypedArray
import android.view.View
import android.widget.RelativeLayout
import com.frlib.basic.R
import com.frlib.utils.ext.dp2px

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 03/06/2021 15:29
 * @desc
 */
class CustomFormView : AbstractFormView() {

    override fun formRightView(context: Context, ta: TypedArray): View {
        val minHeight = ta.getDimensionPixelSize(R.styleable.SuperFormView_formMinHeight, context.dp2px(54f))

        val rightParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL)
        rightParams.marginEnd =
            ta.getDimensionPixelSize(R.styleable.SuperFormView_formRightViewMarginRight, context.dp2px(10f))

        val customViewId = ta.getResourceId(R.styleable.SuperFormView_formRightCustomView, -1)

        return if (customViewId != -1) {
            val customView = View.inflate(context, customViewId, null)
            customView.minimumHeight = minHeight
            customView.layoutParams = rightParams
            customView
        } else {
            return RelativeLayout(context)
        }
    }

    override fun setRightText(text: String) {
    }
}