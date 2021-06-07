package com.frlib.basic.views.form

import android.content.Context
import android.content.res.TypedArray
import android.view.View

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 03/06/2021 09:47
 * @desc 表单view接口
 */
interface ISuperFormView {

    /**
     * 表单左侧view
     */
    fun formLeftView(context: Context, ta: TypedArray): View

    /**
     * 表单右侧view
     */
    fun formRightView(context: Context, ta: TypedArray): View
}