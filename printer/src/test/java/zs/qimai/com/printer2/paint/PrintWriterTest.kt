package zs.qimai.com.printer2.paint

import org.junit.Test
import java.lang.StringBuilder

class PrintWriterTest {


    fun formatGoods(goodsName: String, nums: String, price: String):String {
        var formatGoodsName = fixedNumberOfDigits(goodsName, 16)
        var numsLength = getPerLineMaxByteNums() - getBytesSize(formatGoodsName) - 10
        var formatNums = fixedNumberOfDigits(nums, numsLength, 2)
        var formatPrice = fixedNumberOfDigits(price, 10, 2)
        var endGoodsName = StringBuilder()
        if (formatGoodsName.length<goodsName.length){
         endGoodsName.append(goodsName.substring(formatGoodsName.length,goodsName.length))
        }
        return  StringBuilder(formatGoodsName)
            .append(formatNums)
            .append(formatPrice)
            .append(endGoodsName)

            .toString()
//        16 10（价格) 6


    }
    fun getPerLineMaxByteNums() = 32

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


    fun getBytesSize(value: String): Int {
        return value.toByteArray(charset("GBK")).size
    }


    @Test
    fun test() {
        var value = fixedNumberOfDigits("一二三四五六 七八九十", 16)

        var values2 = formatGoods("红烧肉我喜欢吃and你呢？", "22", "12.00")
        System.out.println("11111111111$values2")
    }

    private fun fixedNumberOfDigits(values: String, needBytes: Int): String {
        var valuesSb = StringBuilder(values)
        while (getBytesSize(valuesSb.toString()) > needBytes) {
            valuesSb.delete(valuesSb.length - 1, valuesSb.length)
            // valuesSb.append(values.substring(0,values.length-1))
            //valuesSb = StringBuilder(valuesSb.substring(0, valuesSb.length - 1))
        }

        //再次判断长度 小于16位补空格
        if (getBytesSize(valuesSb.toString()) < needBytes) {
            for (i in 0 until (16 - getBytesSize(valuesSb.toString()))) {
                valuesSb.append(" ")
            }
        }
        println("nums===== $valuesSb")
        return valuesSb.toString()
    }




}