package com.frlib.web;

import android.os.Build;
import android.webkit.JavascriptInterface;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author ???
 * @date 2017/5/13
 * @since 1.0.0
 */
public abstract class JsBaseInterfaceHolder implements JsInterfaceHolder {

    private AgentWeb.SecurityType mSecurityType;
    private WebCreator mWebCreator;

    protected JsBaseInterfaceHolder(WebCreator webCreator, AgentWeb.SecurityType securityType) {
        this.mSecurityType = securityType;
        this.mWebCreator = webCreator;
    }

    @Override
    public boolean checkObject(Object v) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return true;
        }
        if (mWebCreator.getWebViewType() == AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE) {
            return true;
        }
        boolean tag = false;
        Class clazz = v.getClass();
        Method[] mMethods = clazz.getMethods();
        for (Method mMethod : mMethods) {
            Annotation[] mAnnotations = mMethod.getAnnotations();
            for (Annotation mAnnotation : mAnnotations) {
                if (mAnnotation instanceof JavascriptInterface) {
                    tag = true;
                    break;
                }
            }
            if (tag) {
                break;
            }
        }
        return tag;
    }

    protected boolean checkSecurity() {
        return mSecurityType != AgentWeb.SecurityType.STRICT_CHECK
                ? true : mWebCreator.getWebViewType() == AgentWebConfig.WEBVIEW_AGENTWEB_SAFE_TYPE
                ? true : Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
}
