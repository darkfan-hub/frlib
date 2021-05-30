package com.android.frlib

import com.frlib.basic.app.IAppComponent
import com.frlib.basic.vm.BaseListViewModel

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 30/05/2021 16:57
 * @desc
 */
class FrlibListViewModel(
    appComponent: IAppComponent
) : BaseListViewModel<String>(appComponent) {

    override suspend fun loadData(): List<String> {
        TODO("Not yet implemented")
    }
}