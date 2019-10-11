package zs.qimai.com.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import zs.qimai.com.printer.callback.PrintCallBack
import zs.qimai.com.printer.canvas.LabelTemplete
import zs.qimai.com.printer.canvas.TestPrintTemplate
import zs.qimai.com.printer.executer.PrintExecutor
import zs.qimai.com.printer.manager.DeviceManager

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_connect_bt.setOnClickListener {
            startActivity(Intent(this, BtListActivity::class.java))
        }
        tv_print.setOnClickListener {
            //PrintExecutor().doPrint(TestPrintTemplate())
            // PrintExecutor().doPrint(LabelTemplete())
            val task = object : PrintExecutor() {
                override fun covertEscData(): ByteArray? {
                    return TestPrintTemplate().getPrintData()
                }

                override fun convertTscData(): ByteArray? {
                    return LabelTemplete().getPrintData()
                }
            }
            task.mPrintCallBack = object : PrintCallBack {
                override fun printSucess(deviceManager: DeviceManager) {
                    Log.d(TAG, "printSucess: deviceManager = $deviceManager")
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
}
