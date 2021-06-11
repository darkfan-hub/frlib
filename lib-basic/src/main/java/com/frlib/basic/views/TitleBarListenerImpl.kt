package com.frlib.basic.views

import android.app.Activity
import android.view.View

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/06/2021 02:08
 * @desc 标题栏点击事件实现类
 */
class TitleBarListenerImpl(private val activity: Activity) : TitleBar.OnTitleBarListener {
    override fun leftClick(view: View?) {
        activity.finish()
    }

    override fun doubleClick(view: View?) {
    }

    override fun search(searchWorld: String?) {
    }

    override fun voiceIconClick(view: View?) {
    }

    override fun deleteIconClick(view: View?) {
    }

    override fun rightClick(view: View?) {
    }
}