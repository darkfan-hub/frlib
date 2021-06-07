package com.frlib.basic.gson

import com.frlib.utils.ext.length
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.math.BigDecimal

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 11/02/2021 16:31
 * @desc BigDecimal 类型解析适配器
 */
class BigDecimalTypeAdapter : TypeAdapter<BigDecimal>() {

    override fun write(out: JsonWriter?, value: BigDecimal?) {
        out?.value(value)
    }

    override fun read(`in`: JsonReader): BigDecimal? {
        when (`in`.peek()) {
            JsonToken.NUMBER,
            JsonToken.STRING -> {
                val result = `in`.nextString()
                return if (result.length() > 0) {
                    BigDecimal(result)
                } else {
                    BigDecimal(0)
                }
            }
            JsonToken.NULL -> {
                `in`.skipValue()
                return null
            }
            else -> {
                `in`.skipValue()
                return null
            }
        }
    }
}