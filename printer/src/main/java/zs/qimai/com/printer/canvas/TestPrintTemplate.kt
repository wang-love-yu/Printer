package zs.qimai.com.printer.canvas

import zs.qimai.com.printer.enmu.FontAligin
import zs.qimai.com.printer.enmu.FontSizeType
import zs.qimai.com.printer.paint.PrintWriter
import java.util.ArrayList

class TestPrintTemplate : PrintTemplate {
    override fun getPrintData(): ByteArray? {
        var datas = ArrayList<ByteArray>()
        var printWriter = PrintWriter().apply {
        //编号
            writelnLineText("#22",aligin = FontAligin.CENTER,fontSizeType = FontSizeType.FONT_3)
            writelnLineText("我是店铺",aligin = FontAligin.CENTER,fontSizeType = FontSizeType.FONT_3)
            writelnLineText("---"+"已支付"+"---",aligin = FontAligin.CENTER,fontSizeType = FontSizeType.FONT_3)
            writelnLineText("【打包】",aligin = FontAligin.CENTER,fontSizeType = FontSizeType.FONT_2)
            writelnLineText("取餐号: S005",aligin = FontAligin.CENTER,fontSizeType = FontSizeType.FONT_3)
            writelnLineText(formatValuesAliginLfAndAliginRight("【桌号】","1233"),fontSizeType = FontSizeType.FONT_2)
            writelnLineText(formatValuesAliginLfAndAliginRight("【下单时间】","2019-10-10 18:34:24"))


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