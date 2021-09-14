package com.frlib.basic.views.form;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.frlib.basic.R;
import com.frlib.utils.ext.ResourcesExtKt;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 03/06/2021 00:01
 * @desc 表单view
 */
public class SuperFormView extends RelativeLayout {

    /**
     * 表单view
     */
    private final ISuperFormView formView;
    /**
     * 表单右侧视图
     */
    private final View formRightView;

    public SuperFormView(@NonNull Context context) {
        this(context, null);
    }

    public SuperFormView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperFormView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SuperFormView);

        // 最小高度, 默认44
        int minHeight = ta.getDimensionPixelSize(R.styleable.SuperFormView_formMinHeight, 54);
        setMinimumHeight(minHeight);

        // 背景颜色, 默认白色
        setBackgroundColor(ta.getColor(R.styleable.SuperFormView_formBackground, Color.WHITE));

        int formStyle = ta.getInt(R.styleable.SuperFormView_formStyle, 0);
        // 表单view
        if (formStyle == 0) {
            formView = new BasicFormView();
        } else {
            formView = new CustomFormView();
        }

        addView(formView.formLeftView(context, ta));
        formRightView = formView.formRightView(context, ta);
        addView(formRightView);

        // 下划线
        boolean showBottomLine = ta.getBoolean(R.styleable.SuperFormView_formShowBottomLine, true);
        if (showBottomLine) {
            View lineView = new View(context);
            int lineColor = ta.getColor(R.styleable.SuperFormView_formBottomLineColor, ResourcesExtKt.color(context, R.color.color_f4));
            lineView.setBackgroundColor(lineColor);
            int lineHeight = ta.getDimensionPixelSize(R.styleable.SuperFormView_formBottomLineHeight, ResourcesExtKt.dp2px(context, 1f));
            LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT, lineHeight);
            lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            int leftMargin = ta.getDimensionPixelSize(R.styleable.SuperFormView_formBottomLineMarginLeft, 0);
            int rightMargin = ta.getDimensionPixelSize(R.styleable.SuperFormView_formBottomLineMarginRight, 0);
            lineParams.setMarginStart(leftMargin);
            lineParams.setMarginEnd(rightMargin);
            lineView.setLayoutParams(lineParams);
            addView(lineView);
        }
        ta.recycle();
    }

    /**
     * 表单右侧视图
     *
     * @return view
     */
    public View formRightView() {
        return formRightView;
    }

    public @Nullable
    AppCompatEditText formRightEditText() {
        if (formView instanceof BasicFormView) {
            return ((BasicFormView) formView).rightEditTextView();
        } else {
            return null;
        }
    }

    /**
     * 设置左侧文字
     *
     * @param text 文字
     */
    public void setLeftText(String text) {
        if (formView instanceof BasicFormView) {
            ((BasicFormView) formView).setLeftText(text);
        }
    }

    /**
     * 设置左侧icon显示状态
     *
     * @param visibility 显示状态
     */
    public void setLeftIconVisibility(int visibility) {
        if (formView instanceof BasicFormView) {
            ((BasicFormView) formView).setLeftIconVisibility(visibility);
        }
    }

    /**
     * 获取右侧文字
     *
     * @return 右侧文字
     */
    public String getRightText() {
        if (formView instanceof BasicFormView) {
            return ((BasicFormView) formView).getRightText();
        }

        return "";
    }

    /**
     * 设置右侧文字
     *
     * @param text 文字
     */
    public void setRightText(String text) {
        if (formView instanceof BasicFormView) {
            ((BasicFormView) formView).setRightText(text);
        }
    }

    /**
     * 设置右侧提示文字
     *
     * @param text 文字
     */
    public void setRightHintText(String text) {
        if (formView instanceof BasicFormView) {
            ((BasicFormView) formView).setRightHintText(text);
        }
    }

    /**
     * 添加右侧文字输入监听
     */
    public void addRightTextChangedListener(TextWatcher watcher) {
        if (formView instanceof BasicFormView) {
            ((BasicFormView) formView).addRightTextChangedListener(watcher);
        }
    }

    /**
     * 移除右侧文字输入监听
     */
    public void removeRightTextChangedListener(TextWatcher watcher) {
        if (formView instanceof BasicFormView) {
            ((BasicFormView) formView).removeRightTextChangedListener(watcher);
        }
    }

    /**
     * 为整个view设置点击事件
     *
     * @param clickCallback 点击事件
     */
    public void setClickCallback(View.OnClickListener clickCallback) {
        if (formView instanceof BasicFormView) {
            ((BasicFormView) formView).setClickCallback(clickCallback);
        }
        setOnClickListener(clickCallback);
    }
}
