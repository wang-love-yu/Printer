package zs.qimai.com.printer.utils

import android.app.Application
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import zs.qimai.com.printer.threadPool.PrintThreadPool
import zs.qimai.com.printer.fragment.BlueToothLists
import zs.qimai.com.printer.lifecycle.ActivityManagers
import zs.qimai.com.printer.manager.BlueDeviceManager
import zs.qimai.com.printer.manager.DeviceManagerUtils
import java.io.IOException
import kotlin.collections.ArrayList
import android.widget.Toast
import zs.qimai.com.printer.callback.*
import zs.qimai.com.printer.lifecycle.PrintLifeCycleCallBack
import zs.qimai.com.printer.manager.UsbDeviceManager
import zs.qimai.com.printer.receiverManager.UsbRqPermissionReceiverManager
import java.lang.RuntimeException


/****
 * 获取 连接蓝牙，usb设备
 * 打印
 * **/
class PrintManagerUtils {
    private val TAG = "PrintManagerUtils"

    //获取蓝牙列表
    var application: Application? = null

    fun init(application: Application) {
        this.application = application
        application.registerActivityLifecycleCallbacks(PrintLifeCycleCallBack())

    }

    /**
     * 检查是否在application初始化过
     * **/
    fun checkIsInit() {
        if (this.application == null) {
            throw RuntimeException("请在Application 先调用init()")
        }
    }

    fun getSearchBlueToothList(callBack: BlueToothSearchCallBack? = null) {
        checkIsInit()
        var topActivity = ActivityManagers.getInstance().getTopActivity()
        topActivity?.let {
            BlueToothLists(it).getBlueToothList(callBack)
        } ?: callBack?.searchFailed(ACTIVITY_NOT_FOUND, "获取不到Activity")
    }

    //蓝牙连接
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun btConnect(
        address: String,
        mOnBtConnectCallBack: OnBtConnectCallBack? = null
    ) {
        checkIsInit()

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.cancelDiscovery()
        BlueDeviceManager().apply {
            this.address = address
            this.mOnBtConnectCallBack = mOnBtConnectCallBack
        }.openPort()
    }

    fun getSearchUsbList(usbSearchCallBack: UsbSearchCallBack? = null) {
        checkIsInit()

        usbSearchCallBack?.onSearchStart()
        ActivityManagers.getInstance().getTopActivity()?.let {
            var manager: UsbManager = it.getSystemService(Context.USB_SERVICE) as UsbManager
            var map = manager.deviceList
            for ((key, value) in map) {

                Log.d(TAG, "getSearchUsbList: key= $key value= $value")
                //等于7说明是打印设备
                if (value.getInterface(0).interfaceClass == 7) {
                    usbSearchCallBack?.onSearchFound(key, value)
                }
            }
            usbSearchCallBack?.onSearchFinish()

        }
    }

    fun usbConnect(
        address: String,
        usbDevice: UsbDevice,
        mUsbPrintConnCallBack: UsbPrintConnCallBack? = null
    ) {
        checkIsInit()
        //连接开始
        mUsbPrintConnCallBack?.onConnStart()
        //先判断当前设备是否已经保存过
        if (DeviceManagerUtils.getInstance().isContainerUsbDevice(address, usbDevice)) {
            Log.d(TAG, "usbConnect: this device is bind")
            mUsbPrintConnCallBack?.onConnFailed(USB_DEVICE_ALREAD_CONN, "该设备已经连接过")
            return
        }

        ActivityManagers.getInstance().getTopActivity()?.let {
            var manager = it.getSystemService(Context.USB_SERVICE) as UsbManager
            //检查权限
            if (hasUsbPermission(manager, usbDevice)) {
                createUsbConnect(manager, address, usbDevice)
                Log.d(TAG, "usbConect: $usbDevice has permission")
            } else {
                Log.d(TAG, "usbConect: $usbDevice has no permission")
                //无权限，申请权限
                val mPermissionIntent =
                    PendingIntent.getBroadcast(it, 0, Intent(ACTION_USB_PERMISSION), 0)
                //权限申请广播类 用来监听用户是否授权权限
                UsbRqPermissionReceiverManager(it).apply {
                    mUsbPermissionRqCallBack = object : UsbPermissionRqCallBack {
                        override fun rqSuccess() {
                            createUsbConnect(manager, address, usbDevice)
                        }

                        override fun reqFailed(message: String?) {
                            Log.d(TAG, "reqFailed: mesage= $message")
                            mUsbPrintConnCallBack?.onConnFailed(
                                REFUSE_USB_PERMISSION,
                                "用户拒绝授权USB权限"
                            )
                        }
                    }
                }
                //申请权限
                manager.requestPermission(usbDevice, mPermissionIntent)
            }
        } ?: mUsbPrintConnCallBack?.onConnFailed(ACTIVITY_NOT_FOUND, "获取不到Activity")
    }

