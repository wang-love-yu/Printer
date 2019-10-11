package zs.qimai.com.printer.callback

import zs.qimai.com.printer.manager.DeviceManager

/****
 * 回调 当前设备连接或者移除打印机
 * **/
interface PrintConnOrDisCallBack {
    //当连接上打印机时候回调
    fun onConectPrint(device: DeviceManager)

    //当断开打印机时回调
    fun onDisPrint(device: DeviceManager)
}