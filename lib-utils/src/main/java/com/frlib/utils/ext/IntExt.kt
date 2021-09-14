package com.frlib.utils.ext

import java.math.BigDecimal

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 08/04/2021 19:54
 * @desc 整数扩展
 */
val cent2YuanDecimal by lazy {
    BigDecimal(100)
}

/**
 * 价格换算, 分转元
 */
fun Int.cent2Yuan(scale: Int = 0): String {
    return BigDecimal(this).setScale(scale).divide(cent2YuanDecimal).toString()
}

/**
 * 价格换算, 元转分
 */
fun Double.yuan2Cent(): Int {
    return BigDecimal(this.toString()).multiply(cent2YuanDecimal).toInt()
}

/**
 * 价格换算, 元转分
 */
fun Int.yuan2Cent(): Int {
    return BigDecimal(this.toString()).multiply(cent2YuanDecimal).toInt()
}

/**
 * long 判空
 */
fun Long?.isNull(): Boolean {
    return this == null
}

/**
 * 粉丝数格式化
 */
fun Int.fansCountFormat(): String {
    return if (this < 10000) {
        this.toString()
    } else {
        "${this / 10000}w+"
    }
}

fun Int.fixNumber(): String {
    return if (this < 10) "0$this" else this.toString()
}