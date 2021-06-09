package com.frlib.basic.views.ninegrid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatImageView;
import timber.log.Timber;

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @desc 九宫格图片view
 * @since 10/12/2020 14:55
 */
public class GridImageView extends AppCompatImageView {

    public GridImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridImageView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Drawable drawable = getDrawable();
                if (drawable != null) {
                    drawable.mutate().setColorFilter(new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY));
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Drawable drawableUp = getDrawable();
                if (drawableUp != null) {
                    drawableUp.mutate().clearColorFilter();
                }
                break;
            default:
                break;
        }*/

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}