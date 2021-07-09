package com.frlib.web;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.frlib.basic.permissions.Permission;
import com.frlib.basic.permissions.PermissionUtils;
import com.tencent.smtt.export.external.interfaces.*;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import timber.log.Timber;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * @author ???
 * @since 1.0.0
 */
public class DefaultChromeClient extends MiddlewareWebChromeBase {
    /**
     * 标志位
     */
    public static final int FROM_CODE_INTENTION = 0x18;
    /**
     * 标识当前是获取定位权限
     */
    public static final int FROM_CODE_INTENTION_LOCATION = FROM_CODE_INTENTION << 2;
    /**
     * Activity
     */
    private WeakReference<Activity> mActivityWeakReference = null;
    /**
     * WebChromeClient
     */
    private WebChromeClient mWebChromeClient;
    /**
     * 包装Flag
     */
    private boolean mIsWrapper = false;
    /**
     * Video 处理类
     */
    private IVideo mIVideo;
    /**
     * PermissionInterceptor 权限拦截器
     */
    private PermissionInterceptor mPermissionInterceptor;
    /**
     * 当前 WebView
     */
    private WebView mWebView;
    /**
     * Web端触发的定位 mOrigin
     */
    private String mOrigin = null;
    /**
     * Web 端触发的定位 Callback 回调成功，或者失败
     */
    private GeolocationPermissionsCallback mCallback = null;
    /**
     * AbsAgentWebUIController
     */
    private WeakReference<AbsAgentWebUIController> mAgentWebUIController = null;
    /**
     * IndicatorController 进度条控制器
     */
    private IndicatorController mIndicatorController;
    /**
     * 文件选择器
     */
    private Object mFileChooser;
    private ActionActivity.PermissionListener mPermissionListener = new ActionActivity.PermissionListener() {
        @Override
        public void onGranted(Bundle extras) {
            if (extras.getInt(ActionActivity.KEY_FROM_INTENTION) == FROM_CODE_INTENTION_LOCATION) {
                if (mCallback != null) {
                    mCallback.invoke(mOrigin, true, false);
                    mCallback = null;
                    mOrigin = null;
                }
            }
        }

        @Override
        public void onDenied(Bundle extras) {
            mAgentWebUIController
                    .get()
                    .onPermissionsDeny(new String[]{Permission.ACCESS_FINE_LOCATION}, "Location", "Location");
        }
    };


