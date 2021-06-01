package com.frlib.basic.dialog

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.frlib.basic.R
import timber.log.Timber

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 02/03/2021 15:37
 * @desc 对话框基类
 */
abstract class AbstractDialog : DialogFragment() {

    /** this tag */
    protected val dialogTag: String by lazy { javaClass.simpleName }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("$dialogTag  ------>  onAttach")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Timber.i("$dialogTag  ------>  onCreateView")
        initDialog()
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("$dialogTag  ------>  onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    /**
     * 初始化对话框
     */
    open fun initDialog() {
        dialog?.window?.apply {
            // 去掉dialog默认的padding
            decorView.setPadding(dialogHorizontalMargin(), 0, dialogHorizontalMargin(), 0)
            val params = attributes
            params.width = dialogWidth()
            params.height = dialogHeight()
            params.gravity = dialogGravity()
            attributes = params
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.i("$dialogTag  ------>  onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("$dialogTag  ------>  onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.i("$dialogTag  ------>  onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("$dialogTag  ------>  onDestroy")
    }

    /**
     * 显示对话框
     */
    fun showDialog(fm: FragmentManager) {
        setStyle(STYLE_NORMAL, dialogStyle())
        show(fm, dialogTag)
    }

    /**
     * 隐藏对话框
     */
    fun dismissDialog() {
        dismiss()
    }

    /**
     * 对话框位置
     */
    open fun dialogGravity(): Int {
        return Gravity.BOTTOM
    }

    /**
     * 对话框宽
     */
    open fun dialogWidth(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT
    }

    /**
     * 对话框水平外间距
     */
    open fun dialogHorizontalMargin(): Int {
        return 0
    }

    /**
     * 对话框高
     */
    open fun dialogHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    /**
     * 对话框动画
     */
    open fun dialogStyle(): Int {
        return R.style.anim_bottom_to_bottom
    }

    abstract fun layoutId(): Int

    abstract fun initView(view: View)
}