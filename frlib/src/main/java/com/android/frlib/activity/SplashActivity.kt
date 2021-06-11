package com.android.frlib.activity

import android.os.Bundle
import com.android.frlib.R
import com.android.frlib.databinding.FrlibActivitySplshBinding
import com.frlib.basic.activity.AbstractActivity
import com.frlib.basic.image.displayRadiusImage
import com.frlib.basic.immersion.ImmersionBar
import com.frlib.basic.vm.BaseViewModel
import com.frlib.utils.ext.dp2px

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 17:33
 * @desc app 开屏页
 */
@ImmersionBar
class SplashActivity : AbstractActivity<FrlibActivitySplshBinding, BaseViewModel>() {

    override fun layoutId(): Int = R.layout.frlib_activity_splsh

    override fun initView(savedInstanceState: Bundle?) {

        dataBinding.ivAvatar.displayRadiusImage(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic3.zhimg.com%2F50%2Fv2-039776976715e3c123578682809b474c_hd.jpg&refer=http%3A%2F%2Fpic3.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625989592&t=099fa49a5791baab1ad71b1972e57d85",
            self.dp2px(4f)
        )
    }

    override fun initData() {
        super.initData()
    }

    override fun useDefaultPages(): Boolean = false

    override fun useSwipe(): Boolean = false
}