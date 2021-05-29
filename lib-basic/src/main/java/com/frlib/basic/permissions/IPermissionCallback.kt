package com.frlib.basic.permissions

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 01/02/2021 17:52
 * @desc 权限请求结果回调接口
 */
interface IPermissionCallback {

    /**
     * 所有权限被同意授予时回调
     */
    fun onGranted()

    /**
     * 有被永久拒绝的权限
     */
    fun onDenied()
}