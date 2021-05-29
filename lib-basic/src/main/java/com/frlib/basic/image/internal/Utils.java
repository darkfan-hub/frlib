package com.frlib.basic.image.internal;

import android.content.res.Resources;

public final class Utils {

    private Utils() {
        // Utility class.
    }

    public static int toDp(int px) {
        return px * (int) Resources.getSystem().getDisplayMetrics().density;
    }
}
