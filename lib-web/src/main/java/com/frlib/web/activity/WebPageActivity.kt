package com.frlib.web.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.frlib.basic.activity.AbstractActivity
import com.frlib.basic.ext.statusBarStyle
import com.frlib.basic.immersion.ImmersionBar
import com.frlib.basic.views.TitleBar
import com.frlib.basic.vm.BaseViewModel
import com.frlib.web.*
import com.frlib.web.databinding.WebActivityWebpageBinding
import com.tencent.smtt.sdk.WebView
import timber.log.Timber

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 10/03/2021 16:25
 * @desc 网页activity
 */
@ImmersionBar(statusBarDarkFont = true)
class WebPageActivity : AbstractActivity<WebActivityWebpageBinding, BaseViewModel>() {

    companion object {
        fun open(context: Context, url: String) {
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putBoolean("titleVisible", true)
            start(context, bundle)
        }

        fun open(context: Context, url: String, title: String) {
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putString("title", title)
            bundle.putBoolean("titleVisible", true)
            start(context, bundle)
        }

        fun open(
            context: Context,
            url: String,
            title: String = "",
            titleVisible: Boolean = true,
            statusBarColor: String = "#FFFFFFFF",
            statusBarDarkFont: Boolean = true,
            fitsSystemWindows: Boolean = false,
            isPortrait: Boolean = true,
            indicatorColor: Int = -1
        ) {
            val bundle = Bundle()
            bundle.putString("url", url)
            bundle.putString("title", title)
            bundle.putBoolean("titleVisible", titleVisible)
            bundle.putString("statusBarColor", statusBarColor)
            bundle.putBoolean("statusBarDarkFont", statusBarDarkFont)
            bundle.putBoolean("fitsSystemWindows", fitsSystemWindows)
            bundle.putBoolean("isPortrait", isPortrait)
            bundle.putInt("indicatorColor", indicatorColor)
            start(context, bundle)
        }

        private fun start(context: Context, bundle: Bundle) {
            val intent = Intent(context, WebPageActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    private lateinit var agentWeb: AgentWeb
    private var indicatorColor: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.apply {
            val isPortrait = getBoolean("isPortrait", true)
            requestedOrientation = if (isPortrait) {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        intent.extras?.apply {
            indicatorColor = getInt("indicatorColor")
        }

        agentWeb = AgentWeb.with(self)
            .setAgentWebParent(
                dataBinding.flWebPage,
                FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator(indicatorColor, 3)
            .setAgentWebWebSettings(AbsAgentWebSettings.getInstance())
            .setWebViewClient(webViewClient())
            .setWebChromeClient(webChromeClient())
            // 严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            // 打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
            // 拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
            .interceptUnkownUrl()
            .createAgentWeb()
            .ready()
            .get()

        intent.extras?.apply {
            agentWeb.urlLoader.loadUrl(getString("url"))
        }
    }

    override fun initData() {
    }

    override fun setClickListen() {
        super.setClickListen()
        dataBinding.webTbTitle.addTitleBarListener(object : TitleBar.OnTitleBarListener {
            override fun leftClick(view: View?) {
                if (!agentWeb.back()) {
                    finish()
                }
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
        })
    }

    override fun onStart() {
        super.onStart()
        intent.extras?.apply {
            self.statusBarStyle(
                statusBarColor = getString("statusBarColor", "#FFFFFFFF"),
                statusBarDarkFont = getBoolean("statusBarDarkFont", true),
                fitsSystemWindows = getBoolean("fitsSystemWindows", false)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        agentWeb.webLifeCycle.onResume()
        intent.extras?.apply {
            if (!getBoolean("titleVisible", true)) {
                dataBinding.webTbTitle.visibility = View.GONE
            } else {
                val title = intent.getStringExtra("title")
                dataBinding.webTbTitle.setTitleText(title)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        agentWeb.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
    }

    override fun onBackPressed() {
        if (!agentWeb.back()) {
            super.onBackPressed()
        }
    }

    private fun webViewClient(): WebViewClient {
        return object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Timber.d("onPageStarted -----> $url")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Timber.d("onPageFinished -----> $url")
            }
        }
    }

    private fun webChromeClient(): WebChromeClient {
        return object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                Timber.d("onReceivedTitle -----> $title")

                dataBinding.webTbTitle.post { dataBinding.webTbTitle.setTitleText(title) }
            }
        }
    }

    override fun layoutId(): Int = R.layout.web_activity_webpage
}