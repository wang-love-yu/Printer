package zs.qimai.com.printer.callback

import zs.qimai.com.printer.manager.DeviceManager
import zs.qimai.com.printer.manager.UsbDeviceManager

interface PrintCallBack {

    fun printSucess(deviceManager: DeviceManager)
    fun printFailed(msg:String? = null)
}