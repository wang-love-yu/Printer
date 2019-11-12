package zs.qimai.com.printer.manager

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*
import zs.qimai.com.printer.printStatus.PrintStatusCallBack
import zs.qimai.com.printer.printStatus.PrinterStatusUtils
import zs.qimai.com.printer.utils.PrintFormat
import zs.qimai.com.printer.utils.PrintManagerUtils
import zs.qimai.com.printer.utils.PrintManagerUtils.Companion.ACTIVITY_NOT_FOUND
import zs.qimai.com.printer.utils.PrintManagerUtils.Companion.BOND_NONE
import zs.qimai.com.printer.utils.PrintManagerUtils.Companion.PRINT_MODE_NOT_SUPPORT
import zs.qimai.com.printer.callback.BlueToothPwdCallBack
import zs.qimai.com.printer.callback.OnBtConnectCallBack
import zs.qimai.com.printer.lifecycle.ActivityManagers
import zs.qimai.com.printer.receiverManager.BlueToothPwdReceiverManager
import java.io.IOException
import java.lang.Exception
import java.util.*

class BlueDeviceManager(override var mType: Int = BT) : DeviceManager() {

    private var len = 0
    var mBlueToothDevice: BluetoothDevice? = null
    private var mBlueAdapter: BluetoothAdapter? = null
    private var mBluetoothSocket: BluetoothSocket? = null
    var mOnBtConnectCallBack: OnBtConnectCallBack? = null
    private val TAG = "BlueDeviceManager"
    private var disposable: Disposable? = null
    private val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")//蓝牙打印UUID
    private var job: Job? = null
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun openPort() {
        mOnBtConnectCallBack?.onConnectStart()
        //先判断当前设备是否已经保存过
        if (DeviceManagerUtils.getInstance().isContainerBtDevice(address!!)) {
            Log.d(TAG, "usbConnect: this device is bind")
            mOnBtConnectCallBack?.onConnectError(
                PrintManagerUtils.BT_DEVICE_ALREAD_CONN,
                "该设备已经连接过"
            )
            return
        }
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter()
        mBlueAdapter?.let {
            it.cancelDiscovery()
            mBlueToothDevice = it.getRemoteDevice(address)
        }
            ?: mOnBtConnectCallBack?.onConnectError(
                PrintManagerUtils.HARDWARE_NOT_SUPPORT, "该硬件不支持蓝牙"
            )
        mBlueToothDevice?.let { it ->
            //这里判断是否需要弹出密码输入弹窗
            var isVerify: Boolean = it.createBond()
            //如果不需要就直接去连接
            if (!isVerify) {
                connectBt(it)
            } else {
                //获取顶部Activity
                ActivityManagers.getInstance().getTopActivity()?.let { activity ->
                    //去广播中等待状态
                    BlueToothPwdReceiverManager(activity)
                        .callBack = object : BlueToothPwdCallBack {
                        override fun onInputStatus(state: Int) {
                            Log.d(TAG, "onInputSuccess: state= $state")
                            when (state) {
                                BluetoothDevice.BOND_BONDING -> {
                                    Log.d(TAG, "onInputSuccess: BOND_BONDING")
                                }
                                BluetoothDevice.BOND_BONDED -> {
                                    Log.d(TAG, "onInputSuccess: BOND_BONDED")
                                    //密码验证成功，继续去连接
                                    connectBt(it)
                                }
                                BluetoothDevice.BOND_NONE -> {
                                    Log.d(TAG, "onInputSuccess: BOND_NONE")
                                    mOnBtConnectCallBack?.onConnectError(BOND_NONE, "弹窗异常或者密码错误")
                                    //mOnBtConnectCallBack?.onConnectError(BOND_NONE, "弹窗异常或者密码错误")
                                }
                            }
                        }
                    }
                } ?: mOnBtConnectCallBack?.onConnectError(ACTIVITY_NOT_FOUND, "获取不到Activity实例")
            }
            //mOnBtConnectCallBack?.onConnectError(DEVICE_NOT_FIND, "根据地址获取不到设备")
        }
    }

    override fun closePort() {
        DeviceManagerUtils.getInstance().removeDevice(this@BlueDeviceManager)
        mStatus = false
        job?.cancel()
        mOutPutStream = try {
            mOutPutStream?.close()
            null
        } catch (e: Exception) {
            null
        }

        mInPutStream = try {
            mInPutStream?.close()
            null
        } catch (e: Exception) {
            null
        }
        mBluetoothSocket = try {
            mBluetoothSocket?.close()
            null
        } catch (e: Exception) {
            null
        }
    }

    override fun readData(bytes: ByteArray): Int {
        return if (this.mInPutStream == null) {
            -1
        } else if (this.mInPutStream!!.available() > 0) {
            this.len = this.mInPutStream!!.read(bytes)
            this.len
        } else {
            if (this.mInPutStream!!.available() === -1) -1 else 0
        }
    }


    private fun connect(it: BluetoothDevice) {


        GlobalScope.launch {


        }


    }

