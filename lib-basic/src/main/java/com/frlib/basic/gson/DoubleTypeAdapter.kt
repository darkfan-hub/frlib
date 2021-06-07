package com.frlib.basic.gson

import com.frlib.utils.ext.length
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/02/2021 16:01
 * @desc double / Double 类型解析适配器
 */
class DoubleTypeAdapter : TypeAdapter<Double>() {

    override fun write(out: JsonWriter?, value: Double?) {
        out?.value(value)
    }

    override fun read(`in`: JsonReader): Double? {
        when (`in`.peek()) {
            JsonToken.NUMBER -> {
                return `in`.nextDouble()
            }
            JsonToken.STRING -> {
                val result = `in`.nextString()
                return if (result.length() > 0) {
                    result.toDouble()
                } else {
                    "0".toDouble()
                }
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