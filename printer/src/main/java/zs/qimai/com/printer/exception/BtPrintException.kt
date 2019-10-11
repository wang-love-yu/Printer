package zs.qimai.com.printer.exception

import java.io.IOException

class BtPrintException(val code: Int, override val message: String = "") : IOException(message)
