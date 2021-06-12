package com.frlib.web;

import com.tencent.smtt.sdk.WebView;

import java.util.Map;
import java.util.Set;

/**
 * @author ???
 * @date 2017/5/13
 * @since 1.0.0
 */
public class JsInterfaceHolderImpl extends JsBaseInterfaceHolder {

    private WebCreator mWebCreator;
    private AgentWeb.SecurityType mSecurityType;
    private WebView mWebView;

    static JsInterfaceHolderImpl getJsInterfaceHolder(WebCreator webCreator, AgentWeb.SecurityType securityType) {
        return new JsInterfaceHolderImpl(webCreator, securityType);
    }

    JsInterfaceHolderImpl(WebCreator webCreator, AgentWeb.SecurityType securityType) {
        super(webCreator, securityType);
        this.mWebCreator = webCreator;
        this.mWebView = mWebCreator.getWebView();
        this.mSecurityType = securityType;
    }

    @Override
    public JsInterfaceHolder addJavaObjects(Map<String, Object> maps) {
        if (!checkSecurity()) {
            return this;
        }
        Set<Map.Entry<String, Object>> sets = maps.entrySet();
        for (Map.Entry<String, Object> mEntry : sets) {
            Object v = mEntry.getValue();
            boolean t = checkObject(v);
            if (!t) {
                throw new JsInterfaceObjectException("This object has not offer method javascript to call ,please check addJavascriptInterface annotation was be added");
            } else {
                addJavaObjectDirect(mEntry.getKey(), v);
            }
        }
        return this;
    }

    @Override
    public JsInterfaceHolder addJavaObject(String k, Object v) {
        if (!checkSecurity()) {
            return this;
        }
        boolean t = checkObject(v);
        if (!t) {
            throw new JsInterfaceObjectException("this object has not offer method javascript to call , please check addJavascriptInterface annotation was be added");
        } else {
            addJavaObjectDirect(k, v);
        }
        return this;
    }

    private JsInterfaceHolder addJavaObjectDirect(String k, Object v) {
        this.mWebView.addJavascriptInterface(v, k);
        return this;
    }

}
