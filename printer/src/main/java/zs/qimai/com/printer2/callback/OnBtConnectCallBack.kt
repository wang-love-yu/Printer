package zs.qimai.com.printer2.callback

import zs.qimai.com.printer2.manager.DeviceManager

interface OnBtConnectCallBack {
    fun onConnectStart()
    fun onConnectSuccess(mBlueToothDevice: DeviceManager?)
    fun onConnectError(errCode: Int,errMsg:String?=null)
}