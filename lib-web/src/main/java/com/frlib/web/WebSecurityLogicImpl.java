package com.frlib.web;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;

import com.tencent.smtt.sdk.WebView;

/**
 * @author ???
 */
public class WebSecurityLogicImpl implements WebSecurityCheckLogic {
    private int webviewType;

    public static WebSecurityLogicImpl getInstance(int webViewType) {
        return new WebSecurityLogicImpl(webViewType);
    }

    public WebSecurityLogicImpl(int webViewType) {
        this.webviewType = webViewType;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void dealHoneyComb(WebView view) {
        if (Build.VERSION_CODES.HONEYCOMB > Build.VERSION.SDK_INT || Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return;
        }
        view.removeJavascriptInterface("searchBoxJavaBridge_");
        view.removeJavascriptInterface("accessibility");
        view.removeJavascriptInterface("accessibilityTraversal");
    }

    @Override
    public void dealJsInterface(ArrayMap<String, Object> objects, AgentWeb.SecurityType securityType) {
        if (securityType == AgentWeb.SecurityType.STRICT_CHECK
                && this.webviewType != AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            objects.clear();
            objects = null;
            System.gc();
        }
    }
}
