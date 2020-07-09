package zs.qimai.com.test

import android.content.Intent
import android.hardware.usb.UsbDevice
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import zs.qimai.com.printer2.callback.PrintCallBack
import zs.qimai.com.printer2.callback.PrintConnOrDisCallBack
import zs.qimai.com.printer2.callback.UsbPrintConnCallBack
import zs.qimai.com.printer2.callback.UsbSearchCallBack
import zs.qimai.com.printer2.canvas.LabelTemplete
import zs.qimai.com.printer2.canvas.TestPrintTemplate
import zs.qimai.com.printer2.executer.PrintExecutor
import zs.qimai.com.printer2.manager.DeviceManager
import zs.qimai.com.printer2.manager.DeviceManager.Companion.BT
import zs.qimai.com.printer2.manager.DeviceManager.Companion.USB
import zs.qimai.com.printer2.manager.DeviceManagerUtils
import zs.qimai.com.printer2.manager.UsbDeviceManager
import zs.qimai.com.printer2.utils.PrintManagerUtils

class MainActivity : AppCompatActivity(), PrintConnOrDisCallBack {

    private var list = ArrayList<DeviceManager>()

    override fun onConectPrint(device: DeviceManager) {
        list.add(device)
        Log.d(TAG, "onConectPrint: device= $device")
    }

    override fun onDisPrint(device: DeviceManager) {
        list.remove(device)
        Log.d(TAG, "onDisPrint: device= $device")
    }

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        DeviceManagerUtils.getInstance().addConnectStatusCallBack(this)
        initUsbPrinter()
        tv_connect_bt.setOnClickListener {
            startActivity(Intent(this, BtListActivity::class.java))
        }
        tv_print.setOnClickListener {
            //PrintExecutor().doPrint(TestPrintTemplate())
            // PrintExecutor().doPrint(LabelTemplete())
            val task = object : PrintExecutor() {
                override fun covertEscData(): ByteArray? {
                    return TestPrintTemplate().getPrintData("122")
                }

                override fun convertTscData(): ByteArray? {
                    return LabelTemplete().getPrintData("Test")
                }
            }
            task.mPrintCallBack = object : PrintCallBack {
                override fun printSuccess(deviceManager: DeviceManager) {
                    Log.d(TAG, "printSuccess: deviceManager = $deviceManager")
                }

                override fun printFailed(msg: String?) {
                    Log.d(TAG, "printFailed: msg= $msg")
                }
            }
            task.execute()
        }
        tv_connect_usb.setOnClickListener {
            startActivity(Intent(this, UsbActivity::class.java))
        }

        tv_disconnect_bt.setOnClickListener {
            list.forEach {
                if (it.mType == BT) {
                    PrintManagerUtils.getInstance().closeBtConn(it.address!!)
                }
            }
        }

        tv_disconnect_usb.setOnClickListener {
            list.forEach {
                if (it.mType == USB) {
                    PrintManagerUtils.getInstance().closeUsbConn(it.address!!)
                }
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            one()
            delay(2000)
            two()
        }
        GlobalScope.launch {
            val one = GlobalScope.async {
                Log.d(TAG, "onCreate: async1 delay before")
                delay(2000)
                Log.d(TAG, "onCreate: async1 delay after")
            }
            GlobalScope.async {
                Log.d(TAG, "onCreate: async2 delay before")
                delay(2000)
                Log.d(TAG, "onCreate: async2 delay after")
            }

        }
    }


    private fun initUsbPrinter() {
        val usbDevices = mutableMapOf<String, UsbDevice>()
        PrintManagerUtils.getInstance()
            .getSearchUsbList(object : UsbSearchCallBack {
                override fun onSearchStart() {}
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                override fun onSearchFound(
                    s: String,
                    usbDevice: UsbDevice
                ) {

                    if (!usbDevice.manufacturerName?.trim().equals("POSIN")) {
                        usbDevices[s] = usbDevice
                    }
                }

                override fun onSearchError(i: Int, s: String?) {}
                override fun onSearchFinish() {

                    usbDevices.forEach { entry ->
                        PrintManagerUtils.getInstance()
                            .usbConnect(entry.key, entry.value, object : UsbPrintConnCallBack {
                                override fun onConnStart() {}
                                override fun onConnSucess(usbDeviceManager: UsbDeviceManager) {
                                    Log.d("devicesBlue", "lianjie")
                                    Log.d("devicesBlue", "${entry.value.toString()}")
                                }

                                override fun onConnFailed(i: Int, s: String) {
                                    Log.d("devicesBlue", "fail")

                                }
                            })
                    }

                }
            })
    }
    private suspend fun two() {
        withContext(Dispatchers.IO) {
            Log.d(TAG, "two: ")
        }
    }

    private suspend fun one() {
        withContext(Dispatchers.IO) {
            for (i in 0..500000000) {
                if (i == 0 || i == 500000000) {
                    Log.d(TAG, "one: ")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DeviceManagerUtils.getInstance().removeConnectStatusCallBack(this)

    }
}
