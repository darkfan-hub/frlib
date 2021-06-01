package com.frlib.utils.ext

import java.io.File

/**
 * @author Fanfan Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 01/06/2021 20:29
 * @desc 文件扩展类
 */

fun String.mkdirs(): File {
    val file = File(this)
    if (!file.exists()) {
        file.mkdirs()
    }

    return file
}