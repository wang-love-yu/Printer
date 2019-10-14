package zs.qimai.com.printer.canvas

import zs.qimai.com.printer.enmu.FontAligin
import zs.qimai.com.printer.enmu.FontSizeType
import zs.qimai.com.printer.paint.PrintWriter
import java.util.ArrayList

class TestPrintTemplate : PrintTemplate<Any> {
    override fun getPrintData(data: Any): ByteArray? {
        var datas = ArrayList<ByteArray>()
        var printWriter = PrintWriter().apply {
            //编号
            writelnLineText("#22", aligin = FontAligin.CENTER, fontSizeType = FontSizeType.FONT_3)
            writelnLineText("企迈店铺", aligin = FontAligin.CENTER, fontSizeType = FontSizeType.FONT_3)
            writelnLineText(
                "---" + "已支付" + "---",
                aligin = FontAligin.CENTER,
                fontSizeType = FontSizeType.FONT_3
            )
            writelnLineText("【打包】", aligin = FontAligin.CENTER, fontSizeType = FontSizeType.FONT_2)
            writelnLineText(
                "取餐号: S005",
                aligin = FontAligin.CENTER,
                fontSizeType = FontSizeType.FONT_3
            )
            writelnLineText(
                formatValuesAliginLfAndAliginRight("【桌号】", "1233"),
                fontSizeType = FontSizeType.FONT_2
            )
            writelnLineText(formatValuesAliginLfAndAliginRight("【下单时间】", "2019-10-10 18:34:24"))
            writelnLineText(formatAllLinePonit(), lineSpaceSize = 80)
            writelnLineText(
                formatGoods("红烧肉", "*1 ", "12.00"),
                fontSizeType = FontSizeType.FONT_2,
                lineSpaceSize = 100
            )
            writelnLineText("我是规格")
            writelnLineText(
                formatGoods("蛋炒饭我喜欢吃and你呢？", "【*2】", "15.00"),
                fontSizeType = FontSizeType.FONT_2, lineSpaceSize = 100
            )
            writelnLineText("我是规格", lineSpaceSize = 50)
            writelnLineText(formatAllLinePonit())
            writeNewLine()
            writelnLineText(formatValuesAliginLfAndAliginRight("打包费: ", "11.22"))
            writelnLineText(formatValuesAliginLfAndAliginRight("运费: ", "11.22"))
            writelnLineText(formatValuesAliginLfAndAliginRight("平台优惠: ", "11.22"))
            writelnLineText(formatValuesAliginLfAndAliginRight("平台优惠: ", "11.22"))
            writelnLineText(formatValuesAliginLfAndAliginRight("支付方式: ", "余额"))
            writelnLineText(
                formatValuesAliginLfAndAliginRight("已付:", "11.22"),
                fontSizeType = FontSizeType.FONT_2
            )
            writelnLineText(formatAllLinePonit())
            writeNewLine()
            writelnLineText("订单号:FD124Q443422")
            writelnLineText("谢谢惠顾，欢迎下次光临")
            writeNewLine(3)
            //writeNewLine(1)


            //打印商品


            //writelnLineText()


        }

        /* printWriter.writelnLineText("hello woshi靠左")
             .writelnLineText("hello 我是居中", aligin = FontAligin.CENTER)
             .writelnLineText("hello 我是居右", aligin = FontAligin.RIGHT)
             .writelnLineText("我是加粗", bold = true)
             .writelnLineText("我是放大1", fontSizeType = FontSizeType.FONT_1)
             .writelnLineText("我是放大2", fontSizeType = FontSizeType.FONT_2)
             .writelnLineText("我是放大3", fontSizeType = FontSizeType.FONT_3)*/
        datas.add(printWriter.convertByteArray())
        return printWriter.convertByteArray()
    }


}