package com.frlib.web;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.frlib.basic.permissions.Permission;
import com.frlib.basic.permissions.PermissionUtils;
import com.frlib.basic.permissions.XXPermissions;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebView;
import timber.log.Timber;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ???
 * @date 2017/5/13
 */
public class DefaultDownloadImpl implements DownloadListener {
    /**
     * Application Context
     */
    protected Context mContext;
    protected ConcurrentHashMap<String, BaseDownloadTask> mDownloadTasks = new ConcurrentHashMap<>();
    /**
     * Activity
     */
    protected WeakReference<Activity> mActivityWeakReference = null;
    /**
     * 权限拦截
     */
    protected PermissionInterceptor mPermissionListener = null;
    /**
     * AbsAgentWebUIController
     */
    protected WeakReference<AbsAgentWebUIController> mAgentWebUIController;

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private boolean isInstallDownloader;

    protected DefaultDownloadImpl(Activity activity, WebView webView, PermissionInterceptor permissionInterceptor) {
        this.mContext = activity.getApplicationContext();
        this.mActivityWeakReference = new WeakReference<Activity>(activity);
        this.mPermissionListener = permissionInterceptor;
        this.mAgentWebUIController = new WeakReference<AbsAgentWebUIController>(AgentWebUtil.getAgentWebUIControllerByWebView(webView));
        try {
            FileDownloader.setup(this.mContext);
            isInstallDownloader = true;
        } catch (Throwable throwable) {
            Timber.e("implementation 'com.download.library:Downloader:x.x.x'");
            Timber.e(throwable);
            isInstallDownloader = false;
        }
    }


    @Override
    public void onDownloadStart(final String url, final String userAgent, final String contentDisposition, final String mimetype, final long contentLength) {
        if (!isInstallDownloader) {
            Timber.e("unable start download " + url + "; implementation 'com.download.library:Downloader:x.x.x'");
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onDownloadStartInternal(url, userAgent, contentDisposition, mimetype, contentLength);
            }
        });
    }

    protected void onDownloadStartInternal(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        if (null == mActivityWeakReference.get() || mActivityWeakReference.get().isFinishing()) {
            return;
        }
        if (null != this.mPermissionListener) {
            if (this.mPermissionListener.intercept(url, Permission.Group.STORAGE, "download")) {
                return;
            }
        }
        BaseDownloadTask resourceRequest = createResourceRequest(url);
        this.mDownloadTasks.put(url, resourceRequest);
        if (PermissionUtils.isPermissionPermanentDenied(mActivityWeakReference.get(), Arrays.asList(Permission.Group.STORAGE))) {
            Action mAction = Action.createPermissionsAction(Permission.Group.STORAGE);
            ActionActivity.setPermissionListener(getPermissionListener(url));
            ActionActivity.start(mActivityWeakReference.get(), mAction);
        } else {
            preDownload(url);
        }
    }

    protected BaseDownloadTask createResourceRequest(String url) {
        return FileDownloader.getImpl().create(url);
    }

    protected ActionActivity.PermissionListener getPermissionListener(final String url) {
        return new ActionActivity.PermissionListener() {
            @Override
            public void onGranted(Bundle extras) {
                preDownload(url);
            }

            @Override
            public void onDenied(Bundle extras) {
                if (null != mAgentWebUIController.get()) {
                    mAgentWebUIController.get().onPermissionsDeny(Permission.Group.STORAGE, "Download", "Download");
                }
                Timber.e("储存权限获取失败~");
            }
        };
    }

    protected void preDownload(String url) {
        // 移动数据
        if (AgentWebUtil.checkNetworkType(mContext) > 1) {
            showDialog(url);
            return;
        }

        performDownload(url);
    }

    protected void forceDownload(final String url) {
        performDownload(url);
    }

    protected void showDialog(final String url) {
        Activity mActivity;
        if (null == (mActivity = mActivityWeakReference.get()) || mActivity.isFinishing()) {
            return;
        }
        AbsAgentWebUIController mAgentWebUIController;
        if (null != (mAgentWebUIController = this.mAgentWebUIController.get())) {
            mAgentWebUIController.onForceDownloadAlert(url, createCallback(url));
        }
    }

    protected Handler.Callback createCallback(final String url) {
        return new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                forceDownload(url);
                return true;
            }
        };
    }

    protected void performDownload(String url) {
        try {
            Timber.e("performDownload:" + url);
            BaseDownloadTask resourceRequest = mDownloadTasks.get(url);
            resourceRequest.addHeader("Cookie", AgentWebConfig.getCookiesByUrl(url));
            taskEnqueue(resourceRequest);
        } catch (Throwable ignore) {
            Timber.e(ignore);
        }
    }

    protected void taskEnqueue(BaseDownloadTask resourceRequest) {
        resourceRequest
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        mDownloadTasks.remove(task.getUrl());
                    }
                }).start();
    }

    public static DefaultDownloadImpl create(@NonNull Activity activity,
                                             @NonNull WebView webView,
                                             @Nullable PermissionInterceptor permissionInterceptor) {
        return new DefaultDownloadImpl(activity, webView, permissionInterceptor);
    }
}
