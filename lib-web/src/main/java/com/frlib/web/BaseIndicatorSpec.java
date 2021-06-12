package com.frlib.web;

/**
 * @author ???
 * @since 1.0.0
 */
public interface BaseIndicatorSpec {

    void show();

    void hide();

    void reset();

    void setProgress(int newProgress);
}