    DefaultChromeClient(Activity activity,
                        IndicatorController indicatorController,
                        WebChromeClient chromeClient,
                        @Nullable IVideo iVideo,
                        PermissionInterceptor permissionInterceptor, WebView webView) {
        super(chromeClient);
        this.mIndicatorController = indicatorController;
        mIsWrapper = chromeClient != null ? true : false;
        this.mWebChromeClient = chromeClient;
        mActivityWeakReference = new WeakReference<>(activity);
        this.mIVideo = iVideo;
        this.mPermissionInterceptor = permissionInterceptor;
        this.mWebView = webView;
        mAgentWebUIController = new WeakReference<>(AgentWebUtil.getAgentWebUIControllerByWebView(webView));
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (mIndicatorController != null) {
            mIndicatorController.progress(view, newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (mIsWrapper) {
            super.onReceivedTitle(view, title);
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        if (mAgentWebUIController.get() != null) {
            mAgentWebUIController.get().onJsAlert(view, url, message);
        }
        result.confirm();
        return true;
    }

    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissionsCallback callback) {
        onGeolocationPermissionsShowPromptInternal(origin, callback);
    }

    @Override
    public void onGeolocationPermissionsHidePrompt() {
        super.onGeolocationPermissionsHidePrompt();
    }

    private void onGeolocationPermissionsShowPromptInternal(String origin, GeolocationPermissionsCallback callback) {
        if (mPermissionInterceptor != null) {
            if (mPermissionInterceptor.intercept(this.mWebView.getUrl(), new String[]{Permission.ACCESS_FINE_LOCATION}, "location")) {
                callback.invoke(origin, false, false);
                return;
            }
        }
        Activity mActivity = mActivityWeakReference.get();
        if (mActivity == null) {
            callback.invoke(origin, false, false);
            return;
        }
        /*if (PermissionUtils.isGrantedPermission(mActivity, Permission.ACCESS_FINE_LOCATION)) {
            Timber.i("onGeolocationPermissionsShowPromptInternal:" + true);
            callback.invoke(origin, true, false);
        } else {
            Action mAction = Action.createPermissionsAction(new String[]{Permission.ACCESS_FINE_LOCATION});
            mAction.setFromIntention(FROM_CODE_INTENTION_LOCATION);
            ActionActivity.setPermissionListener(mPermissionListener);
            this.mCallback = callback;
            this.mOrigin = origin;
            ActionActivity.start(mActivity, mAction);
        }*/
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        try {
            if (this.mAgentWebUIController.get() != null) {
                this.mAgentWebUIController.get().onJsPrompt(mWebView, url, message, defaultValue, result);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        if (mAgentWebUIController.get() != null) {
            mAgentWebUIController.get().onJsConfirm(view, url, message, result);
        }
        return true;
    }


    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(totalQuota * 2);
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(requiredStorage * 2);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        Timber.i("openFileChooser>=5.0");
        return openFileChooserAboveL(webView, filePathCallback, fileChooserParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean openFileChooserAboveL(WebView webView, ValueCallback<Uri[]> valueCallbacks, FileChooserParams fileChooserParams) {
        Timber.i("fileChooserParams:" + fileChooserParams.getAcceptTypes() + "  getTitle:" + fileChooserParams.getTitle() + " accept:" + Arrays.toString(fileChooserParams.getAcceptTypes()) + " length:" + fileChooserParams.getAcceptTypes().length + "  isCaptureEnabled:" + fileChooserParams.isCaptureEnabled() + "  " + fileChooserParams.getFilenameHint() + "  intent:" + fileChooserParams.createIntent().toString() + "   mode:" + fileChooserParams.getMode());
        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity == null || mActivity.isFinishing()) {
            return false;
        }
        return AgentWebUtil.showFileChooserCompat(mActivity,
                mWebView,
                valueCallbacks,
                fileChooserParams,
                this.mPermissionInterceptor,
                null,
                null,
                null
        );
    }

    /**
     * Android  >= 4.1
     *
     * @param uploadFile ValueCallback ,  File URI callback
     * @param acceptType
     * @param capture
     */
    @Override
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        /*believe me , i never want to do this */
        Timber.i("openFileChooser>=4.1");
        createAndOpenCommonFileChooser(uploadFile, acceptType);
    }

    //  Android < 3.0
    @Override
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        Timber.i("openFileChooser<3.0");
        createAndOpenCommonFileChooser(valueCallback, "*/*");
    }

    //  Android  >= 3.0
    @Override
    public void openFileChooser(ValueCallback valueCallback, String acceptType) {
        Timber.i("openFileChooser>3.0");
        createAndOpenCommonFileChooser(valueCallback, acceptType);
    }


    private void createAndOpenCommonFileChooser(ValueCallback valueCallback, String mimeType) {
        Activity mActivity = this.mActivityWeakReference.get();
        if (mActivity == null || mActivity.isFinishing()) {
            valueCallback.onReceiveValue(new Object());
            return;
        }
        AgentWebUtil.showFileChooserCompat(mActivity,
                mWebView,
                null,
                null,
                this.mPermissionInterceptor,
                valueCallback,
                mimeType,
                null
        );
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        super.onConsoleMessage(consoleMessage);
        return true;
    }

    @Override
    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
        if (mIVideo != null) {
            mIVideo.onShowCustomView(view, callback);
        }
    }

    @Override
    public void onHideCustomView() {
        if (mIVideo != null) {
            mIVideo.onHideCustomView();
        }
    }
}
