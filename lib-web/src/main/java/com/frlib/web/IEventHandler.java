package com.frlib.web;

import android.view.KeyEvent;

/**
 * @author ???
 * @since 1.0.0
 */
public interface IEventHandler {

    boolean onKeyDown(int keyCode, KeyEvent event);

    boolean back();
}
