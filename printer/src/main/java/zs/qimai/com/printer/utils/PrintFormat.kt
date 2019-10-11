package zs.qimai.com.printer.utils

class PrintFormat {
    companion object {
        val INITBYTE = byteArrayOf(0x1b, 0x40)
        val LEFT_ALIGIN = byteArrayOf(0x1b, 97, 0)
        val CENTER_ALIGIN = byteArrayOf(0x1b, 97, 1)
        val RIGHT_ALIGIN = byteArrayOf(0x1b, 97, 2)
        val ESC_COMMAND = byteArrayOf(0x10, 0x04, 0x02)
        val TSC_COMMAND = byteArrayOf(0x1b, '!'.toByte(), '?'.toByte())

    }
}