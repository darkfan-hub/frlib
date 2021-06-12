package com.frlib.web;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.smtt.sdk.WebView;

/**
 * @author ???
 * @date 2017/7/1
 * @update 4.0.0
 * @since 1.0.0
 */
public interface IWebLayout<T extends WebView, V extends ViewGroup> {

    /**
     * @return WebView 的父控件
     */
    @NonNull
    V getLayout();

    /**
     * @return 返回 WebView  或 WebView 的子View ，返回null AgentWeb 内部会创建适当 WebView
     */
    @Nullable
    T getWebView();
}
