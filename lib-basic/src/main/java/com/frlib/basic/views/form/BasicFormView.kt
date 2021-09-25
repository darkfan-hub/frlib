package com.frlib.basic.views.form

import android.content.Context
import android.content.res.TypedArray
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.frlib.basic.R
import com.frlib.basic.ext.click
import com.frlib.utils.RegexUtil
import com.frlib.utils.ext.*
import timber.log.Timber

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 03/06/2021 11:15
 * @desc 默认表单view
 */
class BasicFormView : AbstractFormView() {

    private lateinit var rightEditText: AppCompatEditText
    private lateinit var rightEditTopView: View
    private var rightIcon: AppCompatImageView? = null

    override fun formRightView(context: Context, ta: TypedArray): View {
        val minHeight =
            ta.getDimensionPixelSize(R.styleable.SuperFormView_formMinHeight, context.dp2px(54f))

        val rightView = RelativeLayout(context)
        rightView.minimumHeight = minHeight
        val rightParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rightParams.marginStart =
            ta.getDimensionPixelSize(
                R.styleable.SuperFormView_formRightViewMarginLeft,
                context.dp2px(128f)
            )
        rightView.layoutParams = rightParams

        // icon
        var iconId = 0
        if (ta.hasValue(R.styleable.SuperFormView_formRightIcon) || ta.hasValue(R.styleable.SuperFormView_formRightIconUrl)) {
            val circleImage = ta.getBoolean(R.styleable.SuperFormView_formRightIconCircle, false)
            rightIcon = createIcon(
                context,
                circleImage,
                ta.getString(R.styleable.SuperFormView_formRightIconUrl),
                ta.getDrawable(R.styleable.SuperFormView_formRightIcon)
            )
            iconId = rightIcon!!.id

            val iconParams =
                RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            iconParams.addRule(RelativeLayout.CENTER_VERTICAL)
            iconParams.addRule(RelativeLayout.ALIGN_PARENT_END)
            iconParams.marginEnd =
                ta.getDimensionPixelSize(
                    R.styleable.SuperFormView_formRightIconMargin,
                    context.dp2px(10f)
                )

            rightView.addView(rightIcon!!, iconParams)
        }

        var rightPlaceTextId = 0
        if (ta.hasValue(R.styleable.SuperFormView_formRightPlaceText)) {
            val rightPlaceText = createText(
                context,
                ta.getString(R.styleable.SuperFormView_formRightPlaceText).invalid(),
                Gravity.START,
                ta.getColor(
                    R.styleable.SuperFormView_formRightPlaceTextColor,
                    context.color(R.color.color_33)
                ),
                ta.getDimensionPixelSize(
                    R.styleable.SuperFormView_formRightPlaceTextSize,
                    context.sp2Px(17f)
                )
            )
            rightPlaceTextId = rightPlaceText.id
            val rightPlaceTextParams =
                RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            rightPlaceTextParams.addRule(RelativeLayout.CENTER_VERTICAL)

            if (iconId != 0) {
                rightPlaceTextParams.addRule(RelativeLayout.START_OF, iconId)
            } else {
                rightPlaceTextParams.addRule(RelativeLayout.ALIGN_PARENT_END)
            }

            rightPlaceTextParams.marginEnd =
                ta.getDimensionPixelSize(
                    R.styleable.SuperFormView_formRightPlaceTextMargin,
                    context.dp2px(10f)
                )
            rightView.addView(rightPlaceText, rightPlaceTextParams)
        }

        rightEditText = createEditText(
            context,
            ta.getBoolean(R.styleable.SuperFormView_formRightEditEnable, false),
            ta.getString(R.styleable.SuperFormView_formRightText).invalid(),
            ta.getDimensionPixelSize(
                R.styleable.SuperFormView_formRightTextSize,
                context.sp2Px(17f)
            ),
            ta.getColor(
                R.styleable.SuperFormView_formRightTextColor,
                context.color(R.color.color_33)
            ),
            ta.getString(R.styleable.SuperFormView_formRightHintText).invalid(),
            ta.getColor(
                R.styleable.SuperFormView_formRightHintTextColor,
                context.color(R.color.color_cc)
            )
        )
        val rightEditFilters = arrayListOf<InputFilter>()
        // 文本输入类型
        when (ta.getInt(R.styleable.SuperFormView_formRightTextInputType, 4)) {
            0 -> rightEditText.inputType = InputType.TYPE_CLASS_TEXT
            1 -> rightEditText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
            2 -> rightEditText.inputType = InputType.TYPE_CLASS_PHONE
            3 -> rightEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            5 -> {
                val idCardFilter = object : InputFilter {
                    override fun filter(
                        source: CharSequence?,
                        start: Int,
                        end: Int,
                        dest: Spanned?,
                        dstart: Int,
                        dend: Int
                    ): CharSequence {
                        val charSequence = source.toString()
                        if (charSequence.length() > 0) {
                            val isIdCard = RegexUtil.isMatch("^[1234567890X]+$", charSequence)
                            if (!Character.isLetterOrDigit(charSequence[start]) || !isIdCard) {
                                return ""
                            }
                        }

                        return charSequence
                    }
                }
                rightEditFilters.add(idCardFilter)
            }
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
        // 文本是否单行显示
        val singleLine = ta.getBoolean(R.styleable.SuperFormView_formSingleLine, false)
        rightEditText.isSingleLine = singleLine
        // 文本最大字符
        val maxSize = ta.getInteger(R.styleable.SuperFormView_formRightTextMaxSize, 30)
        rightEditFilters.add(LengthFilter(maxSize))
        rightEditText.filters = rightEditFilters.toTypedArray()
        rightEditText.background = null
        rightEditText.minHeight = minHeight
        val paddingVertical =
            ta.getDimensionPixelSize(
                R.styleable.SuperFormView_formRightPaddingVertical,
                context.dp2px(15f)
            )
        rightEditText.setPadding(0, paddingVertical, 0, paddingVertical)
        val autoGravity = ta.getBoolean(R.styleable.SuperFormView_formRightTextAutoGravity, true)
        // 文本输入行数监听
        rightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val lineCount = rightEditText.lineCount
                Timber.i("当前行数: $lineCount")

                if (autoGravity) {
                    changeEditGravity(if (lineCount > 1) 1 else 0)
                }

                if (rightIcon != null) {
                    if (s.toString().length() > 0) {
                        rightIcon!!.visibility = View.VISIBLE
                    } else {
                        rightIcon!!.visibility = View.INVISIBLE
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        // 开启清楚文本
        val openClearText = ta.getBoolean(R.styleable.SuperFormView_formOpenClearText, false)
        if (openClearText) {
            rightIcon!!.visibility = View.INVISIBLE
            clearClickCallback()
        }

        val rightEditParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        when {
            rightPlaceTextId != 0 -> {
                rightEditParams.addRule(RelativeLayout.START_OF, rightPlaceTextId)
            }
            iconId != 0 -> {
                rightEditParams.addRule(RelativeLayout.START_OF, iconId)
            }
            else -> {
                rightEditParams.addRule(RelativeLayout.ALIGN_PARENT_END)
            }
        }
        rightEditParams.marginEnd =
            ta.getDimensionPixelSize(
                R.styleable.SuperFormView_formRightTextMargin,
                context.dp2px(10f)
            )
        rightView.addView(rightEditText, rightEditParams)

        rightEditTopView = View(context)
        rightView.addView(rightEditTopView, rightEditParams)
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

    fun rightEditTextView(): AppCompatEditText? {
        return rightEditText
    }

    fun rightIconView(): AppCompatImageView? {
        return rightIcon
    }

    /**
     * 添加右侧文字输入监听
     */
    fun addRightTextChangedListener(watcher: TextWatcher) {
        rightEditText.addTextChangedListener(watcher)
    }

    /**
     * 移除右侧文字输入监听
     */
    fun removeRightTextChangedListener(watcher: TextWatcher) {
        rightEditText.removeTextChangedListener(watcher)
    }

    fun getRightText(): String {
        return rightEditText.text.toString().invalid()
    }

    fun setClickCallback(callback: View.OnClickListener) {
        if (!editEnable()) {
            rightEditTopView.setOnClickListener(callback)
        }
    }

    fun editEnable(): Boolean {
        return rightEditText.isEnabled
    }

    private fun clearClickCallback() {
        rightIcon?.click {
            rightEditText.text = null
            rightIcon?.visibility = View.INVISIBLE
        }
    }
}