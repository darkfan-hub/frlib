package com.frlib.web;

/**
 * @author ???
 * @since 3.0.0
 */
public interface PermissionInterceptor {
    boolean intercept(String url, String[] permissions, String action);
}
