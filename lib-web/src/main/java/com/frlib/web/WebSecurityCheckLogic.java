package com.frlib.web;

import android.util.ArrayMap;

import com.tencent.smtt.sdk.WebView;

/**
 * @author ???
 */
public interface WebSecurityCheckLogic {
    void dealHoneyComb(WebView view);
    void dealJsInterface(ArrayMap<String, Object> objects, AgentWeb.SecurityType securityType);
}
