package com.frlib.web;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebView;

/**
 * @author ???
 * @since 1.0.0
 */
public class AgentWebSettingsImpl extends AbsAgentWebSettings {
    private AgentWeb mAgentWeb;

    @Override
    protected void bindAgentWebSupport(AgentWeb agentWeb) {
        this.mAgentWeb = agentWeb;
    }

    @Override
    public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
        if (downloadListener == null) {
            downloadListener = DefaultDownloadImpl.create(mAgentWeb.getActivity(), webView, mAgentWeb.getPermissionInterceptor());
        }
        return super.setDownloader(webView, downloadListener);
    }

    /**
     * Copy from com.blankj.utilcode.util.ActivityUtils#getActivityByView
     */
    private Activity getActivityByContext(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
