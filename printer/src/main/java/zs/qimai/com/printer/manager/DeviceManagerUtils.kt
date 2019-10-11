package zs.qimai.com.printer.manager

import android.bluetooth.BluetoothDevice
import android.hardware.usb.UsbDevice
import java.util.HashSet

/*****
 * 打印设备管理
 *
 *
 */
class DeviceManagerUtils {
    var lists: HashSet<DeviceManager> = HashSet()

    fun addDevice(deviceManager: DeviceManager?) {
        deviceManager?.let {
            lists.add(deviceManager)
        }
    }

    fun removeDevice(deviceManager: DeviceManager) {
        lists.remove(deviceManager)
    }

    fun removeBtDevice(bluetoothDevice: BluetoothDevice) {
        if (lists.size > 0) {
            lists.forEach {
                if (it is BlueDeviceManager) {
                    if (it.mBlueToothDevice == bluetoothDevice) {
                        //先关闭 在移除
                        it.closePort()
                        lists.remove(it)
                    }
                }
            }

        }
    }


    fun removeUsbDevice(device: UsbDevice) {
        if (lists.size > 0) {
            lists.forEach {
                if (it is UsbDeviceManager) {
                    if (it.usbDevice == device) {
                        it.closePort()
                        lists.remove(it)
                    }
                }
            }

        }
    }

    companion object {
        private var deviceManagerUtils = DeviceManagerUtils()
        fun getInstance(): DeviceManagerUtils {
            return deviceManagerUtils
        }
    }

    /***
     * 判断是否已经连接的usb设备
     * */
    fun isContainerUsbDevice(address: String, mUsbDevice: UsbDevice): Boolean {
        if (lists.size == null) {
            return false
        }
        lists.forEach {
            if (it is UsbDeviceManager) {
                if (it.address == address && it.usbDevice == mUsbDevice) {
                    return true
                }
            }
        }
        return false
    }

    /***
     * 判断是否已经连接的蓝牙设备
     * */
    fun isContainerBtDevice(address: String): Boolean {
        if (lists.size == null) {
            return false
        }
        lists.forEach {
            if (it is BlueDeviceManager) {
                if (it.address == address) {
                    return true
                }
            }
        }
        return false
    }

}