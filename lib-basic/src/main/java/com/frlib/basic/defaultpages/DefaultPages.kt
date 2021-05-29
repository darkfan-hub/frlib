package com.frlib.basic.defaultpages

import android.app.Activity
import android.view.View
import android.view.ViewGroup

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 13:44
 * @desc 缺省页
 */
object DefaultPages {

    /**
     * 实现原理
     * 1.根据目标view在父view中的位置索引,移除原目标view,
     * 2.将MultiStateContainer添加到原view的索引处
     * 3.MultiStateContainer 的 layoutParams 是原目标View的 layoutParams
     */
    @JvmStatic
    fun bindDefaultPages(
        targetView: View
    ): DefaultPagesContainer {
        return bindDefaultPages(targetView, null)
    }

    @JvmStatic
    fun bindDefaultPages(
        targetView: View,
        onRetryEventListener: DefaultPagesContainer.OnRetryEventListener? = null
    ): DefaultPagesContainer {
        val parent = targetView.parent as ViewGroup?
        var targetViewIndex = 0
        val multiStateContainer =
            DefaultPagesContainer(targetView.context, targetView, onRetryEventListener)
        parent?.let { targetViewParent ->
            for (i in 0 until targetViewParent.childCount) {
                if (targetViewParent.getChildAt(i) == targetView) {
                    targetViewIndex = i
                    break
                }
            }
            targetViewParent.removeView(targetView)
            targetViewParent.addView(multiStateContainer, targetViewIndex, targetView.layoutParams)
        }
        multiStateContainer.initialization()
        return multiStateContainer
    }

    /**
     * 实现原理
     * 1. android.R.id.content 是Activity setContentView 内容的父view
     * 2. 在这个view中移除原本要添加的contentView
     * 3. 将MultiStateContainer设置为 content的子View  MultiStateContainer中持有原有的Activity setContentView
     */
    @JvmStatic
    fun bindDefaultPages(
        activity: Activity
    ): DefaultPagesContainer {
        return bindDefaultPages(activity, null)
    }

    @JvmStatic
    fun bindDefaultPages(
        activity: Activity,
        onRetryEventListener: DefaultPagesContainer.OnRetryEventListener? = null
    ): DefaultPagesContainer {
        val targetView = activity.findViewById<ViewGroup>(android.R.id.content)
        val targetViewIndex = 0
        val oldContent: View = targetView.getChildAt(targetViewIndex)
        targetView.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val multiStateContainer =
            DefaultPagesContainer(oldContent.context, oldContent, onRetryEventListener)
        targetView.addView(multiStateContainer, targetViewIndex, oldLayoutParams)
        multiStateContainer.initialization()
        return multiStateContainer
    }
}