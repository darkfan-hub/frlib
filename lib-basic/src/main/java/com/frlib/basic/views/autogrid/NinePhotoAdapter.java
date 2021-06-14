package com.frlib.basic.views.autogrid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.frlib.basic.R;
import com.frlib.basic.ext.ViewExtKt;
import com.frlib.basic.views.ninegrid.GridImageView;

import java.util.List;

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/03/2021 17:39
 * @desc 九宫格图片适配器
 */
public abstract class NinePhotoAdapter extends QuickAutoGridAdapter<String, BaseAutoGridHolder> {

    @Override
    public int getItemCount() {
        if (mPlusEnable && itemCount() < mMaxItemCount) {
            return itemCount() + 1;
        }

        return itemCount();
    }

    @Override
    public int onHandleViewType(int position) {
        return 0;
    }

    @Override
    public BaseAutoGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }

        View convertView = getItemView(R.layout.frlib_grid_item_nine_photo, parent);
        return createBaseViewHolder(convertView);
    }

    @Override
    public void convert(BaseAutoGridHolder holder, int position, String item) {
        GridImageView imageView = holder.findViewById(R.id.iv_item_nine_photo_photo);
        onDisplayImage(imageView.getContext(), imageView, item);
        bindViewClickListener(holder, imageView.getContext(), position);
    }

    private void bindViewClickListener(BaseAutoGridHolder holder, Context context, int position) {
        ViewExtKt.click(holder.getConvertView(), () -> {
            if (isPlusItem(position)) {
                onClickAddNinePhotoItem(context);
            } else {
                onClickNinePhotoItem(context, position, getData());
            }
            return null;
        });
    }

    /**
     * 显示图片
     *
     * @param context   上下文对象
     * @param imageView 图片view
     * @param item      图片源, 如果有添加图片则可能为空
     */
    protected abstract void onDisplayImage(Context context, ImageView imageView, String item);

    protected abstract void onClickAddNinePhotoItem(Context context);

    protected abstract void onClickNinePhotoItem(Context context, int index, List<String> list);
}
