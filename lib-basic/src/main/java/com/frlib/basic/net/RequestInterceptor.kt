package com.frlib.basic.net

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import okio.IOException
import java.nio.charset.Charset

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 28/12/2020 15:25
 * @desc 请求拦截器
 */
class RequestInterceptor(
    private val globalHttpHandler: GlobalHttpHandler
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)
        val responseBody = originalResponse.body
        var bodyString = ""
        if (responseBody != null) {
            bodyString = requestResult(originalResponse)
        }

        globalHttpHandler.onHttpResultResponse(bodyString, chain, originalResponse)
        return originalResponse
    }

    private fun requestResult(response: Response): String {
        try {
            val responseBody = response.newBuilder().build().body
            val source = responseBody?.source()
            source?.apply {
                request(Long.MAX_VALUE)
                val clone = source.buffer.clone()
                val encoding = response.headers["Content-Encoding"]

                return parseContent(responseBody, encoding, clone)
            }
            return ""
        } catch (e: IOException) {
            return "{\"error\": \"" + e.message + "\"}"
        }
    }

    private fun parseContent(responseBody: ResponseBody, encoding: String?, clone: Buffer): String {
        var charset = Charsets.UTF_8
        val contentType = responseBody.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)!!
        }

        return when (encoding) {
            "gzip" -> {
                HttpZip.decompressForGzip(clone.readByteArray(), convertCharset(charset))
            }
            "zlib" -> {
                HttpZip.decompressToStringForZlib(
                    clone.readByteArray(),
                    convertCharset(charset)
                )
            }
            else -> {
                clone.readString(charset)
            }
        }
    }

    private fun convertCharset(charset: Charset): String? {
        val s: String = charset.toString()
        val i = s.indexOf("[")
        return if (i == -1) {
            s
        } else s.substring(i + 1, s.length - 1)
    }
}