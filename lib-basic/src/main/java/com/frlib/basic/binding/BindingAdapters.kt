package com.frlib.basic.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.frlib.basic.image.displayCircleImage

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 31/05/2021 00:54
 * @desc data binding adapters
 */

@BindingAdapter(value = ["imageUrl", "placeholder"])
fun bindImage(imageView: ImageView, imageUrl: String, placeholder: Drawable) {
    imageView.displayCircleImage(imageUrl, placeholder = placeholder)
}

@BindingAdapter(value = ["circleUrl", "placeholder"])
fun bindCircleImage(imageView: ImageView, circleUrl: String, placeholder: Drawable) {
    imageView.displayCircleImage(circleUrl, placeholder = placeholder)
}
