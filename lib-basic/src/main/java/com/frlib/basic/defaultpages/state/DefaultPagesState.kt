package com.frlib.basic.defaultpages.state

import android.view.View

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 13:45
 * @desc 缺省页状态
 */
abstract class DefaultPagesState {

    private var defaultView: View? = null

    open fun invokeView(view: View) {
        defaultView = view
    }

    open fun defaultPageView(): View? = defaultView
}