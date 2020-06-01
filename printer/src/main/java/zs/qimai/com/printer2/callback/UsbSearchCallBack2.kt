package zs.qimai.com.printer2.callback

import android.hardware.usb.UsbDevice
import zs.qimai.com.printer2.manager.DeviceManager

/****
 * 扩展UsbSearchCallBack 新增onSearchFoundAll方法 用于直接返回支持Usb打印的设备列表
 * */
interface UsbSearchCallBack2 : UsbSearchCallBack {
    fun onSearchFoundAll(devices: HashMap<String, UsbDevice>?)
}