    /****
     * 正式进行usb连接
     * */
    private fun createUsbConnect(
        manager: UsbManager,
        address: String,
        usbDevice: UsbDevice,
        mUsbPrintConnCallBack: UsbPrintConnCallBack? = null
    ) {
        checkIsInit()

        UsbDeviceManager().apply {
            this.usbDevice = usbDevice
            this.address = address
            this.usbManager = manager
            this.mUsbPrintConnCallBack = mUsbPrintConnCallBack
        }.openPort()


    }

    //检查权限
    private fun hasUsbPermission(
        context: UsbManager,
        usbDevice: UsbDevice
    ): Boolean {

        return context.hasPermission(usbDevice)
    }


    fun print(data: ArrayList<ByteArray>) {
        //未连接
        for (item in DeviceManagerUtils.getInstance().lists) {
            if (item.mStatus && data.isNotEmpty()) {
                try {
                    for (i in data) {
                        item.writeData(i)
                    }
                } catch (e: IOException) {
                    //打印异常 关闭连接
                    Log.d(TAG, "print: e= ${e.message}")
                    item.closePort()
                    // throw IOException("打印失败")
                }

            }

        }
    }

    /***
     * 遍历获取设备打印
     * **/
    fun print(data: ByteArray) {

     //   for (item in DeviceManagerUtils.getInstance().lists) {

            //item.writeData(data)

            /*    PrintThreadPool.getInstance().addTask(Runnable {
                    Log.d(TAG, "doPrint: threadName= ${Thread.currentThread().name}")
                    if (item.mStatus && data.isNotEmpty()) {
                        try {
                            Log.d(TAG, "print: print beigin threadName= ${Thread.currentThread().name}")
                            item.writeData(data)
                        } catch (e: IOException) {
                            //打印异常 关闭连接
                            item.closePort()
                            // throw IOException("打印失败")
                        }

                        Log.d(TAG, "print: finish threadName= ${Thread.currentThread().name}")
                    }
                })*/


     //   }


    }

    companion object {
        private val PRINT_MANAGER_UTILS: PrintManagerUtils = PrintManagerUtils()
        fun getInstance(): PrintManagerUtils {
            return PRINT_MANAGER_UTILS
        }

        val ACTION_USB_PERMISSION = "zs.qimai.com.printer.USB_PERMISSION"

        //该硬件不支持蓝牙
        const val HARDWARE_NOT_SUPPORT = 11
        //根据地址获取不到设备
        const val DEVICE_NOT_FIND = 12
        //弹窗异常或者密码错误
        const val BOND_NONE = 13
        //获取不到socket
        const val SOCKET_NOT_FOUND = 14
        //socket连接异常
        const val SOCKET_ERROR = 15
        //获取流失败
        const val IO_ERROR = 16
        //获取不到Activity
        const val ACTIVITY_NOT_FOUND = 17
        //获取不到该打印机支持的模式 ps ESC(小票) TSC(标签)
        const val PRINT_MODE_NOT_SUPPORT = 18
        //该设备已经连接过
        const val BT_DEVICE_ALREAD_CONN = 19

        //usb相关
        //该设备已经连接过
        const val USB_DEVICE_ALREAD_CONN = 21
        //用户拒绝授权USB权限
        const val REFUSE_USB_PERMISSION = 21
        //获取不到USB设备信息
        const val USB_DEVICE_INFO_NOT_FOUND = 22
        //连接失败
        const val USB_CONN_FAILED = 23
    }


}