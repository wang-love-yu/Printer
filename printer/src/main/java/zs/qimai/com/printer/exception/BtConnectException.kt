package zs.qimai.com.printer.exception

import java.io.IOException

class BtConnectException(val code: Int, override val message: String = "") : IOException(message)
