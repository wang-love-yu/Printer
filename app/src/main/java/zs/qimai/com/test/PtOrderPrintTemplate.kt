package zs.qimai.com.test

import zs.qimai.com.printer2.canvas.PrintTemplate

class PtOrderPrintTemplate:PrintTemplate<Any> {
    override fun getPrintData(data: Any): ByteArray? {
        return null
    }
}