package zs.qimai.com.printer2.paint

import zs.qimai.com.printer2.enmu.FontAligin
import zs.qimai.com.printer2.enmu.FontSizeType
import zs.qimai.com.printer2.utils.PrintFormat
import java.io.ByteArrayOutputStream
import java.lang.StringBuilder
import java.nio.charset.Charset

/****
 * 这里可以类比为画笔
 *
 * 可以继承此类扩展更多写法
 *
 * 原理 -> 创建个ByteArrayOutputStream() 先写入其中，最后在转为字节数组
 *
 * **/
open class PrintWriter {
    val CHARSET = "gbk"
    val bos = ByteArrayOutputStream()

    init {
        //初始化设备
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
    fun writeAlign(align: Int = 0): PrintWriter {
        when (align) {
            0 -> write(PrintFormat.LEFT_ALIGIN)
            1 -> write(PrintFormat.CENTER_ALIGIN)
            2 -> write(PrintFormat.RIGHT_ALIGIN)
        }
        return this
    }

    fun write(value: String, charset: String = CHARSET): PrintWriter {
        value?.let {
            bos.write(it.toByteArray(charset(charset)))
        }
        return this
    }

    fun writeln(value: String, charset: String = CHARSET): PrintWriter {
        value?.let {
            bos.write(it.toByteArray(charset(charset)))
            writeNewLine()
        }

        return this
    }

    fun writeNewLine(numb: Int = 1): PrintWriter {
        for (i in 0 until numb) {
            write("\n".toByteArray(Charset.defaultCharset()))
        }
        return this
    }

    fun convertByteArray(): ByteArray {
        return bos.toByteArray()
    }

    /***
     *@param lineSpaceSize 行距，默认为 -1
     * ***/
    fun writelnLineText(
        value: String?,
        aligin: FontAligin = FontAligin.LEFT,
        bold: Boolean = false,
        fontSizeType: FontSizeType = FontSizeType.FONT_0,
        lineSpaceSize: Int = -1
    ): PrintWriter {
        if (value.isNullOrEmpty()) {
            return this
        }
        if (lineSpaceSize.toInt() == -1) {
            if (fontSizeType == FontSizeType.FONT_0 || fontSizeType == FontSizeType.FONT_1) {
                writeLineSpacingSize(50)
            } else {
                writeLineSpacingSize(100)
            }
        } else {
            writeLineSpacingSize(lineSpaceSize)
        }
        // write(byteArrayOf(0x1B, 0x33, 120))
        writeAligin(aligin)
        writeFontType(fontSizeType)
        writeFontNormalOrBold(bold)
        //写入内容
        write(value)
        //换行
        writeNewLine(1)
        return this
    }

    private fun writeFontNormalOrBold(bold: Boolean) {
        if (bold) write(PrintFormat.FONT_BOLD) else write(PrintFormat.FONT_NORMAL)
    }

    /***
     * 写入间距 默认大小是30
     * **/
    fun writeLineSpacingSize(nums: Int) {
        write(byteArrayOf(0x1B, 0x33, nums.toByte()))
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

    /***
     * 格式化............
     * */
    fun formatAllLinePonit(): String? {
        var sb = StringBuilder()
        for (i in 0 until getPerLineMaxByteNums()) {
            sb.append(".")
        }
        return sb.toString()
    }

    /****
     * 平台的订单打印商品
     * 要求 第一行显示商品，数量，价格
     * 如果商品名称过程，则剩余要在第二行写
     *
     * ps  红烧肉      【*10】    100
     * **/

    fun formatGoods(goodsName: String, nums: String, price: String): String {
        var formatGoodsName = fixedNumberOfDigits(goodsName, 16)
        var numsLength = getPerLineMaxByteNums() - getBytesSize(formatGoodsName) - 10
        var formatNums = fixedNumberOfDigits(nums, numsLength, 2)
        var formatPrice = fixedNumberOfDigits(price, 10, 2)
        var endGoodsName = StringBuilder()
        if (formatGoodsName.length < goodsName.length) {
            endGoodsName.append(goodsName.substring(formatGoodsName.length, goodsName.length))
        }
        return StringBuilder(formatGoodsName)
            .append(formatNums)
            .append(formatPrice)
            .append(endGoodsName)
            .toString()
    }

    /****
     *
     *@param values 需要修改的值
     * @param needBytes 固定多少位  多的填充空格
     * @param align  0 靠左 1靠中 2 靠右
     * **/
    private fun fixedNumberOfDigits(values: String, needBytes: Int, align: Int = 0): String {
        var valuesSb = StringBuilder(values)
        while (getBytesSize(valuesSb.toString()) > needBytes) {
            valuesSb.delete(valuesSb.length - 1, valuesSb.length)
        }

        //再次判断长度 小于16位补空格
        if (getBytesSize(valuesSb.toString()) < needBytes) {

            var leaveSpace = needBytes - getBytesSize(valuesSb.toString())
            when (align) {
                0 -> {
                    for (i in 0 until leaveSpace) {
                        valuesSb.append(" ")
                    }
                }
                1 -> {

                    var leftSpace = leaveSpace / 2
                    for (i in 0 until leftSpace) {
                        valuesSb = StringBuilder(" $valuesSb")
                    }
                    var rightSpace = leaveSpace - leftSpace
                    for (i in 0 until rightSpace) {
                        valuesSb.append(" ")
                    }
                }
                2 -> {
                    for (i in 0 until leaveSpace) {
                        valuesSb = StringBuilder(" $valuesSb")

                    }
                }

            }

        }
        return valuesSb.toString()
    }

    /****
     * 格式化内容 一左 一右
     * ps   优惠券           100
     * */
    fun formatValuesAliginLfAndAliginRight(left: String, right: String): String {
        var maxNums = getPerLineMaxByteNums()
        var leftLength = getBytesSize(left)
        var rightLength = getBytesSize(right)
        var spaceLength = maxNums - leftLength - rightLength
        var valuesBuilder = StringBuilder(left)
        for (i in 0 until spaceLength) {
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