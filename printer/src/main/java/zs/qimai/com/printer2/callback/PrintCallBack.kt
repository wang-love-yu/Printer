package zs.qimai.com.printer2.callback

import zs.qimai.com.printer2.manager.DeviceManager

interface PrintCallBack {

    fun printSuccess(deviceManager: DeviceManager)
    fun printFailed(msg:String? = null)
}