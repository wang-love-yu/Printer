package zs.qimai.com.printer2.callback

import zs.qimai.com.printer2.manager.UsbDeviceManager

/***
 * usb连接状态回调
 * ***/
interface UsbPrintConnCallBack {
    fun onConnStart()
    fun onConnSucess(usbDeviceManager: UsbDeviceManager)
    fun onConnFailed(code: Int, error: String)
}