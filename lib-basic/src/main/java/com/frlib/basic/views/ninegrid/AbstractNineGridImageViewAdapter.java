package com.frlib.basic.views.ninegrid;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @desc 九宫格图片适配器
 * @since 10/12/2020 14:55
 */
public abstract class AbstractNineGridImageViewAdapter<T> {
    /**
     * 显示图片
     *
     * @param context   上下文对象
     * @param imageView 图片view
     * @param t         图片源
     */
    protected abstract void onDisplayImage(Context context, ImageView imageView, T t);

    protected void onItemImageClick(Context context, ImageView imageView, int index, List<T> list) {
    }

    protected boolean onItemImageLongClick(Context context, ImageView imageView, int index, List<T> list) {
        return false;
    }

    protected ImageView generateImageView(Context context) {
        GridImageView imageView = new GridImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
}