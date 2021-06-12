package com.frlib.web;

import android.view.View;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;

/**
 * @author ???
 * @date 2017/6/10
 * @since 2.0.0
 */
public interface IVideo {

    void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback);


    void onHideCustomView();


    boolean isVideoState();
}
