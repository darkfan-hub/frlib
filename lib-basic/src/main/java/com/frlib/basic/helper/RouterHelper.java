package com.frlib.basic.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 2020/12/26
 * @desc 路由帮助类
 */
public class RouterHelper {

    private RouterHelper() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 使用 {@link ARouter} 根据 {@code path} 跳转到对应的页面, 这个方法因为没有使用 {@link Activity}跳转
     * 所以 {@link ARouter} 会自动给 {@link android.content.Intent} 加上 Intent.FLAG_ACTIVITY_NEW_TASK
     * 如果不想自动加上这个 Flag 请使用 {@link ARouter#getInstance()#navigation(Context, String)} 并传入 {@link Activity}
     *
     * @param path {@code path}
     */
    public static void navigation(String path) {
        ARouter.getInstance().build(path).navigation();
    }

    public static void navigation(String path, Bundle bundle) {
        ARouter.getInstance().build(path).with(bundle).navigation();
    }

    /**
     * 使用 {@link ARouter} 根据 {@code path} 跳转到对应的页面, 如果参数 {@code context} 传入的不是 {@link Activity}
     * {@link ARouter} 就会自动给 {@link android.content.Intent} 加上 Intent.FLAG_ACTIVITY_NEW_TASK
     * 如果不想自动加上这个 Flag 请使用 {@link Activity} 作为 {@code context} 传入
     *
     * @param context
     * @param path
     */
    public static void navigation(Context context, String path) {
        ARouter.getInstance().build(path).navigation(context);
    }

    public static void navigation(Context context, String path, Bundle bundle) {
        ARouter.getInstance().build(path).with(bundle).navigation(context);
    }

    public static <T extends IProvider> T provide(Class<T> clz, String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        IProvider provider = null;
        try {
            provider = (IProvider) ARouter.getInstance()
                    .build(path)
                    .navigation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) provider;
    }


    public static <T extends IProvider> T provide(Class<T> clz) {
        IProvider provider = null;
        try {
            provider = ARouter.getInstance().navigation(clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) provider;
    }
}