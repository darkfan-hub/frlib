package com.frlib.web;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.tencent.smtt.sdk.ValueCallback;

/**
 * @author ???
 * @date 2017/5/29
 * @since 1.0.0
 */
public interface QuickCallJs {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    void quickCallJs(String method, ValueCallback<String> callback, String... params);

    void quickCallJs(String method, String... params);

    void quickCallJs(String method);
}
