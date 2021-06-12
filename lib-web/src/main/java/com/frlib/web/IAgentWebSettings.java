package com.frlib.web;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * @author ???
 * @since 1.0.0
 */

public interface IAgentWebSettings<T extends WebSettings> {

    IAgentWebSettings toSetting(WebView webView);

    T getWebSettings();

}
