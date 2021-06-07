package com.frlib.basic.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/02/2021 15:12
 * @desc String 类型解析适配器
 */
class StringTypeAdapter : TypeAdapter<String>() {

    override fun write(out: JsonWriter?, value: String?) {
        out?.value(value)
    }

    override fun read(`in`: JsonReader): String {
        when (`in`.peek()) {
            JsonToken.STRING,
            JsonToken.NUMBER -> {
                return `in`.nextString()
            }
            JsonToken.BOOLEAN -> {
                return `in`.nextBoolean().toString()
            }
            JsonToken.NULL -> {
                `in`.nextNull()
                return ""
            }
            else -> {
                `in`.skipValue()
                return ""
            }
        }
    }
}