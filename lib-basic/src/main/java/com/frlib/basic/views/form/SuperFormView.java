package com.frlib.basic.views.form;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.frlib.basic.R;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 03/06/2021 00:01
 * @desc 表单view
 */
public class SuperFormView extends RelativeLayout {

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
        ISuperFormView formView;
        if (formStyle == 0) {
            formView = new BasicFormView();
        } else {
            formView = new CustomFormView();
        }

        addView(formView.formLeftView(context, ta));
        formRightView = formView.formRightView(context, ta);
        addView(formRightView);
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

    /**
     * 为整个view设置点击事件
     *
     * @param clickCallback 点击事件
     */
    public void setClickCallback(View.OnClickListener clickCallback) {
        setOnClickListener(clickCallback);
    }
}
