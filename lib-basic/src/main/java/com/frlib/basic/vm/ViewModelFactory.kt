package com.frlib.basic.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frlib.basic.app.IAppComponent

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 28/05/2021 00:53
 * @desc 自定义view model创建工厂
 */
class ViewModelFactory(
    private val appComponent: IAppComponent
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        private var instances: ViewModelFactory? = null

        fun default(appComponent: IAppComponent): ViewModelFactory {
            if (null == instances) {
                synchronized(ViewModelFactory::class.java) {
                    if (null == instances) {
                        instances = ViewModelFactory(appComponent)
                    }
                }
            }

            return instances!!
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(IAppComponent::class.java).newInstance(appComponent)
    }
}