package com.frlib.web;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import timber.log.Timber;

import java.io.File;

/**
 * @author ???
 * @since 1.0.0
 */
public class AgentWebConfig {

    static final String FILE_CACHE_PATH = "WebCache";
    static final String AGENTWEB_CACHE_PATCH = File.separator + "WebCache";
    /**
     * 缓存路径
     */
    static String AGENTWEB_FILE_PATH;
    /**
     * DEBUG 模式 ， 如果需要查看日志请设置为 true
     */
    public static boolean DEBUG = false;
    /**
     * 当前操作系统是否低于 KITKAT
     */
    static final boolean IS_KITKAT_OR_BELOW_KITKAT = Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
    /**
     * 默认 WebView  类型 。
     */
    public static final int WEBVIEW_DEFAULT_TYPE = 1;
    /**
     * 使用 AgentWebView
     */
    public static final int WEBVIEW_AGENTWEB_SAFE_TYPE = 2;
    /**
     * 自定义 WebView
     */
    public static final int WEBVIEW_CUSTOM_TYPE = 3;
    private static volatile boolean IS_INITIALIZED = false;
    public static final String AGENTWEB_NAME = "AgentWeb";
    /**
     * AgentWeb 的版本
     */
    public static final String AGENTWEB_VERSION = AGENTWEB_NAME + "/" + BuildConfig.VERSION_NAME;
    /**
     * 通过JS获取的文件大小， 这里限制最大为5MB ，太大会抛出 OutOfMemoryError
     */
    public static int MAX_FILE_LENGTH = 1024 * 1024 * 5;

    //获取Cookie
    public static String getCookiesByUrl(String url) {
        return CookieManager.getInstance() == null ? null : CookieManager.getInstance().getCookie(url);
    }

    public static void debug() {
        DEBUG = true;
        WebView.setWebContentsDebuggingEnabled(true);
    }

    /**
     * 删除所有已经过期的 Cookies
     */
    public static void removeExpiredCookies() {
        CookieManager mCookieManager = null;
        // 同步清除
        if ((mCookieManager = CookieManager.getInstance()) != null) {
            mCookieManager.removeExpiredCookie();
            toSyncCookies();
        }
    }

    /**
     * 删除所有 Cookies
     */
    public static void removeAllCookies() {
        removeAllCookies(null);
    }

    // 解决兼容 Android 4.4 java.lang.NoSuchMethodError: android.webkit.CookieManager.removeSessionCookies
    public static void removeSessionCookies() {
        removeSessionCookies(null);
    }

    /**
     * 同步cookie
     *
     * @param url
     * @param cookies
     */
    public static void syncCookie(String url, String cookies) {
        CookieManager mCookieManager = CookieManager.getInstance();
        if (mCookieManager != null) {
            mCookieManager.setCookie(url, cookies);
            toSyncCookies();
        }
    }

    public static void removeSessionCookies(ValueCallback<Boolean> callback) {
        if (callback == null) {
            callback = getDefaultIgnoreCallback();
        }
        if (CookieManager.getInstance() == null) {
            callback.onReceiveValue(new Boolean(false));
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeSessionCookie();
            toSyncCookies();
            callback.onReceiveValue(new Boolean(true));
            return;
        }
        CookieManager.getInstance().removeSessionCookies(callback);
        toSyncCookies();
    }

    /**
     * @param context
     * @return WebView 的缓存路径
     */
    public static String getCachePath(Context context) {
        return context.getCacheDir().getAbsolutePath() + AGENTWEB_CACHE_PATCH;
    }

    /**
     * @param context
     * @return AgentWeb 缓存路径
     */
    public static String getExternalCachePath(Context context) {
        return AgentWebUtil.getAgentWebFilePath(context);
    }

    // Android  4.4  NoSuchMethodError: android.webkit.CookieManager.removeAllCookies
    public static void removeAllCookies(@Nullable ValueCallback<Boolean> callback) {
        if (callback == null) {
            callback = getDefaultIgnoreCallback();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookie();
            toSyncCookies();
            callback.onReceiveValue(!CookieManager.getInstance().hasCookies());
            return;
        }
        CookieManager.getInstance().removeAllCookies(callback);
        toSyncCookies();
    }

    /**
     * 清空缓存
     *
     * @param context
     */
    public static synchronized void clearDiskCache(Context context) {
        try {
            AgentWebUtil.clearCacheFolder(new File(getCachePath(context)), 0);
            String path = getExternalCachePath(context);
            if (!TextUtils.isEmpty(path)) {
                File mFile = new File(path);
                AgentWebUtil.clearCacheFolder(mFile, 0);
            }
        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    static synchronized void initCookiesManager(Context context) {
        if (!IS_INITIALIZED) {
            createCookiesSyncInstance(context);
            IS_INITIALIZED = true;
        }
    }

    private static void createCookiesSyncInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context);
        }
    }

    private static void toSyncCookies() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
            return;
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                CookieManager.getInstance().flush();
            }
        });
    }

    static String getDatabasesCachePath(Context context) {
        return context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
    }

    private static ValueCallback<Boolean> getDefaultIgnoreCallback() {
        return new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean ignore) {
                Timber.i("removeExpiredCookies:" + ignore);
            }
        };
    }
}
