package com.frlib.basic.net

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 14/02/2021 11:12
 * @desc
 */
infix fun <A, B> A.with(that: B): Pair<A, B> = Pair(this, that)

/**
 * 请求参数 to hashmap
 */
inline fun paramToHashMap(vararg params: Pair<String, Any>): HashMap<String, Any> {
    return hashMapOf<String, Any>().apply {
        params.forEach {
            put(it.first, it.second)
        }
    }
}

/**
 * 请求参数 to RequestBody
 */
inline fun paramToRequestBody(vararg params: Pair<String, Any>): RequestBody {
    val json = JSONObject().apply {
        params.forEach {
            if (it.second is List<*>) {
                put(it.first, JSONArray.parseArray(JSON.toJSONString(it.second)))
            } else {
                put(it.first, it.second)
            }
        }
    }
    return json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
}

/**
 * 请求参数 to RequestBody
 */
inline fun paramToRequestBody(params: Map<String, Any>): RequestBody {
    val json = JSONObject(params)
    return json.toRequestBody()
}

/**
 * 请求参数 to RequestBody
 */
inline fun getRequestBody(vararg params: Pair<String, Any>): RequestBody {
    val json = JSONObject().apply {
        params.forEach {
            if (it.second is List<*>) {
                put(it.first, JSONArray.parseArray(JSON.toJSONString(it.second)))
            } else {
                put(it.first, it.second)
            }
        }
    }
    return json.toRequestBody()
}

inline fun JSONObject.toRequestBody(): RequestBody {
    return this.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
}

/**
 * 请求参数 to RequestBody
 */
inline fun getRequestBody(params: Map<String, Any>): RequestBody {
    val json = JSONObject(params)
    return json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
}

/**
 * 请求参数 to RequestBody 表单
 */
inline fun requestBodyWithForm(params: Map<String, String>): RequestBody {
    val builder = FormBody.Builder()
    for ((key, value) in params.entries) {
        builder.add(key, value)
    }
    return builder.build()
}