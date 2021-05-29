package com.frlib.basic.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.*;
import com.frlib.basic.R;
import com.frlib.basic.databinding.FrlibLayoutToastBinding;
import com.frlib.utils.ext.ResourcesExtKt;

/**
 * This file is part of Toasty.
 * <p>
 * Toasty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Toasty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Toasty.  If not, see <http://www.gnu.org/licenses/>.
 */

@SuppressLint("InflateParams")
public class FrToasty {
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;
    private static final Typeface LOADED_TOAST_TYPEFACE = Typeface.create("sans-serif-condensed", Typeface.NORMAL);
    private static Typeface currentTypeface = LOADED_TOAST_TYPEFACE;
    private static int textSize = 16; // in SP
    private static boolean tintIcon = true;
    private static boolean allowQueue = true;
    private static Toast lastToast = null;

    private FrToasty() {
        // avoiding instantiation
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @StringRes int message) {
        return normal(context, context.getString(message), Toast.LENGTH_SHORT, null, false);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @NonNull CharSequence message) {
        return normal(context, message, Toast.LENGTH_SHORT, null, false);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @StringRes int message, Drawable icon) {
        return normal(context, context.getString(message), Toast.LENGTH_SHORT, icon, true);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @NonNull CharSequence message, Drawable icon) {
        return normal(context, message, Toast.LENGTH_SHORT, icon, true);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @StringRes int message, int duration) {
        return normal(context, context.getString(message), duration, null, false);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @NonNull CharSequence message, int duration) {
        return normal(context, message, duration, null, false);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @StringRes int message, int duration,
                               Drawable icon) {
        return normal(context, context.getString(message), duration, icon, true);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @NonNull CharSequence message, int duration,
                               Drawable icon) {
        return normal(context, message, duration, icon, true);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @StringRes int message, int duration,
                               Drawable icon, boolean withIcon) {
        return custom(context, context.getString(message), icon, ResourcesExtKt.color(context, R.color.color_toast_frame),
                ResourcesExtKt.color(context, R.color.color_white), duration, withIcon);
    }

