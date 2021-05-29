package com.frlib.basic.net

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import java.lang.NullPointerException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
        is SocketTimeoutException,
        is UnknownHostException-> {
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
