package com.frlib.basic.image.transformations.gpu;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.frlib.basic.image.transformations.BitmapTransformation;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

import java.security.MessageDigest;

public class GPUFilterTransformation extends BitmapTransformation {

    private static final int VERSION = 1;
    private static final String ID =
            "com.frlib.basic.image.transformations.gpu.GPUFilterTransformation." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private final GPUImageFilter gpuImageFilter;

    public GPUFilterTransformation(GPUImageFilter filter) {
        this.gpuImageFilter = filter;
    }

    @Override
    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool,
                               @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(toTransform);
        gpuImage.setFilter(gpuImageFilter);

        return gpuImage.getBitmapWithFilterApplied();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @SuppressWarnings("unchecked")
    public <T> T getFilter() {
        return (T) gpuImageFilter;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GPUFilterTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
