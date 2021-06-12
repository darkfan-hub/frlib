package com.frlib.web.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.FrameLayout
import com.frlib.basic.fragment.AbstractFragment
import com.frlib.basic.vm.BaseViewModel
import com.frlib.web.*
import com.frlib.web.databinding.WebFragmentWebpageBinding
import com.tencent.smtt.sdk.WebView
import timber.log.Timber

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 12/05/2021 15:45
 * @desc 网页fragment
 */
class WebPageFragment : AbstractFragment<WebFragmentWebpageBinding, BaseViewModel>() {

    companion object {
        fun newInstances(
            url: String,
            indicatorColor: Int = -1
        ): WebPageFragment {
            val fragment = WebPageFragment()
            val args = Bundle()
            args.putString("url", url)
            args.putInt("indicatorColor", indicatorColor)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var agentWeb: AgentWeb
    private var indicatorColor: Int = -1

    override fun initView(savedInstanceState: Bundle?) {
        arguments?.apply {
            indicatorColor = getInt("indicatorColor")
        }

        agentWeb = AgentWeb.with(this)
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

        arguments?.apply {
            agentWeb.urlLoader.loadUrl(getString("url"))
        }
    }

    override fun initData() {
    }

    override fun onPause() {
        super.onPause()
        agentWeb.webLifeCycle.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
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
            }
        }
    }

    fun back(): Boolean {
        return agentWeb.back()
    }

    override fun layoutId(): Int = R.layout.web_fragment_webpage
}