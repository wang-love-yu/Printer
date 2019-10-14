package zs.qimai.com.printer.manager

import android.hardware.usb.*
import android.util.Log
import android.widget.Toast
import zs.qimai.com.printer.callback.UsbPrintConnCallBack
import zs.qimai.com.printer.printStatus.PrintStatusCallBack
import zs.qimai.com.printer.printStatus.PrinterStatusUtils
import zs.qimai.com.printer.lifecycle.ActivityManagers
import zs.qimai.com.printer.utils.PrintManagerUtils.Companion.PRINT_MODE_NOT_SUPPORT
import zs.qimai.com.printer.utils.PrintManagerUtils.Companion.USB_CONN_FAILED
import zs.qimai.com.printer.utils.PrintManagerUtils.Companion.USB_DEVICE_INFO_NOT_FOUND

class UsbDeviceManager(override var mType: Int = USB) : DeviceManager() {
    private val TAG = "UsbDeviceManager"
    var usbDevice: UsbDevice? = null
    var usbManager: UsbManager? = null
    private var mmConnection: UsbDeviceConnection? = null
    private var mmIntf: UsbInterface? = null
    private var mmEndIn: UsbEndpoint? = null
    private var mmEndOut: UsbEndpoint? = null
    var mUsbPrintConnCallBack: UsbPrintConnCallBack? = null
    override fun openPort() {
        Log.d(TAG, "openPort: ")
        usbDevice?.let {
            if (it.interfaceCount != 0) {
                mmIntf = it.getInterface(0)
                if (mmIntf != null) {
                    this.mmConnection = this.usbManager?.openDevice(this.usbDevice)
                    if (this.mmConnection != null && this.mmConnection?.claimInterface(
                            mmIntf!!,
                            true
                        ) == true
                    ) {
                        for (i in 0 until mmIntf!!.endpointCount) {
                            val ep = mmIntf!!.getEndpoint(i)
                            if (ep.type == 2) {
                                if (ep.direction == 0) {
                                    this.mmEndOut = ep
                                } else {
                                    this.mmEndIn = ep
                                }
                            }
                        }
                    }
                }
            }
            if (this.mmEndOut != null && this.mmEndIn != null) {
                //检查打印机状态
                PrinterStatusUtils(this).apply {
                    mPrintStatusCallBack = object : PrintStatusCallBack {
                        override fun searchResult(status: Int?) {
                            if (status == null) {
                                mUsbPrintConnCallBack?.onConnFailed(
                                    PRINT_MODE_NOT_SUPPORT,
                                    "获取不到该打印机支持的模式"
                                )
                            } else {
                                mPrintMode = status
                                mStatus = true
                                DeviceManagerUtils.getInstance().addDevice(this@UsbDeviceManager)
                                mUsbPrintConnCallBack?.onConnSucess(this@UsbDeviceManager)
                            }
                        }
                    }
                }.queryStatus()

            } else {
                mUsbPrintConnCallBack?.onConnFailed(USB_CONN_FAILED, "连接失败")

            }
        } ?: mUsbPrintConnCallBack?.onConnFailed(USB_DEVICE_INFO_NOT_FOUND, "获取不到USB设备信息")

    }

    override fun closePort() {
        mStatus = false
        mUsbPrintConnCallBack = null
        DeviceManagerUtils.getInstance().removeDevice(this@UsbDeviceManager)
        if (this.mmIntf != null && this.mmConnection != null) {
            this.mmConnection!!.releaseInterface(this.mmIntf)
            this.mmConnection!!.close()
            this.mmConnection = null
        }
    }

    override fun readData(bytes: ByteArray): Int {
        return if (this.mmConnection != null) this.mmConnection!!.bulkTransfer(
            this.mmEndIn,
            bytes,
            bytes.size,
            200
        ) else 0
    }

    override fun writeData(bytes: ByteArray) {
         this.mmConnection!!.bulkTransfer(
            this.mmEndOut,
            bytes,
            bytes.size,
            1000
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other != null && other is UsbDeviceManager) {
            if ((other.address == this.address) && other.usbDevice == this.usbDevice) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {

        return address?.hashCode() ?: 0 + usbDevice.hashCode()
    }

    override fun toString(): String {
        return "UsbDeviceManager(mType=$mType, TAG='$TAG', usbDevice=$usbDevice, usbManager=$usbManager, mmConnection=$mmConnection, mmIntf=$mmIntf, mmEndIn=$mmEndIn, mmEndOut=$mmEndOut, mUsbPrintConnCallBack=$mUsbPrintConnCallBack)"
    }


}