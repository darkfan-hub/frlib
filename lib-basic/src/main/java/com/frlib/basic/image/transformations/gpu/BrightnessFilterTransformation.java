package com.frlib.basic.image.transformations.gpu;

import androidx.annotation.NonNull;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;

import java.security.MessageDigest;

/**
 * brightness value ranges from -1.0 to 1.0, with 0.0 as the normal level
 */
public class BrightnessFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.frlib.basic.image.transformations.gpu.BrightnessFilterTransformation." + VERSION;

    private final float brightness;

    public BrightnessFilterTransformation() {
        this(0.0f);
    }

    public BrightnessFilterTransformation(float brightness) {
        super(new GPUImageBrightnessFilter());
        this.brightness = brightness;
        GPUImageBrightnessFilter filter = getFilter();
        filter.setBrightness(this.brightness);
    }

    @Override
    public String toString() {
        return "BrightnessFilterTransformation(brightness=" + brightness + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BrightnessFilterTransformation &&
                ((BrightnessFilterTransformation) o).brightness == brightness;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + (int) ((brightness + 1.0f) * 10);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + brightness).getBytes(CHARSET));
    }
}
