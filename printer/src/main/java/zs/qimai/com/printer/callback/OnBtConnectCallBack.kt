package zs.qimai.com.printer.callback

import zs.qimai.com.printer.manager.DeviceManager

interface OnBtConnectCallBack {
    fun onConnectStart()
    fun onConnectSuccess(mBlueToothDevice: DeviceManager?)
    fun onConnectError(errCode: Int,errMsg:String?=null)
}