package zs.qimai.com.printer2.executer

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import zs.qimai.com.printer2.callback.PrintCallBack
import zs.qimai.com.printer2.manager.DeviceManager
import zs.qimai.com.printer2.manager.DeviceManagerUtils

/***
 * 打印执行类
 * **/
open abstract class PrintExecutor {
    open abstract fun covertEscData(): ByteArray?
    abstract fun convertTscData(): ByteArray?
    var mPrintCallBack: PrintCallBack? = null
    private val TAG = "PrintExecutor"

    fun execute() {
        var count = DeviceManagerUtils.getInstance().lists.size
        if (count > 0) {
            covertEscData()?.let { data ->
                DeviceManagerUtils.getInstance().lists.filter {
                    it.mPrintMode == DeviceManager.ESC
                }?.forEach {
                    if (it.mStatus) {

                        Log.d(TAG, "execute: thread= ${Thread.currentThread().name}")
                        GlobalScope.launch(Dispatchers.Main) {
                            try {
                               goToPrint(it, data)
                                // PrintManagerUtils.getInstance().print(data)
                            } catch (e: Exception) {
                                mPrintCallBack?.printFailed(e.message)
                                it.closePort()
                                Log.d(TAG, "execute: print error e= $e")
                            }
                        }
                        //it.writeData(data)

                        mPrintCallBack?.printSuccess(it)

                    }
                }
            }
            convertTscData()?.let { data ->
                DeviceManagerUtils.getInstance().lists.filter {
                    it.mPrintMode == DeviceManager.TSC
                }?.forEach {
                    if (it.mStatus) {
                        Log.d(TAG, "execute: thread= ${Thread.currentThread().name}")
                        GlobalScope.launch(Dispatchers.Main) {
                            try {
                                goToPrint(it, data)
                                // PrintManagerUtils.getInstance().print(data)
                            } catch (e: Exception) {
                                mPrintCallBack?.printFailed(e.message)
                                it.closePort()
                                Log.d(TAG, "execute: print error e= $e")
                            }
                        }
                        //it.writeData(data)

                        mPrintCallBack?.printSuccess(it)
                    }
                }
            }

        } else {
            mPrintCallBack?.printFailed("当前未有可打印设备连接")
        }
    }

    private fun goToPrint(it: DeviceManager, data: ByteArray) = GlobalScope.async(Dispatchers.IO) {
        it.writeData(data)
    }
}