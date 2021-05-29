package com.frlib.basic.defaultpages

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.frlib.basic.defaultpages.state.DefaultPagesState
import com.frlib.basic.defaultpages.state.SuccessState

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 13:59
 * @desc 缺省页容器
 */
@SuppressLint("ViewConstructor")
class DefaultPagesContainer(
    context: Context,
    private val originTargetView: View,
    private val onRetry: OnRetryEventListener? = null
) : FrameLayout(context) {

    private var statePool: MutableMap<Class<out DefaultPagesState>, DefaultPagesState> =
        mutableMapOf()

    private var currentState: DefaultPagesState? = null

    private val animator: ValueAnimator by lazy {
        ValueAnimator.ofFloat(0.0f, 1.0f).apply {
            duration = 500
        }
    }

    fun initialization() {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(originTargetView, 0, layoutParams)
    }

    fun <T : DefaultPagesState> show(state: T) {
        if (childCount == 0) {
            initialization()
        }

        val currentState = findState(state)

        currentState?.let {
            if (childCount > 1) {
                removeViewAt(1)
            }

            if (currentState is SuccessState) {
                originTargetView.visibility = View.VISIBLE
                originTargetView.doAnimator()
            } else {
                originTargetView.visibility = View.GONE

                val stateView = state.defaultPageView()
                stateView?.apply {
                    setOnClickListener {
                        onRetry?.onRetry()
                    }
                    addView(this)
                    doAnimator()
                }
            }
        }
    }

    private fun <T : DefaultPagesState> findState(state: T): DefaultPagesState? {
        val doState = if (statePool.containsKey(state.javaClass)) {
            statePool[state.javaClass]
        } else {
            statePool[state.javaClass] = state
            state
        }

        return if (doState == currentState) {
            null
        } else {
            currentState = doState
            currentState
        }
    }

    private fun View.doAnimator() {
        this.clearAnimation()
        animator.addUpdateListener {
            this.alpha = it.animatedValue as Float
        }
        animator.start()
    }

    interface OnRetryEventListener {

        fun onRetry()
    }
}