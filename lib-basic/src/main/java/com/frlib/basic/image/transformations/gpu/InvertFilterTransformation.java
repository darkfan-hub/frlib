package com.frlib.basic.image.transformations.gpu;

import androidx.annotation.NonNull;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter;

import java.security.MessageDigest;

/**
 * Invert all the colors in the image.
 */
public class InvertFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.frlib.basic.image.transformations.gpu.InvertFilterTransformation." + VERSION;

    public InvertFilterTransformation() {
        super(new GPUImageColorInvertFilter());
    }

    @Override
    public String toString() {
        return "InvertFilterTransformation()";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof InvertFilterTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID).getBytes(CHARSET));
    }
}
