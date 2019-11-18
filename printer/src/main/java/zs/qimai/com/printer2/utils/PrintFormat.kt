package zs.qimai.com.printer2.utils

class PrintFormat {
    companion object {
        val INITBYTE = byteArrayOf(0x1b, 0x40)
        val LEFT_ALIGIN = byteArrayOf(0x1b, 97, 0)
        val CENTER_ALIGIN = byteArrayOf(0x1b, 97, 1)
        val RIGHT_ALIGIN = byteArrayOf(0x1b, 97, 2)
        val ESC_COMMAND = byteArrayOf(0x10, 0x04, 0x02)
        val TSC_COMMAND = byteArrayOf(0x1b, '!'.toByte(), '?'.toByte())

        val FONT_BOLD = byteArrayOf(0x1b, 0x45, 0x01)
        val FONT_NORMAL = byteArrayOf(0x1b, 0x45, 0x00)

        val FONT_TYPE_0 = byteArrayOf(0x1d, 0x21, 0x00)
        val FONT_TYPE_1 = byteArrayOf(0x1d, 0x21, 0x10)
        val FONT_TYPE_2 = byteArrayOf(0x1d, 0x21, 0x01)
        val FONT_TYPE_3 = byteArrayOf(0x1d, 0x21, 17)


    }
}