    @CheckResult
    public static Toast normal(@NonNull Context context, @NonNull CharSequence message, int duration,
                               Drawable icon, boolean withIcon) {
        return custom(context, message, icon, ResourcesExtKt.color(context, R.color.color_toast_frame),
                ResourcesExtKt.color(context, R.color.color_white), duration, withIcon);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context, @StringRes int message) {
        return warning(context, context.getString(message), Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context, @NonNull CharSequence message) {
        return warning(context, message, Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context, @StringRes int message, int duration) {
        return warning(context, context.getString(message), duration, true);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context, @NonNull CharSequence message, int duration) {
        return warning(context, message, duration, true);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
        return custom(context, context.getString(message), ResourcesExtKt.drawable(context, R.drawable.frlib_icon_toast_info),
                ResourcesExtKt.color(context, R.color.color_ffa900), ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast warning(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
        return custom(context, message, ResourcesExtKt.drawable(context, R.drawable.frlib_icon_toast_info),
                ResourcesExtKt.color(context, R.color.color_ffa900), ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast info(@NonNull Context context, @StringRes int message) {
        return info(context, context.getString(message), Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast info(@NonNull Context context, @NonNull CharSequence message) {
        return info(context, message, Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast info(@NonNull Context context, @StringRes int message, int duration) {
        return info(context, context.getString(message), duration, true);
    }

    @CheckResult
    public static Toast info(@NonNull Context context, @NonNull CharSequence message, int duration) {
        return info(context, message, duration, true);
    }

    @CheckResult
    public static Toast info(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
        return custom(context, context.getString(message), ResourcesExtKt.drawable(context, R.drawable.frlib_icon_toast_info),
                ResourcesExtKt.color(context, R.color.color_3f51b5), ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast info(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
        return custom(context, message, ResourcesExtKt.drawable(context, R.drawable.frlib_icon_toast_info),
                ResourcesExtKt.color(context, R.color.color_3f51b5), ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast success(@NonNull Context context, @StringRes int message) {
        return success(context, context.getString(message), Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast success(@NonNull Context context, @NonNull CharSequence message) {
        return success(context, message, Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast success(@NonNull Context context, @StringRes int message, int duration) {
        return success(context, context.getString(message), duration, true);
    }

    @CheckResult
    public static Toast success(@NonNull Context context, @NonNull CharSequence message, int duration) {
        return success(context, message, duration);
    }

    @CheckResult
    public static Toast success(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
        return custom(context, context.getString(message), ResourcesExtKt.drawable(context, R.drawable.frlib_icon_toast_success),
                ResourcesExtKt.color(context, R.color.color_388e3c), ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast success(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
        return custom(context, message, ResourcesExtKt.drawable(context, R.drawable.frlib_icon_toast_success),
                ResourcesExtKt.color(context, R.color.color_388e3c), ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast error(@NonNull Context context, @StringRes int message) {
        return error(context, context.getString(message), Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast error(@NonNull Context context, @NonNull CharSequence message) {
        return error(context, message, Toast.LENGTH_SHORT, true);
    }

    @CheckResult
    public static Toast error(@NonNull Context context, @StringRes int message, int duration) {
        return error(context, context.getString(message), duration, true);
    }

    @CheckResult
    public static Toast error(@NonNull Context context, @NonNull CharSequence message, int duration) {
        return error(context, message, duration, true);
    }

    @CheckResult
    public static Toast error(@NonNull Context context, @StringRes int message, int duration, boolean withIcon) {
        return custom(context, context.getString(message), ResourcesExtKt.drawable(context, R.drawable.frlib_icon_toast_error),
                ResourcesExtKt.color(context, R.color.color_d50000), ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast error(@NonNull Context context, @NonNull CharSequence message, int duration, boolean withIcon) {
        return custom(context, message, ResourcesExtKt.drawable(context, R.drawable.frlib_icon_toast_error),
                ResourcesExtKt.color(context, R.color.color_d50000), ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast custom(@NonNull Context context, @StringRes int message, Drawable icon,
                               int duration, boolean withIcon) {
        return custom(context, context.getString(message), icon, -1, ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast custom(@NonNull Context context, @NonNull CharSequence message, Drawable icon,
                               int duration, boolean withIcon) {
        return custom(context, message, icon, -1, ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast custom(@NonNull Context context, @StringRes int message, @DrawableRes int iconRes,
                               @ColorRes int tintColorRes, int duration, boolean withIcon) {
        return custom(context, context.getString(message), ResourcesExtKt.drawable(context, iconRes),
                ResourcesExtKt.color(context, tintColorRes), ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast custom(@NonNull Context context, @NonNull CharSequence message, @DrawableRes int iconRes,
                               @ColorRes int tintColorRes, int duration, boolean withIcon) {
        return custom(context, message, ResourcesExtKt.drawable(context, iconRes),
                ResourcesExtKt.color(context, tintColorRes), ResourcesExtKt.color(context, R.color.color_white),
                duration, withIcon);
    }

    @CheckResult
    public static Toast custom(@NonNull Context context, @StringRes int message, Drawable icon,
                               @ColorRes int tintColorRes, int duration,
                               boolean withIcon) {
        return custom(context, context.getString(message), icon, ResourcesExtKt.color(context, tintColorRes),
                ResourcesExtKt.color(context, R.color.color_white), duration, withIcon);
    }

    @CheckResult
    public static Toast custom(@NonNull Context context, @StringRes int message, Drawable icon,
                               @ColorRes int tintColorRes, @ColorRes int textColorRes, int duration,
                               boolean withIcon) {
        return custom(context, context.getString(message), icon, ResourcesExtKt.color(context, tintColorRes),
                ResourcesExtKt.color(context, textColorRes), duration, withIcon);
    }

    @SuppressLint("ShowToast")
    @CheckResult
    public static Toast custom(@NonNull Context context, @NonNull CharSequence message, Drawable icon,
                               @ColorInt int tintColor, @ColorInt int textColor, int duration,
                               boolean withIcon) {
        final Toast currentToast = Toast.makeText(context, "", duration);
        FrlibLayoutToastBinding binding = FrlibLayoutToastBinding.inflate(LayoutInflater.from(context));
        Drawable drawableFrame = ResourcesExtKt.drawable(context, R.drawable.frlib_toast_frame);
        ResourcesExtKt.tintIcon(drawableFrame, tintColor);
        ResourcesExtKt.backgroundExt(binding.getRoot(), drawableFrame);

        if (withIcon) {
            if (icon == null) {
                throw new IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true");
            }

            ResourcesExtKt.backgroundExt(binding.toastIcon, icon);
            ResourcesExtKt.backgroundExt(binding.toastIcon, tintIcon ? ResourcesExtKt.tintIcon(icon, textColor) : icon);
        } else {
            binding.toastIcon.setVisibility(View.GONE);
        }

        binding.toastText.setText(message);
        binding.toastText.setTextColor(textColor);
        binding.toastText.setTypeface(currentTypeface);
        binding.toastText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        currentToast.setView(binding.getRoot());
        currentToast.setGravity(Gravity.CENTER, 0,0);

        if (!allowQueue) {
            if (lastToast != null) {
                lastToast.cancel();
            }
            lastToast = currentToast;
        }

        return currentToast;
    }

    public static class Config {
        private Typeface typeface = FrToasty.currentTypeface;
        private int textSize = FrToasty.textSize;

        private boolean tintIcon = FrToasty.tintIcon;
        private boolean allowQueue = true;

        private Config() {
        }

        @CheckResult
        public static Config getInstance() {
            return new Config();
        }

        public static void reset() {
            FrToasty.currentTypeface = LOADED_TOAST_TYPEFACE;
            FrToasty.textSize = 16;
            FrToasty.tintIcon = true;
            FrToasty.allowQueue = true;
        }

        @CheckResult
        public Config setToastTypeface(@NonNull Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        @CheckResult
        public Config setTextSize(int sizeInSp) {
            this.textSize = sizeInSp;
            return this;
        }

        @CheckResult
        public Config tintIcon(boolean tintIcon) {
            this.tintIcon = tintIcon;
            return this;
        }

        @CheckResult
        public Config allowQueue(boolean allowQueue) {
            this.allowQueue = allowQueue;
            return this;
        }

        public void apply() {
            FrToasty.currentTypeface = typeface;
            FrToasty.textSize = textSize;
            FrToasty.tintIcon = tintIcon;
            FrToasty.allowQueue = allowQueue;
        }
    }
}
