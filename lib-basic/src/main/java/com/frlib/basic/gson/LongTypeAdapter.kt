package com.frlib.basic.gson

import com.frlib.utils.ext.length
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.math.BigDecimal

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/02/2021 15:19
 * @desc long / Long 类型解析适配器
 */
class LongTypeAdapter : TypeAdapter<Long>() {

    override fun write(out: JsonWriter?, value: Long?) {
        out?.value(value)
    }

    override fun read(`in`: JsonReader): Long? {
        when (`in`.peek()) {
            JsonToken.NUMBER -> {
                return try {
                    `in`.nextLong()
                } catch (e: NumberFormatException) {
                    BigDecimal(`in`.nextString()).toLong()
                }
            }
            JsonToken.STRING -> {
                val result = `in`.nextString()
                return if (result.length() > 0) {
                    try {
                        result.toLong()
                    } catch (e: NumberFormatException) {
                        BigDecimal(result).toLong()
                    }
                } else {
                    0L
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