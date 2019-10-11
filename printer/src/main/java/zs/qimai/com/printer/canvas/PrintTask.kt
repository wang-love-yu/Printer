package zs.qimai.com.printer.canvas

import zs.qimai.com.printer.paint.PrintWriter
import java.util.ArrayList

class PrintTask {

    fun getPrintData(): ByteArray {
        var datas = ArrayList<ByteArray>()
        var printWriter = PrintWriter()
        printWriter
            .printAlign(1)
            .println("hello")
            .printAlign(0)
            .println("中国")
            .printNewLine(10)
        datas.add(printWriter.convertByteArray())
        return printWriter.convertByteArray()
    }

}