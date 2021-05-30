package com.android.frlib

import android.os.Bundle
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.frlib.basic.fragment.AbstractFragment
import com.frlib.basic.databinding.FrlibFragmentListBinding
import com.frlib.basic.views.list.AbstractFrListView
import com.frlib.basic.vm.BaseListViewModel

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 30/05/2021 16:55
 * @desc
 */
class FrlibFragment : AbstractFragment<FrlibFragmentListBinding, FrlibListViewModel>() {

    private val listView : AbstractFrListView<String> by lazy {
        object : AbstractFrListView<String>(selfContext, this) {
            override fun itemLayoutId(): Int {
                TODO("Not yet implemented")
            }

            override fun convertView(helper: BaseViewHolder, item: String) {
            }

            override fun viewModel(): BaseListViewModel<String> = viewModel
        }
    }

    override fun layoutId(): Int {
        TODO("Not yet implemented")
    }

    override fun initView(savedInstanceState: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun initData() {
        TODO("Not yet implemented")
    }
}