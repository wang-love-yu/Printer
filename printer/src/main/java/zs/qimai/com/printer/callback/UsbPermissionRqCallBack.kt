package zs.qimai.com.printer.callback

interface UsbPermissionRqCallBack {
    fun rqSuccess()
    fun reqFailed(message: String? = null)
}