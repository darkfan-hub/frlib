package com.frlib.basic.binding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.frlib.basic.image.displayCircleImage
import com.frlib.basic.image.displayImage
import com.frlib.basic.image.displayRadiusImage
import com.frlib.utils.ext.dp2px
import com.frlib.utils.ext.drawable
import com.frlib.utils.ext.invalid

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 31/05/2021 00:54
 * @desc data binding adapters
 */

@BindingAdapter(value = ["imageRes"])
fun bindImage(imageView: ImageView, imageRes: Int) {
    imageView.setImageDrawable(imageView.context.drawable(imageRes))
}

@BindingAdapter(value = ["imageUrl", "placeholder"])
fun bindImage(imageView: ImageView, imageUrl: String?, placeholder: Drawable) {
    imageView.displayImage(imageUrl.invalid(), placeholder = placeholder)
}

@BindingAdapter(value = ["imageUrl", "placeholderRes"])
fun bindImage(imageView: ImageView, imageUrl: String?, placeholderRes: Int) {
    imageView.displayImage(imageUrl.invalid(), placeholder = imageView.context.drawable(placeholderRes)!!)
}

@BindingAdapter(value = ["imageUrl", "radius", "placeholder"])
fun bindRadiusImage(imageView: ImageView, imageUrl: String?, radius: Int, placeholder: Drawable) {
    imageView.displayRadiusImage(imageUrl.invalid(), imageView.context.dp2px(radius.toFloat()), placeholder = placeholder)
}

@BindingAdapter(value = ["circleUrl", "placeholder"])
fun bindCircleImage(imageView: ImageView, circleUrl: String?, placeholder: Drawable) {
    imageView.displayCircleImage(circleUrl.invalid(), placeholder = placeholder)
}
