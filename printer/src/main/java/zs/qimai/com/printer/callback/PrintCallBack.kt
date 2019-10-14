package zs.qimai.com.printer.callback

import zs.qimai.com.printer.manager.DeviceManager
import zs.qimai.com.printer.manager.UsbDeviceManager

interface PrintCallBack {

    fun printSuccess(deviceManager: DeviceManager)
    fun printFailed(msg:String? = null)
}