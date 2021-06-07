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
        val width = ta.getDimensionPixelSize(R.styleable.SuperFormView_formRightCustomViewWidth, 0)

        val rightParams = RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.WRAP_CONTENT)
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL)
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        rightParams.marginEnd =
            ta.getDimensionPixelSize(R.styleable.SuperFormView_formRightViewMarginRight, context.dp2px(10f))

        val customViewId = ta.getResourceId(R.styleable.SuperFormView_formRightCustomView, -1)

        return if (customViewId != -1) {
            val customView = View.inflate(context, customViewId, null)
            customView.layoutParams = rightParams
            customView
        } else {
            return RelativeLayout(context)
        }
    }
}