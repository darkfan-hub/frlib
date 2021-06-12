package com.frlib.web;

import java.util.Map;

/**
 * @author ???
 * @date 2017/5/13
 * @since 1.0.0
 */
public interface JsInterfaceHolder {

    JsInterfaceHolder addJavaObjects(Map<String, Object> maps);

    JsInterfaceHolder addJavaObject(String k, Object v);

    boolean checkObject(Object v);
}
