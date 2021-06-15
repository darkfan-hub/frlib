package com.frlib.utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * Creator: Gu FanFan.
 * Date: 2017/8/4, 15:27.
 * Description: 复制相关.
 */
public final class ClipboardUtil {

    private ClipboardUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Copy the text to clipboard.
     * <p>The label equals name of package.</p>
     *
     * @param text The text.
     */
    public static void copyText(final Context context, final CharSequence text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(ClipData.newPlainText(context.getPackageName(), text));
    }

    /**
     * Copy the text to clipboard.
     *
     * @param label The label.
     * @param text  The text.
     */
    public static void copyText(final Context context, final CharSequence label, final CharSequence text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(ClipData.newPlainText(label, text));
    }

    /**
     * Clear the clipboard.
     */
    public static void clear(final Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.setPrimaryClip(ClipData.newPlainText(null, ""));
    }

    /**
     * Return the label for clipboard.
     *
     * @return the label for clipboard
     */
    public static CharSequence getLabel(final Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        ClipDescription des = cm.getPrimaryClipDescription();
        if (des == null) {
            return "";
        }
        CharSequence label = des.getLabel();
        if (label == null) {
            return "";
        }
        return label;
    }

    /**
     * Return the text for clipboard.
     *
     * @return the text for clipboard
     */
    public static CharSequence getText(final Context context) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            CharSequence text = clip.getItemAt(0).coerceToText(context);
            if (text != null) {
                return text;
            }
        }
        return "";
    }

    /**
     * Add the clipboard changed listener.
     */
    public static void addChangedListener(final Context context, final ClipboardManager.OnPrimaryClipChangedListener listener) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.addPrimaryClipChangedListener(listener);
    }

    /**
     * Remove the clipboard changed listener.
     */
    public static void removeChangedListener(final Context context, final ClipboardManager.OnPrimaryClipChangedListener listener) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //noinspection ConstantConditions
        cm.removePrimaryClipChangedListener(listener);
    }
}
