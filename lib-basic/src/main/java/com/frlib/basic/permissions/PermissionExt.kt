package com.frlib.basic.permissions

import android.content.Context
import androidx.fragment.app.Fragment

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/05/2021 17:31
 * @desc 权限申请扩展类
 */

fun Context.storage(callback: IPermissionCallback) {
    XXPermissions.with(this)
        .permission(Permission.MANAGE_EXTERNAL_STORAGE)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                if (all) {
                    callback.onGranted()
                } else {
                    storage(callback)
                }
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                if (never) {
                    callback.onDenied()
                } else {
                    storage(callback)
                }
            }
        })
}

fun Fragment.storage(callback: IPermissionCallback) {
    XXPermissions.with(this)
        .permission(Permission.MANAGE_EXTERNAL_STORAGE)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                if (all) {
                    callback.onGranted()
                } else {
                    storage(callback)
                }
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                if (never) {
                    callback.onDenied()
                } else {
                    storage(callback)
                }
            }
        })
}

fun Context.camera(callback: IPermissionCallback) {
    XXPermissions.with(this)
        .permission(Permission.CAMERA)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                if (all) {
                    callback.onGranted()
                } else {
                    storage(callback)
                }
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                if (never) {
                    callback.onDenied()
                } else {
                    storage(callback)
                }
            }
        })
}

fun Fragment.camera(callback: IPermissionCallback) {
    XXPermissions.with(this)
        .permission(Permission.CAMERA)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                if (all) {
                    callback.onGranted()
                } else {
                    storage(callback)
                }
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                if (never) {
                    callback.onDenied()
                } else {
                    storage(callback)
                }
            }
        })
}

fun Context.record(callback: IPermissionCallback) {
    XXPermissions.with(this)
        .permission(Permission.RECORD_AUDIO)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                if (all) {
                    callback.onGranted()
                } else {
                    storage(callback)
                }
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                if (never) {
                    callback.onDenied()
                } else {
                    storage(callback)
                }
            }
        })
}

fun Fragment.record(callback: IPermissionCallback) {
    XXPermissions.with(this)
        .permission(Permission.RECORD_AUDIO)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                if (all) {
                    callback.onGranted()
                } else {
                    storage(callback)
                }
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                if (never) {
                    callback.onDenied()
                } else {
                    storage(callback)
                }
            }
        })
}