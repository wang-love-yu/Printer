package zs.qimai.com.printer.callback

import zs.qimai.com.printer.manager.UsbDeviceManager

/***
 * usb连接状态回调
 * ***/
interface UsbPrintConnCallBack {
    fun onConnStart()
    fun onConnSucess(usbDeviceManager: UsbDeviceManager)
    fun onConnFailed(code: Int, error: String)
}