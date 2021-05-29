package com.frlib.basic.image.transformations.gpu;

import androidx.annotation.NonNull;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter;

import java.security.MessageDigest;

public class SketchFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.frlib.basic.image.transformations.gpu.SketchFilterTransformation." + VERSION;

    public SketchFilterTransformation() {
        super(new GPUImageSketchFilter());
    }

    @Override
    public String toString() {
        return "SketchFilterTransformation()";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SketchFilterTransformation;
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
