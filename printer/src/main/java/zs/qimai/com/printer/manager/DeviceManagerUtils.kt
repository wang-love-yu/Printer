package zs.qimai.com.printer.manager

import android.bluetooth.BluetoothDevice
import android.hardware.usb.UsbDevice
import android.util.Log
import zs.qimai.com.printer.callback.PrintConnOrDisCallBack
import zs.qimai.com.printer.manager.DeviceManager.Companion.BT
import zs.qimai.com.printer.manager.DeviceManager.Companion.USB
import java.util.HashSet

/*****
 * 打印设备管理
 *
 *
 */
class DeviceManagerUtils {

    private val TAG = "DeviceManagerUtils"
    private var mCallBacklist: ArrayList<PrintConnOrDisCallBack> = ArrayList()
    var lists: java.util.ArrayList<DeviceManager> = java.util.ArrayList()

    internal fun addDevice(deviceManager: DeviceManager?) {
        deviceManager?.let {
            lists.add(deviceManager)
            notifyaddObserver(it)
        }
    }

    internal fun removeDevice(device: DeviceManager) {
        var iterator = lists.listIterator()
        while (iterator.hasNext()) {
            val deviceManager = iterator.next()
            if (deviceManager == device) {
                iterator.remove()
                notifyRemoveObserver(device)
            }
        }
        //  lists.remove(deviceManager)
    }

    internal fun removeUsbDevice(device: UsbDevice) {

        lists.forEach {
            if (it is UsbDeviceManager) {
                if (it.usbDevice == device) {
                    //closePort()会清除所有配置项，并调用removeDevice()
                    it.closePort()
                    // notifyRemoveObserver(it)
                    //lists.remove(it)
                }
            }
        }

    }


    private fun getBtDevice(address: String): BlueDeviceManager? {
        if (lists.size > 0) {
            return null
        } else {
            lists.forEach {

                if (it.address == address && it is BlueDeviceManager) {
                    return it
                }
            }

        }
        return null
    }


    internal fun removeBtDevice(address: String) {
        var device = getBtDevice(address)
        device?.closePort()
    }

    private fun getUsbDevice(address: String): UsbDeviceManager? {
        if (lists.size > 0) {
            return null
        } else {
            lists.forEach {

                if (it.address == address && it is UsbDeviceManager) {
                    return it
                }
            }

        }
        return null
    }
    internal fun removeUsbDevice(address: String) {
        var device = getUsbDevice(address)
        device?.closePort()
    }

    internal fun removeBtDevice(bluetoothDevice: BluetoothDevice?) {

        if (bluetoothDevice == null) {
            return
        }
        /*if (lists.size > 0) {
            //迭代删除
            var iterator = lists.iterator()


            while (iterator.hasNext()) {
                val deviceManager = iterator.next()
                if (deviceManager is BlueDeviceManager && deviceManager.mBlueToothDevice == bluetoothDevice) {
                    deviceManager.closePort()
                }
            }
*/
        if (lists.size > 0) {
            lists.forEach {
                if (it is BlueDeviceManager) {
                    if (it.mBlueToothDevice == bluetoothDevice) {
                        //closePort()会清除所有配置项，并调用removeDevice()
                        it.closePort()
                        //notifyRemoveObserver(it)
                        //lists.remove(it)
                    }
                }
            }

        }
    }


    /***
     * 添加状态监听
     * **/
    fun addConnectStatusCallBack(callBack: PrintConnOrDisCallBack) {
        mCallBacklist.add(callBack)
    }

    /****
     * 移除状态监听
     * **/

    fun removeConnectStatusCallBack(callBack: PrintConnOrDisCallBack) {
        mCallBacklist.remove(callBack)
    }


    /****
     * 通知观察者们，该设备移除了
     * ***/
    private fun notifyRemoveObserver(deviceManager: DeviceManager) {
        Log.d(TAG, "notifyRemoveObserver: ${Log.getStackTraceString(Throwable())}")
        mCallBacklist?.forEach {
            it.onDisPrint(deviceManager)
        }
    }

    /****
     * 通知观察者们，该设备添加了
     * ***/
    private fun notifyaddObserver(deviceManager: DeviceManager) {
        Log.d(TAG, "notifyaddObserver: ${Log.getStackTraceString(Throwable())}")
        mCallBacklist?.forEach {
            it.onConectPrint(deviceManager)
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
        if (lists.size == 0) {
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

    //移除所有已连接蓝牙设备 主要使用于广播检测到蓝牙开关关掉
    fun removeAllBtDevice() {
        if (lists.size == 0) {
            return
        }
        lists.forEach {
            if (it.mType == DeviceManager.BT) {
                it.closePort()
            }
        }
    }

    //获得通过蓝牙连接的打印机数量
    fun getAccrodBtConNums(): Int {
        var nums = 0
        return if (lists.size == 0) {
            nums
        } else {
            lists.forEach {
                if (it.mType == BT) {
                    nums++
                }
            }
            nums
        }
    }

    //获得通过USB连接的打印机数量
    fun getAccrodUsbConNums(): Int {
        var nums = 0
        return if (lists.size == 0) {
            nums
        } else {
            lists.forEach {
                if (it.mType == USB) {
                    nums++
                }
            }
            nums
        }
    }
}