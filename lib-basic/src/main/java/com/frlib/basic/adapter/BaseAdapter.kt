package com.frlib.basic.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 14/02/2021 11:50
 * @desc 适配器基类
 */
open class BaseAdapter<T, VH : BaseViewHolder> constructor(
    layoutId: Int
) : BaseQuickAdapter<T, VH>(layoutId) {

    lateinit var callback: ConvertCallback<T>

    override fun convert(holder: VH, item: T) {
        callback.convert(holder, item)
    }

    interface ConvertCallback<T> {

        fun convert(holder: BaseViewHolder, item: T)
    }
}