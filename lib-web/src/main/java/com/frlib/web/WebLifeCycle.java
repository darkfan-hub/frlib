package com.frlib.web;

/**
 * @author ???
 * @date 2017/5/30
 * @since 1.0.0
 */
public interface WebLifeCycle {
    void onResume();
    void onPause();
    void onDestroy();
}
