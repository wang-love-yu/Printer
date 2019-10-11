package zs.qimai.com.printer.paint

import zs.qimai.com.printer.utils.PrintFormat
import java.io.ByteArrayOutputStream
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

    public fun write(bytes: ByteArray): PrintWriter {
        bytes?.let {
            bos.write(it)
        }
        return this
    }

    /***
     * 0 居左 1 居中 2 居右
     * **/
    public fun printAlign(align: Int = 0): PrintWriter {
        when (align) {
            0 -> write(PrintFormat.LEFT_ALIGIN)
            1 -> write(PrintFormat.CENTER_ALIGIN)
            2 -> write(PrintFormat.RIGHT_ALIGIN)
        }
        return this
    }

    public fun print(value: String, charset: String = CHARSET): PrintWriter {
        value?.let {
            bos.write(it.toByteArray(kotlin.text.charset(charset)))
        }
        return this
    }

    public fun println(value: String, charset: String = CHARSET): PrintWriter {
        value?.let {
            bos.write(it.toByteArray(kotlin.text.charset(charset)))
            printNewLine()
        }

        return this
    }

    public fun printNewLine(numb: Int = 1): PrintWriter {
        for (i in 0 until numb) {
            write("\n".toByteArray(Charset.defaultCharset()))
        }
        return this
    }

    fun convertByteArray(): ByteArray {
        return bos.toByteArray()
    }

}