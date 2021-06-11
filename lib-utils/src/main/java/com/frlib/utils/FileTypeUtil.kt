package com.frlib.utils

import java.io.FileInputStream

/**
 * @author Gu <a href="mailto:stefan.gufan@gmail.com">Contact me.</a>
 * @date 28/12/2020 17:02
 * @desc 通过文件头来获取文件类型
 */
object FileTypeUtil {

    private val mFileTypes: HashMap<String, String> = HashMap()

    init {
        /**images */
        mFileTypes["FFD8FF"] = "jpg"
        mFileTypes["89504E47"] = "png"
        mFileTypes["47494638"] = "gif"
        mFileTypes["49492A00"] = "tif"
        mFileTypes["424D"] = "bmp"
        /** */
        mFileTypes["41433130"] = "dwg" //CAD
        mFileTypes["38425053"] = "psd"
        mFileTypes["7B5C727466"] = "rtf" //日记本
        mFileTypes["3C3F786D6C"] = "xml"
        mFileTypes["68746D6C3E"] = "html"
        mFileTypes["44656C69766572792D646174653A"] = "eml" //邮件
        mFileTypes["D0CF11E0"] = "doc"
        mFileTypes["5374616E64617264204A"] = "mdb"
        mFileTypes["252150532D41646F6265"] = "ps"
        mFileTypes["255044462D312E"] = "pdf"
        mFileTypes["504B0304"] = "zip"
        mFileTypes["52617221"] = "rar"
        mFileTypes["57415645"] = "wav"
        mFileTypes["41564920"] = "avi"
        mFileTypes["2E524D46"] = "rm"
        mFileTypes["000001BA"] = "mpg"
        mFileTypes["000001B3"] = "mpg"
        mFileTypes["6D6F6F76"] = "mov"
        mFileTypes["3026B2758E66CF11"] = "asf"
        mFileTypes["4D546864"] = "mid"
        mFileTypes["1F8B08"] = "gz"
    }

    fun getFileType(filePath: String): String? {
        if (filePath.startsWith("http")) {
            return null
        }

        val header = getFileHeader(filePath)
        return if (mFileTypes.containsKey(header)) {
            mFileTypes[header]
        } else {
            null
        }
    }

    /**
     * 获取头文件信息
     */
    private fun getFileHeader(filePath: String): String = FileInputStream(filePath).use {
        val b = ByteArray(3)
        it.read(b, 0, b.size)
        ConvertUtil.bytes2HexString(b)
    }
}