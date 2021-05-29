package com.frlib.basic.image.transformations.gpu;

import androidx.annotation.NonNull;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageKuwaharaFilter;

import java.security.MessageDigest;

/**
 * Kuwahara all the colors in the image.
 * <p>
 * The radius to sample from when creating the brush-stroke effect, with a default of 25.
 * The larger the radius, the slower the filter.
 */
public class KuwaharaFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.frlib.basic.image.transformations.gpu.KuwaharaFilterTransformation." + VERSION;

    private final int radius;

    public KuwaharaFilterTransformation() {
        this(25);
    }

    public KuwaharaFilterTransformation(int radius) {
        super(new GPUImageKuwaharaFilter());
        this.radius = radius;
        GPUImageKuwaharaFilter filter = getFilter();
        filter.setRadius(this.radius);
    }

    @Override
    public String toString() {
        return "KuwaharaFilterTransformation(radius=" + radius + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof KuwaharaFilterTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + radius * 10;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius).getBytes(CHARSET));
    }
}
