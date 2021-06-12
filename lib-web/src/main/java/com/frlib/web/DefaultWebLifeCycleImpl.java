package com.frlib.web;

import android.os.Build;

import com.tencent.smtt.sdk.WebView;

/**
 * @author ???
 * @date 2017/6/3
 * @since 2.0.0
 */
public class DefaultWebLifeCycleImpl implements WebLifeCycle {
    private WebView mWebView;
    DefaultWebLifeCycleImpl(WebView webView) {
        this.mWebView = webView;
    }

    @Override
    public void onResume() {
        if (this.mWebView != null) {
            if (Build.VERSION.SDK_INT >= 11){
                this.mWebView.onResume();
            }
            this.mWebView.resumeTimers();
        }
    }

    @Override
    public void onPause() {
        if (this.mWebView != null) {
            if (Build.VERSION.SDK_INT >= 11){
                this.mWebView.onPause();
            }
            this.mWebView.pauseTimers();
        }
    }

    @Override
    public void onDestroy() {
        if(this.mWebView!=null){
            this.mWebView.resumeTimers();
        }
        AgentWebUtil.clearWebView(this.mWebView);
    }
}
