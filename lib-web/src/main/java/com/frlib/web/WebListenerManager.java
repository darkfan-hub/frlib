package com.frlib.web;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @author ???
 * @date 2017/5/13
 * @since 1.0.0
 */
public interface WebListenerManager {
    WebListenerManager setWebChromeClient(WebView webview, WebChromeClient webChromeClient);
    WebListenerManager setWebViewClient(WebView webView, WebViewClient webViewClient);
    WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener);
}
