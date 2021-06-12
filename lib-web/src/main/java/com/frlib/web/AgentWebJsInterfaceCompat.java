package com.frlib.web;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;
import timber.log.Timber;

import java.lang.ref.WeakReference;

/**
 * @author ???
 * @since 1.0.0
 */
public class AgentWebJsInterfaceCompat {

    private WeakReference<AgentWeb> mReference = null;
    private WeakReference<Activity> mActivityWeakReference = null;

    AgentWebJsInterfaceCompat(AgentWeb agentWeb, Activity activity) {
        mReference = new WeakReference<AgentWeb>(agentWeb);
        mActivityWeakReference = new WeakReference<Activity>(activity);
    }

    @JavascriptInterface
    public void uploadFile() {
        uploadFile("*/*");
    }

    @JavascriptInterface
    public void uploadFile(String acceptType) {
        Timber.i(acceptType + "  " + mActivityWeakReference.get() + "  " + mReference.get());
        if (mActivityWeakReference.get() != null && mReference.get() != null) {
            AgentWebUtil.showFileChooserCompat(mActivityWeakReference.get(),
                    mReference.get().getWebCreator().getWebView(),
                    null,
                    null,
                    mReference.get().getPermissionInterceptor(),
                    null,
                    acceptType,
                    new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            if (mReference.get() != null) {
                                mReference.get().getJsAccessEntrace()
                                        .quickCallJs("uploadFileResult",
                                                msg.obj instanceof String ? (String) msg.obj : null);
                            }
                            return true;
                        }
                    }
            );
        }
    }
}
