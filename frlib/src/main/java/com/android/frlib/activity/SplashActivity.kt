package com.android.frlib.activity

import android.os.Bundle
import com.android.frlib.R
import com.android.frlib.databinding.FrlibActivitySplshBinding
import com.frlib.basic.activity.AbstractActivity
import com.frlib.basic.defaultpages.Pages
import com.frlib.basic.immersion.ImmersionBar
import com.frlib.basic.vm.BaseViewModel

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 17:33
 * @desc app 开屏页
 */
@ImmersionBar
class SplashActivity : AbstractActivity<FrlibActivitySplshBinding, BaseViewModel>() {

    override fun layoutId(): Int = R.layout.frlib_activity_splsh

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
        super.initData()
        viewModel.defaultPages(Pages.EMPTY)
        viewModel.toast("123")
        viewModel.showLoading()
    }

    override fun useDefaultPages(): Boolean = false

    override fun useSwipe(): Boolean = false
}