package com.frlib.basic.views.autogrid;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.IdRes;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/02/2021 10:26
 * @desc 网格适配器holder
 */
public class BaseAutoGridHolder {

    private View convertView;
    private int mViewType;

    public BaseAutoGridHolder(final View convertView) {
        this.convertView = convertView;
    }

    public View getConvertView() {
        return convertView;
    }

    public <T extends View> T findViewById(@IdRes int viewId) {
        T view = (T) convertView.findViewById(viewId);
        return view;
    }

    public ImageView getImageView(@IdRes int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(@IdRes int viewId) {
        return findViewById(viewId);
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        this.mViewType = viewType;
    }
}
