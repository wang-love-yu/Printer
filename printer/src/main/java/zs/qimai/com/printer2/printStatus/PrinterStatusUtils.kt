package zs.qimai.com.printer2.printStatus

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import kotlinx.coroutines.*
import zs.qimai.com.printer2.utils.PrintFormat
import zs.qimai.com.printer2.manager.DeviceManager
import zs.qimai.com.printer2.manager.DeviceManager.Companion.ESC
import zs.qimai.com.printer2.manager.DeviceManager.Companion.TSC
import java.lang.Exception
import kotlin.experimental.and

/***
 * 判断当前打印机的模式tsc esc....  原理是往打印机写入命令，看看能不能收到回应
 */
class PrinterStatusUtils(val deviceManager: DeviceManager) {
    private val TAG = "PrinterStatusUtils"
    private val READ_DATA = 1
    var mSendMode: Int = ESC
    private val READ_DATA_CNT = "read_data_length"
    private val READ_BUFFER_ARRAY = "read_buffer_array"
    private var mPrintMode: Int? = null
    val FLAG: Byte = 0x10
    var mPrintStatusCallBack: PrintStatusCallBack? = null
    var mSearchThread: SearchThread = SearchThread()
    var job: Job? = null
    var async1: Deferred<Unit>? = null
    var async2: Deferred<Unit>? = null
    fun queryStatus() {
        mSearchThread.start()
        job = GlobalScope.launch {
            try {

                //发送esc命令查询
                async1 = sendEscCommandSearch()
                //延时2秒
                delay(2000)
                //发送tsc命名查询
                async2 = sendTscCommandSearch()
                delay(2000)
                //以上如果成功都会取消协程。这里
                handleEnd()
            } catch (e: Exception) {
                Log.d(TAG, "queryStatus: e= $e")
            }
        }
    }

    /***
     * 到这里说明获取不到状态
     * **/
    private fun handleEnd() {
        async1?.cancel()
        async2?.cancel()
        job?.cancel()
        mSearchThread.cancel()
        mHandler?.removeCallbacksAndMessages("")
        mHandler = null
        mPrintStatusCallBack?.searchResult(mPrintMode)

    }

    private suspend fun sendEscCommandSearch(): Deferred<Unit> {
        Log.d(TAG, "sendEscCommandSearch: ")
        return GlobalScope.async {
            withContext(Dispatchers.IO) {
                mSendMode = ESC
                deviceManager.writeData(PrintFormat.ESC_COMMAND)
            }

        }

    }

    private suspend fun sendTscCommandSearch(): Deferred<Unit> {
        Log.d(TAG, "sendTscCommandSearch: ")
        return GlobalScope.async {
            withContext(Dispatchers.IO) {
                if (mPrintMode != null || mPrintMode == ESC) {
                    return@withContext
                } else {
                    mSendMode = TSC
                    deviceManager.writeData(PrintFormat.TSC_COMMAND)
                }
            }
        }

    }

    inner class SearchThread : Thread() {
        private var isRun = true
        private val buffer = ByteArray(100)
        override fun run() {
            super.run()
            while (isRun) {
                val len = deviceManager.readData(buffer)
                if (len > 0) {
                    val message = Message.obtain()
                    message.what = READ_DATA
                    val bundle = Bundle()
                    bundle.putInt(READ_DATA_CNT, len) //数据长度
                    bundle.putByteArray(READ_BUFFER_ARRAY, buffer) //数据
                    message.data = bundle
                    mHandler?.sendMessage(message)
                }
            }
        }

        fun cancel() {
            isRun = false
        }

    }

    /**
     * 判断是实时状态（10 04 02）还是查询状态（1D 72 01）
     */
    private fun judgeResponseType(r: Byte): Int {
        return (r and FLAG).toInt().shr(4)

    }

    private var mHandler: Handler? = @SuppressLint("HandlerLeak")
    object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                READ_DATA -> {
                    val cnt = msg.data.getInt(READ_DATA_CNT) //数据长度 >0;
                    //buff不为空说明收到响应
                    val buffer =
                        (msg.data.getByteArray(READ_BUFFER_ARRAY) ?: return) ?: return  //数据
                    Log.d(TAG, "handleMessage: mSendMode= $mSendMode")
                    //这里只对查询状态返回值做处理，其它返回值可参考编程手册来解析
                    // val result = judgeResponseType(buffer[0]) //数据右移
                    if (mSendMode == ESC) {
                        //设置当前打印机模式为ESC模式
                        if (mPrintMode == null) {
                            mPrintMode = ESC
                            handleEnd()

                        }
                    } else if (mSendMode == TSC) {
                        //设置当前打印机模式为TSC模式
                        if (mPrintMode == null) {
                            mPrintMode = TSC
                            handleEnd()
                        }
                    }
                    //mPrintStatusCallBack?.searchResult(mPrintMode)
                }

            }
        }
    }


}
