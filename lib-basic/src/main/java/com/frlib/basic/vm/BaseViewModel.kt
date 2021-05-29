package com.frlib.basic.vm

import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.frlib.basic.app.IAppComponent
import com.frlib.basic.config.AppConstants
import com.frlib.basic.data.entity.BasicApiEntity
import com.frlib.basic.defaultpages.Pages
import com.frlib.basic.net.ERROR
import com.frlib.basic.net.ResponseThrowable
import com.frlib.basic.net.handleException
import com.frlib.utils.ext.string
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 26/05/2021 17:56
 * @desc
 */
open class BaseViewModel(
    private val appComponent: IAppComponent
) : AndroidViewModel(appComponent.application()), LifecycleObserver {

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
    fun showLoading() {
        defUI.loading.postValue(true)
    }

    /**
     * 隐藏loading
     */
    fun hideLoading() {
        defUI.loading.postValue(false)
    }

    /** 协逞线程切换 */
    private fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }
    private fun launchSync(block: suspend CoroutineScope.() -> Unit) = CoroutineScope(Dispatchers.IO).launch { block() }

    /**
     * 异步线程调用耗时操作并在主线程返回结果
     */
    open fun <T> launchOfResult(
        block: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
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
                { error ->
                    toast(error.errMsg)
                    error(error)
                },
                { complete() })
        }
    }

    /**
     * 分发结果
     */
    open suspend fun <T> handlerResult(
        block: suspend CoroutineScope.() -> T,
        success: suspend CoroutineScope.(T) -> Unit,
        error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                val result = block()
                if (result is BasicApiEntity<*>) {
                    if (result.status == AppConstants.api_state_success) {
                        success(result)
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
                when (e) {
                    is HttpException -> {
                        if (AppConstants.api_state_token_invalid == e.code()) {
                            error(ResponseThrowable(ERROR.TOKEN_ERROR, e))
                        } else {
                            val srt = e.response()?.errorBody()?.string()
                            srt?.let { errStr ->
                                if (errStr.startsWith("{") && errStr.endsWith("}")) {
                                    val jsonObject = JSONObject(errStr)
                                    val code = jsonObject.optInt("code")
                                    val msg = jsonObject.optString("message")
                                    error(ResponseThrowable(code, msg))
                                } else {
                                    error(ResponseThrowable(ERROR.HTTP_ERROR, e))
                                }
                            }
                        }
                    }
                    else -> {
                        e.handleException()
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
        defUI.defaultPages.postValue(pages)
    }

    class UIChange {
        val toast by lazy { MutableLiveData<String>() }
        val loading by lazy { MutableLiveData<Boolean>() }
        val defaultPages by lazy { MutableLiveData<Pages>() }
    }
}