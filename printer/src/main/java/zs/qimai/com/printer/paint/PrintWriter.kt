package zs.qimai.com.printer.paint

import com.tools.command.EscCommand
import zs.qimai.com.printer.enmu.FontAligin
import zs.qimai.com.printer.enmu.FontSizeType
import zs.qimai.com.printer.utils.PrintFormat
import java.io.ByteArrayOutputStream
import java.lang.StringBuilder
import java.nio.charset.Charset

/****
 * 这里可以类比为画笔
 * **/
class PrintWriter {
    val CHARSET = "gbk"
    val bos = ByteArrayOutputStream()

    init {
        write(PrintFormat.INITBYTE)
    }

    fun write(bytes: ByteArray): PrintWriter {
        bytes?.let {
            bos.write(it)
        }
        return this
    }

    /***
     * 0 居左 1 居中 2 居右
     * **/
    fun printAlign(align: Int = 0): PrintWriter {
        when (align) {
            0 -> write(PrintFormat.LEFT_ALIGIN)
            1 -> write(PrintFormat.CENTER_ALIGIN)
            2 -> write(PrintFormat.RIGHT_ALIGIN)
        }
        return this
    }

    fun print(value: String, charset: String = CHARSET): PrintWriter {
        value?.let {
            bos.write(it.toByteArray(charset(charset)))
        }
        return this
    }

    fun println(value: String, charset: String = CHARSET): PrintWriter {
        value?.let {
            bos.write(it.toByteArray(charset(charset)))
            printNewLine()
        }

        return this
    }

    fun printNewLine(numb: Int = 1): PrintWriter {
        for (i in 0 until numb) {
            write("\n".toByteArray(Charset.defaultCharset()))
        }
        return this
    }

    fun convertByteArray(): ByteArray {
        return bos.toByteArray()
    }

    /***
     *
     * ***/
    fun writelnLineText(
        value: String?,
        aligin: FontAligin = FontAligin.LEFT,
        bold: Boolean = false,
        fontSizeType: FontSizeType = FontSizeType.FONT_0
    ): PrintWriter {
        if (value.isNullOrEmpty()) {
            return this
        }
        writeAligin(aligin)
        writeFontType(fontSizeType)
        writeFontNormalOrBold(bold)

        //写入内容
        print(value)
        //换行
        printNewLine(1)
        return this
    }

    private fun writeFontNormalOrBold(bold: Boolean) {
        if (bold) write(PrintFormat.FONT_BOLD) else write(PrintFormat.FONT_NORMAL)
    }

    /****
     * 写入方向
     * **/
    public fun writeAligin(aligin: FontAligin) {
        when (aligin) {
            FontAligin.LEFT -> write(PrintFormat.LEFT_ALIGIN)
            FontAligin.CENTER -> write(PrintFormat.CENTER_ALIGIN)
            FontAligin.RIGHT -> write(PrintFormat.RIGHT_ALIGIN)
        }
    }

    /**
     * 字体
     * 0 不放大 0x1d, 0x21, 0x00
     * <p>
     * 1 横向 宽方法  0x1d, 0x21, 0x10
     * <p>
     * 2 纵向
     * 3 横线纵向都放大
     */
    fun writeFontType(fontType: FontSizeType) {
        when (fontType) {
            FontSizeType.FONT_0 -> write(PrintFormat.FONT_TYPE_0)
            FontSizeType.FONT_1 -> write(PrintFormat.FONT_TYPE_1)
            FontSizeType.FONT_2 -> write(PrintFormat.FONT_TYPE_2)
            FontSizeType.FONT_3 -> write(PrintFormat.FONT_TYPE_3)
        }
    }

    /****
     * 格式化内容 一左 一右
     *
     * */
    fun formatValuesAliginLfAndAliginRight(left: String, right: String):String {
        var maxNums = getPerLineMaxByteNums()
        var leftLength = getBytesSize(left)
        var rightLength = getBytesSize(right)
        var spaceLength = maxNums-leftLength-rightLength
        var valuesBuilder = StringBuilder(left)
        for (i in 0 until spaceLength){
            valuesBuilder.append(" ")
        }
        valuesBuilder.append(right)
        return valuesBuilder.toString()
    }

    /****
     * 获取字节长度
     * **/
    fun getBytesSize(value: String): Int {
        return value.toByteArray(charset("GBK")).size
    }

    /**
     * 获取每行最大字节数
     * **/
    fun getPerLineMaxByteNums() = 32

}