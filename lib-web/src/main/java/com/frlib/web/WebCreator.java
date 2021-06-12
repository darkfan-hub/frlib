package com.frlib.web;

import android.widget.FrameLayout;

import com.tencent.smtt.sdk.WebView;

/**
 * @author ???
 * @since 1.0.0
 */
public interface WebCreator extends IWebIndicator {
    WebCreator create();

    WebView getWebView();

    FrameLayout getWebParentLayout();

    int getWebViewType();
}