    /****
     * 这里是去连接打印机
     * ***/
    private fun connectBt(it: BluetoothDevice) {

        GlobalScope.launch(Dispatchers.Main) {
            mBluetoothSocket = getBtSocket(it)
            if (mBluetoothSocket != null) {
                try {
                    withContext(Dispatchers.IO) {
                        mBluetoothSocket!!.connect()
                    }
                    mOutPutStream = mBluetoothSocket!!.outputStream
                    mInPutStream = mBluetoothSocket!!.inputStream
                    //到这里说明配对并连接成功 判断打印机模式
                    PrinterStatusUtils(this@BlueDeviceManager).apply {
                        mPrintStatusCallBack = object : PrintStatusCallBack {
                            override fun searchResult(status: Int?) {
                                Log.d(TAG, "searchResult: status= $status")
                                status?.let {
                                    handleConnectSuccess(status)
                                } ?: handleUnknownStatus()
                            }
                        }
                        queryStatus()
                    }
                } catch (e: IOException) {
                    Log.d(TAG, "connectBt: e= $e")
                    mOnBtConnectCallBack?.onConnectError(
                        PrintManagerUtils.SOCKET_ERROR,
                        "socket连接异常"
                    )
                    closePort()
                    mStatus = false
                }
            } else {
                mOnBtConnectCallBack?.onConnectError(
                    PrintManagerUtils.SOCKET_NOT_FOUND,
                    "获取不到socket"
                )
            }
        }
        /* mBluetoothSocket = it.createRfcommSocketToServiceRecord(uuid)
         mBluetoothSocket?.let {
             try {
                 //发起连接
                 it.connect()
                 try {
                     mOutPutStream = it.outputStream
                     mInPutStream = it.inputStream
                 } catch (e: IOException) {
                     mOnBtConnectCallBack?.onConnectError(PrintManagerUtils.IO_ERROR, "获取流失败")
                     closePort()
                     mStatus = false
                 }

             } catch (e: IOException) {
                 mOnBtConnectCallBack?.onConnectError(PrintManagerUtils.SOCKET_ERROR, "socket连接异常")
                 closePort()
                 mStatus = false
             }

             //到这里说明配对并连接成功 判断打印机模式
             PrinterStatusUtils(this).apply {
                 mPrintStatusCallBack = object : PrintStatusCallBack {
                     override fun searchResult(status: Int?) {
                         Log.d(TAG, "searchResult: status= $status")
                         status?.let {
                             handleConnectSuccess(status)
                         } ?: handleUnknownStatus()
                     }
                 }
                 queryStatus()
             }


         } ?: mOnBtConnectCallBack?.onConnectError(PrintManagerUtils.SOCKET_NOT_FOUND, "获取不到socket")*/
    }


    private suspend fun getBtSocket(it: BluetoothDevice) =
        withContext(Dispatchers.IO) {
            it.createRfcommSocketToServiceRecord(uuid)
        }

    override fun writeData(bytes: ByteArray) {
        this.mOutPutStream?.write(bytes)
        this.mOutPutStream?.flush()
    }

    /****
     * 配置一些成功连接属性
     * */
    private fun handleConnectSuccess(status: Int) {
        mStatus = true
        mPrintMode = status
        mOnBtConnectCallBack?.onConnectSuccess(this@BlueDeviceManager)
        DeviceManagerUtils.getInstance().addDevice(this)
        //timeGetBtPrintDeviceStatus()

    }

    /******
     * 测试通过以下打印捕获异常与收到android.bluetooth.device.action.ACL_DISCONNECTED时间差不多，所以采用广播方式移除蓝牙设备 {@link BlueToothStatusReceiver}
     * ***/
    private fun timeGetBtPrintDeviceStatus() {
        job = GlobalScope.launch(Dispatchers.IO) {
            while (mStatus) {
                try {
                    writeData(PrintFormat.INITBYTE)
                    Log.d(TAG, "timeGetBtPrintDeviceStatus: ")
                    delay(5000)
                } catch (e: Exception) {
                    Log.d(TAG, "timeGetBtPrintDeviceStatus: e= $e")
                    job?.cancel()
                    closePort()
                    break
                }
            }
        }

        /*  Observable.interval(5L, TimeUnit.SECONDS)
              .map {
                  writeData(PrintFormat.INITBYTE)
                  true
              }
              .subscribe(object : Observer<Boolean> {
                  override fun onComplete() {

                  }

                  override fun onSubscribe(d: Disposable) {
                      disposable = d
                  }

                  override fun onNext(t: Boolean) {
                      Log.d(TAG, "onNext: t= $t")
                  }

                  override fun onError(e: Throwable) {
                      Log.d(TAG, "onError: e= ${e.message}")
                      disposable = null
                  }
              })*/
    }

    private fun handleUnknownStatus() {
        mOnBtConnectCallBack?.onConnectError(
            PRINT_MODE_NOT_SUPPORT,
            "获取不到改打印机支持的模式"
        )
        closePort()
    }

    override fun hashCode(): Int {
        return address.hashCode() + mBlueToothDevice.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other != null && other is BlueDeviceManager) {
            if ((other.address == this.address) && other.mBlueToothDevice == this.mBlueToothDevice) {
                return true
            }
        }
        return false
    }

    override fun toString(): String {
        return "BlueDeviceManager(mType=$mType, len=$len, mBlueToothDevice=$mBlueToothDevice, mBlueAdapter=$mBlueAdapter, mBluetoothSocket=$mBluetoothSocket, mOnBtConnectCallBack=$mOnBtConnectCallBack, TAG='$TAG', disposable=$disposable, uuid=$uuid, job=$job)"
    }


}