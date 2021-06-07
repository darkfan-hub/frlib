package com.frlib.basic.gson

import com.frlib.utils.ext.length
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/02/2021 15:58
 * @desc float / Float 类型解析适配器
 */
class FloatTypeAdapter : TypeAdapter<Float>() {

    override fun write(out: JsonWriter?, value: Float?) {
        out?.value(value)
    }

    override fun read(`in`: JsonReader): Float? {
        when (`in`.peek()) {
            JsonToken.NUMBER -> {
                return `in`.nextDouble().toFloat()
            }
            JsonToken.STRING -> {
                val result = `in`.nextString()
                return if (result.length() > 0) {
                    result.toFloat()
                } else {
                    0F
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