package com.frlib.basic.gson

import com.frlib.utils.ext.length
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/02/2021 16:06
 * @desc boolean / Boolean 类型解析适配器
 */
class BooleanTypeAdapter : TypeAdapter<Boolean>() {

    override fun write(out: JsonWriter?, value: Boolean?) {
        out?.value(value)
    }

    override fun read(`in`: JsonReader): Boolean? {
        when (`in`.peek()) {
            JsonToken.BOOLEAN -> {
                return `in`.nextBoolean()
            }
            JsonToken.STRING -> {
                val result = `in`.nextString()
                return if (result.length() > 0) {
                    result!!.toBoolean()
                } else {
                    false
                }
            }
            JsonToken.NUMBER -> {
                return `in`.nextInt() != 0
            }
            JsonToken.NULL -> {
                `in`.nextNull()
                return null
            }
            else -> {
                `in`.skipValue()
                return null
            }
        }
    }
}