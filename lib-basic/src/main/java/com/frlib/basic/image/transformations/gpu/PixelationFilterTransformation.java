package com.frlib.basic.image.transformations.gpu;

import androidx.annotation.NonNull;
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePixelationFilter;

import java.security.MessageDigest;

/**
 * Applies a Pixelation effect to the image.
 * <p>
 * The pixel with a default of 10.0.
 */
public class PixelationFilterTransformation extends GPUFilterTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.frlib.basic.image.transformations.gpu.PixelationFilterTransformation." + VERSION;

    private final float pixel;

    public PixelationFilterTransformation() {
        this(10f);
    }

    public PixelationFilterTransformation(float pixel) {
        super(new GPUImagePixelationFilter());
        this.pixel = pixel;
        GPUImagePixelationFilter filter = getFilter();
        filter.setPixel(this.pixel);
    }

    @Override
    public String toString() {
        return "PixelationFilterTransformation(pixel=" + pixel + ")";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PixelationFilterTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + (int) (pixel * 10);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + pixel).getBytes(CHARSET));
    }
}
