package zs.qimai.com.printer2.callback

import android.hardware.usb.UsbDevice

interface UsbSearchCallBack {
    fun onSearchStart()
    fun onSearchFound(address:String,usbDevice: UsbDevice)
    fun onSearchError(errCode: Int,errMsg:String?=null)
    fun onSearchFinish()

}