package zs.qimai.com.printer.executer

import zs.qimai.com.printer.callback.PrintCallBack
import zs.qimai.com.printer.manager.DeviceManager
import zs.qimai.com.printer.manager.DeviceManagerUtils

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
                        try {
                            it.writeData(data)
                        } catch (e: Exception) {
                            mPrintCallBack?.printFailed(e.message)
                            it.closePort()
                        }
                        mPrintCallBack?.printSucess(it)

                    }
                }
            }
            convertTscData()?.let { data ->
                DeviceManagerUtils.getInstance().lists.filter {
                    it.mPrintMode == DeviceManager.TSC
                }?.forEach {
                    if (it.mStatus) {
                        if (it.mStatus) {
                            try {
                                it.writeData(data)
                            } catch (e: Exception) {
                                mPrintCallBack?.printFailed(e.message)
                                it.closePort()
                            }
                            mPrintCallBack?.printSucess(it)
                        }
                    }
                }
            }

        } else {
            mPrintCallBack?.printFailed("当前未有可打印设备连接")
        }
    }
}