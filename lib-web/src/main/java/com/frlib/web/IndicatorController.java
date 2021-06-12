package com.frlib.web;

import com.tencent.smtt.sdk.WebView;

/**
 * @author ???
 * @update 4.0.0
 * @since 1.0.0
 */
public interface IndicatorController {

    void progress(WebView v, int newProgress);

    BaseIndicatorSpec offerIndicator();

    void showIndicator();

    void setProgress(int newProgress);

    void finish();
}
