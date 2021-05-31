package com.frlib.basic.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.frlib.basic.image.displayCircleImage
import com.frlib.basic.image.displayImage
import com.frlib.basic.image.displayRadiusImage
import com.frlib.utils.ext.dp2px

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 31/05/2021 00:54
 * @desc data binding adapters
 */

@BindingAdapter(value = ["imageUrl", "placeholder"])
fun bindImage(imageView: ImageView, imageUrl: String, placeholder: Drawable) {
    imageView.displayImage(imageUrl, placeholder = placeholder)
}

@BindingAdapter(value = ["imageUrl", "radius", "placeholder"])
fun bindRadiusImage(imageView: ImageView, imageUrl: String, radius: Int, placeholder: Drawable) {
    imageView.displayRadiusImage(imageUrl, imageView.context.dp2px(radius.toFloat()), placeholder = placeholder)
}

@BindingAdapter(value = ["circleUrl", "placeholder"])
fun bindCircleImage(imageView: ImageView, circleUrl: String, placeholder: Drawable) {
    imageView.displayCircleImage(circleUrl, placeholder = placeholder)
}
