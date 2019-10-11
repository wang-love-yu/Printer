package zs.qimai.com.printer.callback

import android.hardware.usb.UsbDevice

interface UsbSearchCallBack {
    fun onSearchStart()
    fun onSearchFound(address:String,usbDevice: UsbDevice)
    fun onSearchError(errCode: Int,errMsg:String?=null)
    fun onSearchFinish()

}