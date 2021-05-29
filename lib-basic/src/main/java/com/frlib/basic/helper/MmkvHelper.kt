package com.frlib.basic.helper

import android.content.Context
import android.os.Parcelable
import com.frlib.utils.ext.invalid
import com.tencent.mmkv.MMKV

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 29/05/2021 17:52
 * @desc mmkv数据保存帮助类
 */
class MmkvHelper private constructor(context: Context) {

    companion object {
        private var instances: MmkvHelper? = null

        fun initialize(context: Context): MmkvHelper {
            if (null == instances) {
                synchronized(MmkvHelper::class.java) {
                    if (null == instances) {
                        instances = MmkvHelper(context)
                    }
                }
            }

            return instances!!
        }

        fun default(): MmkvHelper {
            return instances!!
        }
    }

    private var mmkv: MMKV

    init {
        MMKV.initialize(context)
        mmkv = MMKV.defaultMMKV()!!
    }

    fun <T : Parcelable> put(key: String, t: T) {
        mmkv.encode(key, t)
    }

    fun put(key: String, sets: Set<String>) {
        mmkv.encode(key, sets)
    }

    fun get(key: String, defaultValue: Boolean = false): Boolean {
        return if (contains(key)) {
            mmkv.decodeBool(key, defaultValue)
        } else {
            defaultValue
        }
    }

    fun get(key: String, defaultValue: Int = 0): Int {
        return if (contains(key)) {
            mmkv.decodeInt(key, defaultValue)
        } else {
            defaultValue
        }
    }

    fun get(key: String, defaultValue: Long = 0L): Long {
        return if (contains(key)) {
            mmkv.decodeLong(key, defaultValue)
        } else {
            defaultValue
        }
    }

    fun get(key: String, defaultValue: Float = 0F): Float {
        return if (contains(key)) {
            mmkv.decodeFloat(key, defaultValue)
        } else {
            defaultValue
        }
    }

    fun get(key: String, defaultValue: Double = 0.0): Double {
        return if (contains(key)) {
            mmkv.decodeDouble(key, defaultValue)
        } else {
            defaultValue
        }
    }

    fun get(key: String, defaultValue: String = ""): String {
        return if (contains(key)) {
            mmkv.decodeString(key, defaultValue).invalid()
        } else {
            defaultValue
        }
    }

    fun remove(key: String) {
        mmkv.removeValueForKey(key)
    }

    fun contains(key: String): Boolean {
        return mmkv.containsKey(key) ?: false
    }

    fun clear() {
        mmkv.clearAll()
    }
}