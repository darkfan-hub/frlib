package com.frlib.basic.views.form

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatEditText
import com.frlib.basic.R
import com.frlib.utils.ext.color
import com.frlib.utils.ext.dp2px
import com.frlib.utils.ext.invalid
import com.frlib.utils.ext.sp2Px
import timber.log.Timber


/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 03/06/2021 11:15
 * @desc 默认表单view
 */
class BasicFormView : AbstractFormView() {

    private lateinit var rightEditText: AppCompatEditText

    override fun formRightView(context: Context, ta: TypedArray): View {
        val minHeight = ta.getDimensionPixelSize(R.styleable.SuperFormView_formMinHeight, context.dp2px(54f))

        val rightView = RelativeLayout(context)
        rightView.minimumHeight = minHeight
        val rightParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rightParams.marginStart =
            ta.getDimensionPixelSize(R.styleable.SuperFormView_formRightViewMarginLeft, context.dp2px(128f))
        rightView.layoutParams = rightParams

        // icon
        var iconId = 0
        if (ta.hasValue(R.styleable.SuperFormView_formRightIcon) || ta.hasValue(R.styleable.SuperFormView_formRightIconUrl)) {
            val circleImage = ta.getBoolean(R.styleable.SuperFormView_formRightIconCircle, false)
            val icon = createIcon(
                context,
                circleImage,
                ta.getString(R.styleable.SuperFormView_formRightIconUrl),
                ta.getDrawable(R.styleable.SuperFormView_formRightIcon)
            )
            iconId = icon.id

            val iconParams =
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            iconParams.addRule(RelativeLayout.CENTER_VERTICAL)
            iconParams.addRule(RelativeLayout.ALIGN_PARENT_END)
            iconParams.marginEnd =
                ta.getDimensionPixelSize(R.styleable.SuperFormView_formRightIconMargin, context.dp2px(10f))
            icon.layoutParams = iconParams

            rightView.addView(icon)
        }

        rightEditText = createEditText(
            context,
            ta.getBoolean(R.styleable.SuperFormView_formRightEditEnable, false),
            ta.getString(R.styleable.SuperFormView_formRightText).invalid(),
            ta.getDimensionPixelSize(R.styleable.SuperFormView_formRightTextSize, context.sp2Px(17f)),
            ta.getColor(R.styleable.SuperFormView_formRightTextColor, context.color(R.color.color_33)),
            ta.getString(R.styleable.SuperFormView_formRightHintText).invalid(),
            ta.getColor(R.styleable.SuperFormView_formRightHintTextColor, context.color(R.color.color_cc))
        )
        // 文本输入类型
        when (ta.getInt(R.styleable.SuperFormView_formRightTextInputType, 0)) {
            /*0 -> rightEditText.inputType = InputType.TYPE_CLASS_TEXT
            1 -> rightEditText.inputType = InputType.TYPE_CLASS_NUMBER*/
            2 -> rightEditText.inputType = InputType.TYPE_CLASS_PHONE
        }
        // 文本位置
        val editGravity = ta.getInt(R.styleable.SuperFormView_formRightTextGravity, 0)
        changeEditGravity(editGravity)
        // 文本是否可点击
        val enable = ta.getBoolean(R.styleable.SuperFormView_formRightEditEnable, false)
        if (!enable) {
            rightEditText.isFocusable = false
            rightEditText.isFocusableInTouchMode = false
            rightEditText.keyListener = null
        }
        rightEditText.isEnabled = enable
        // 文本最大字符
        val maxSize = ta.getInteger(R.styleable.SuperFormView_formRightTextMaxSize, 30)
        rightEditText.filters = arrayOf<InputFilter>(LengthFilter(maxSize))
        rightEditText.background = null
        rightEditText.minHeight = minHeight
        val paddingVertical =
            ta.getDimensionPixelSize(R.styleable.SuperFormView_formRightPaddingVertical, context.dp2px(15f))
        rightEditText.setPadding(0, paddingVertical, 0, paddingVertical)
        // 文本输入行数监听
        rightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lineCount = rightEditText.lineCount
                Timber.i("当前行数: $lineCount")

                changeEditGravity(if (lineCount > 1) 1 else 0)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        val rightEditParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        if (iconId != 0) {
            rightEditParams.addRule(RelativeLayout.START_OF, iconId)
        } else {
            rightEditParams.addRule(RelativeLayout.ALIGN_PARENT_END)
        }
        rightEditParams.marginEnd =
            ta.getDimensionPixelSize(R.styleable.SuperFormView_formRightTextMargin, context.dp2px(10f))
        rightEditText.layoutParams = rightEditParams

        rightView.addView(rightEditText)
        return rightView
    }

    /**
     * 当edit多行时改变Gravity
     */
    private fun changeEditGravity(editGravity: Int) {
        when (editGravity) {
            0 -> {
                rightEditText.gravity = Gravity.END or Gravity.TOP
            }
            1 -> {
                rightEditText.gravity = Gravity.START or Gravity.TOP
            }
        }
    }

    fun setRightText(text: String) {
        rightEditText.setText(text)
    }

    fun setRightHintText(text: String) {
        rightEditText.hint = text
    }

    /**
     * 添加右侧文字输入监听
     */
    fun addRightTextChangedListener(watcher: TextWatcher) {
        rightEditText.addTextChangedListener(watcher)
    }

    fun getRightText(): String {
        return rightEditText.text.toString().invalid()
    }
}