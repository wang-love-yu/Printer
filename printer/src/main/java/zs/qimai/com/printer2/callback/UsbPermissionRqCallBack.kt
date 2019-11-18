package zs.qimai.com.printer2.callback

interface UsbPermissionRqCallBack {
    fun rqSuccess()
    fun reqFailed(message: String? = null)
}