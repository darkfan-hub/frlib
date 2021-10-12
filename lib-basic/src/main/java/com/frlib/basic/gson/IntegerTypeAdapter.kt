package com.frlib.basic.gson

import com.frlib.utils.ext.length
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.NumberFormatException
import java.math.BigDecimal

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/02/2021 15:26
 * @desc int / Integer 类型解析适配器
 */
class IntegerTypeAdapter : TypeAdapter<Int>() {

    override fun write(out: JsonWriter?, value: Int?) {
        out?.value(value)
    }

    override fun read(`in`: JsonReader): Int? {
        when (`in`.peek()) {
            JsonToken.NUMBER -> {
                return try {
                    `in`.nextInt()
                } catch (e: NumberFormatException) {
                    `in`.nextDouble().toInt()
                }
            }
            JsonToken.STRING -> {
                val result = `in`.nextString()
                return try {
                    result.toInt()
                } catch (e: NumberFormatException) {
                    if (result.length() == 0) {
                        0
                    } else {
                        BigDecimal(result).toFloat().toInt()
                    }
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