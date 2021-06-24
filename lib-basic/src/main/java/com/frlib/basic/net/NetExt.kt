package com.frlib.basic.net

import android.net.ParseException
import com.frlib.basic.config.AppConstants
import com.frlib.utils.ext.length
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 25/05/2021 17:26
 * @desc
 */
fun Throwable.handleException(): ResponseThrowable {
    return when (this) {
        is JsonParseException,
        is JSONException,
        is ParseException,
        is MalformedJsonException -> {
            ResponseThrowable(ERROR.PARSE_ERROR, this)
        }
        is ConnectException -> {
            ResponseThrowable(ERROR.NETWORD_ERROR, this)
        }
        is SSLException -> {
            ResponseThrowable(ERROR.SSL_ERROR, this)
        }
        is TimeoutException,
        is SocketTimeoutException,
        is UnknownHostException -> {
            ResponseThrowable(ERROR.TIMEOUT_ERROR, this)
        }
        is NullPointerException -> {
            ResponseThrowable(ERROR.HTTP_API_ERROR, this)
        }
        else -> {
            ResponseThrowable(ERROR.UNKNOWN, this)
        }
    }
}

fun Throwable.handleHttpException(): ResponseThrowable {
    return if (this is HttpException) {
        when (this.code()) {
            AppConstants.api_state_token_invalid -> ResponseThrowable(ERROR.TOKEN_ERROR, this)
            404 -> ResponseThrowable(ERROR.HTTP_NOT_FOUND, this)
            else -> {
                val str = this.response()?.errorBody()?.string()
                if (str.length() > 0 && str!!.startsWith("{") && str.endsWith("}")) {
                    val jsonObject = JSONObject(str)
                    val code = jsonObject.optInt("code")
                    val msg = jsonObject.optString("message")
                    ResponseThrowable(code, msg)
                } else {
                    ResponseThrowable(ERROR.HTTP_ERROR, this)
                }
            }
        }
    } else {
        ResponseThrowable(ERROR.HTTP_ERROR, this)
    }
}