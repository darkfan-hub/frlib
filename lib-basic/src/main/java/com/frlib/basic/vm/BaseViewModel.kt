package com.frlib.basic.vm

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.frlib.basic.R
import com.frlib.basic.app.IAppComponent
import com.frlib.basic.config.AppConstants
import com.frlib.basic.data.entity.BasicApiEntity
import com.frlib.basic.defaultpages.Pages
import com.frlib.basic.lifecycle.SingleLiveEvent
import com.frlib.basic.net.ERROR
import com.frlib.basic.net.ResponseThrowable
import com.frlib.basic.net.handleException
import com.frlib.basic.net.handleHttpException
import com.frlib.utils.ext.string
import kotlinx.coroutines.*
import retrofit2.HttpException
import timber.log.Timber

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 17:56
 * @desc
 */
open class BaseViewModel(
    application: Application
) : AndroidViewModel(application), LifecycleObserver {

    open lateinit var appComponent: IAppComponent

    fun setupAppComponent(appComponent: IAppComponent) {
        this.appComponent = appComponent
    }

    val defUI: UIChange by lazy { UIChange() }

    /**
     * Activity 已显示
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun resumed() {
    }

    /**
     * Activity 已暂停
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun paused() {
    }

    /**
     * Activity 已停止
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun stooped() {
    }

    /**
     * 吐司
     */
    fun toast(msg: String) {
        defUI.toast.postValue(msg)
    }

    /**
     * 吐司
     */
    fun toast(@StringRes resId: Int) {
        toast(appComponent.application().string(resId))
    }

    /**
     * 显示loading
     */
    fun showLoading(title: String = appComponent.application().string(R.string.frlib_text_loading)) {
        defUI.showLoading.postValue(title)
    }

    /**
     * 隐藏loading
     */
    fun hideLoading() {
        launchUI { defUI.hideLoading.call() }
    }

    /** 协逞线程切换 */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }
    fun launchSync(block: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.IO).launch { block() }

    /**
     * 异步线程调用耗时操作并在主线程返回结果
     */
    open fun <T> launchOfResult(
        block: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        successNoResult: () -> Unit = {},
        error: (ResponseThrowable) -> Unit = { throwable ->
            Timber.e("${throwable.code} -------> ${throwable.errMsg}")
        },
        complete: () -> Unit = {
            Timber.d("${block.hashCode()} completed! ")
        },
        showLoading: Boolean = true
    ) {
        if (showLoading) {
            showLoading()
        }

        launchUI {
            handlerResult(
                { withContext(Dispatchers.IO) { block() } },
                { result ->
                    if (showLoading) {
                        hideLoading()
                    }

                    success(result)
                },
                { successNoResult() },
                { error ->
                    if (error.code == ERROR.TIMEOUT_ERROR.getKey()) {
                        defaultPages(Pages.NET_ERROR)
                    }

                    toast(error.errMsg)
                    error(error)
                },
                {
                    if (showLoading) {
                        hideLoading()
                    }
                    complete()
                })
        }
    }

    /**
     * 分发结果
     */
    open suspend fun <T> handlerResult(
        block: suspend CoroutineScope.() -> T,
        success: suspend CoroutineScope.(T) -> Unit,
        successNoResult: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                val result = block()
                if (result is BasicApiEntity<*>) {
                    if (result.status == AppConstants.api_state_success) {
                        if (result.data != null) {
                            success(result)
                        } else {
                            successNoResult()
                        }
                    } else {
                        defUI.toast.postValue(result.message)
                        error(
                            ResponseThrowable(
                                ERROR.HTTP_API_ERROR.getKey(),
                                result.message,
                                null
                            )
                        )
                    }
                } else {
                    success(result)
                }
            } catch (e: Throwable) {
                Timber.e("http request error -> $e")
                when (e) {
                    is HttpException -> {
                        error(e.handleHttpException())
                    }
                    else -> {
                        error(e.handleException())
                    }
                }
            } finally {
                complete()
            }
        }
    }

    /**
     * 缺省页
     *
     * @param pages 缺省页状态
     */
    fun defaultPages(pages: Pages) {
        launchUI { defUI.defaultPages.postValue(pages) }
    }

    class UIChange {
        val toast by lazy { MutableLiveData<String>() }
        val showLoading by lazy { MutableLiveData<String>() }
        val hideLoading by lazy { SingleLiveEvent<Void>() }
        val defaultPages by lazy { MutableLiveData<Pages>() }
    }
}