package com.frlib.basic.views.ninegrid;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @desc 九宫格图片点击事件
 * @since 10/12/2020 14:55
 */
public interface ItemImageClickListener<T> {
    /**
     * 点击事件
     *
     * @param context 上下文对象
     * @param imageView 图片view
     * @param index 下标
     * @param list 图片集合
     */
    void onItemImageClick(Context context, ImageView imageView, int index, List<T> list);
}
