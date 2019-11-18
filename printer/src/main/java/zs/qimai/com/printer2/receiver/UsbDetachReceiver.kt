package zs.qimai.com.printer2.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import zs.qimai.com.printer2.utils.PrintManagerUtils
import zs.qimai.com.printer2.manager.DeviceManagerUtils

class UsbDetachReceiver : BroadcastReceiver() {
    private val TAG = "UsbDetachReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d(TAG, "onReceive: intent.action = ${intent.action}")
        //如果存在移除usb设备，则需要去连接设备管理中移除
        if (intent.action == UsbManager.ACTION_USB_DEVICE_DETACHED) {
            val mUsbDevice = intent.getParcelableExtra<UsbDevice?>(UsbManager.EXTRA_DEVICE)
            Log.d(TAG, "onReceive:  ACTION_USB_DEVICE_DETACHED mUsbDevice = ${mUsbDevice}")
            mUsbDevice?.let {
                DeviceManagerUtils.getInstance().removeUsbDevice(it)
            }

        }
        if (intent.action == UsbManager.ACTION_USB_DEVICE_ATTACHED) {
            val mUsbDevice = intent.getParcelableExtra<UsbDevice?>(UsbManager.EXTRA_DEVICE)
            if (PrintManagerUtils.getInstance().detachUsbDeviceAutoConn) {
                PrintManagerUtils.getInstance().usbConnect(mUsbDevice?.deviceName!!, mUsbDevice)
            }
        }
    }
}
