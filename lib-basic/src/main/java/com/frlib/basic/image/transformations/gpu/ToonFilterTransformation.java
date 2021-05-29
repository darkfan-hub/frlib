package com.frlib.basic.image.transformations.gpu;

import androidx.annotation.NonNull;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageToonFilter;

import java.security.MessageDigest;

/**
 * The threshold at which to apply the edges, default of 0.2.
 * The levels of quantization for the posterization of colors within the scene,
 * with a default of 10.0.
 */
public class ToonFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.frlib.basic.image.transformations.gpu.ToonFilterTransformation." + VERSION;

    private final float threshold;
    private final float quantizationLevels;

    public ToonFilterTransformation() {
        this(.2f, 10.0f);
    }

    public ToonFilterTransformation(float threshold, float quantizationLevels) {
        super(new GPUImageToonFilter());
        this.threshold = threshold;
        this.quantizationLevels = quantizationLevels;
        GPUImageToonFilter filter = getFilter();
        filter.setThreshold(this.threshold);
        filter.setQuantizationLevels(this.quantizationLevels);
    }

    @Override
    public String toString() {
        return "ToonFilterTransformation(threshold=" + threshold + ",quantizationLevels="
                + quantizationLevels + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ToonFilterTransformation &&
                ((ToonFilterTransformation) o).threshold == threshold &&
                ((ToonFilterTransformation) o).quantizationLevels == quantizationLevels;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + (int) (threshold * 1000) + (int) (quantizationLevels * 10);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + threshold + quantizationLevels).getBytes(CHARSET));
    }
}
