package com.frlib.basic.image.transformations.gpu;

import androidx.annotation.NonNull;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;

import java.security.MessageDigest;

/**
 * contrast value ranges from 0.0 to 4.0, with 1.0 as the normal level
 */
public class ContrastFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.frlib.basic.image.transformations.gpu.ContrastFilterTransformation." + VERSION;

    private final float contrast;

    public ContrastFilterTransformation() {
        this(1.0f);
    }

    public ContrastFilterTransformation(float contrast) {
        super(new GPUImageContrastFilter());
        this.contrast = contrast;
        GPUImageContrastFilter filter = getFilter();
        filter.setContrast(this.contrast);
    }

    @Override
    public String toString() {
        return "ContrastFilterTransformation(contrast=" + contrast + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ContrastFilterTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + (int) (contrast * 10);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + contrast).getBytes(CHARSET));
    }
}
