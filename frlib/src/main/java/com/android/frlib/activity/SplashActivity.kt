package com.android.frlib.activity

import android.content.Context
import android.os.Bundle
import android.os.Environment
import com.android.frlib.R
import com.android.frlib.databinding.FrlibActivitySplshBinding
import com.frlib.basic.activity.AbstractActivity
import com.frlib.basic.download.DownloadImageService
import com.frlib.basic.ext.click
import com.frlib.basic.image.displayRadiusImage
import com.frlib.basic.immersion.ImmersionBar
import com.frlib.basic.vm.BaseViewModel
import com.frlib.utils.SysUtil
import com.frlib.utils.TimeUtil
import com.frlib.utils.ext.dp2px
import timber.log.Timber
import java.io.File

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 17:33
 * @desc app 开屏页
 */
@ImmersionBar
class SplashActivity : AbstractActivity<FrlibActivitySplshBinding, SplashVM>() {

    override fun layoutId(): Int = R.layout.frlib_activity_splsh

    override fun initView(savedInstanceState: Bundle?) {

        dataBinding.ivAvatar.displayRadiusImage(
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3378665999,2087198621&fm=26&gp=0.jpg",
            self.dp2px(4f)
        )
    }

    override fun setClickListen() {
        super.setClickListen()
        dataBinding.btTestClick.click { viewModel.downloadImage() }
    }

    override fun useDefaultPages(): Boolean = true

    override fun useSwipe(): Boolean = false
}