package com.frlib.web;

import com.tencent.smtt.sdk.WebChromeClient;

/**
 * @author ???
 * @date 2017/12/16
 * @since 3.0.0
 */
public class MiddlewareWebChromeBase extends WebChromeClientDelegate {

    private MiddlewareWebChromeBase mMiddlewareWebChromeBase;

    protected MiddlewareWebChromeBase(WebChromeClient webChromeClient) {
        super(webChromeClient);
    }

    protected MiddlewareWebChromeBase() {
        super(null);
    }

    @Override
    final void setDelegate(WebChromeClient delegate) {
        super.setDelegate(delegate);
    }

    final MiddlewareWebChromeBase enq(MiddlewareWebChromeBase middlewareWebChromeBase) {
        setDelegate(middlewareWebChromeBase);
        this.mMiddlewareWebChromeBase = middlewareWebChromeBase;
        return this.mMiddlewareWebChromeBase;
    }


    final MiddlewareWebChromeBase next() {
        return this.mMiddlewareWebChromeBase;
    }
}
