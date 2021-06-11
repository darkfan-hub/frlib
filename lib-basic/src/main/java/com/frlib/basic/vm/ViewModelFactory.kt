package com.frlib.basic.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.frlib.basic.app.IAppComponent

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 28/05/2021 00:53
 * @desc 自定义view model创建工厂
 */
class ViewModelFactory(
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        private var instances: ViewModelFactory? = null

        fun default(application: Application): ViewModelFactory {
            if (null == instances) {
                synchronized(ViewModelFactory::class.java) {
                    if (null == instances) {
                        instances = ViewModelFactory(application)
                    }
                }
            }

            return instances!!
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Application::class.java).newInstance(application)
    }
}