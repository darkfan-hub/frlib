package com.android.frlib

import android.app.Application
import com.frlib.basic.vm.BaseListViewModel

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 30/05/2021 16:57
 * @desc
 */
class FrlibListViewModel(
    application: Application
) : BaseListViewModel<String>(application) {

    override suspend fun loadData(): List<String> {
        return arrayListOf()
    }
}