package zs.qimai.com.printer2.manager

import java.io.InputStream
import java.io.OutputStream

/****
 * 设备基类
 * **/
abstract class DeviceManager {

    var mOutPutStream: OutputStream? = null
    var mInPutStream: InputStream? = null
    var mStatus = false
    var address: String? = null
    var mPrintMode: Int? = null
    abstract var mType: Int

    companion object {
        //小票模式
        val ESC = 1
        //标签模式
        val TSC = 2
        //蓝牙
        val BT = 1
        //usb
        val USB = 2
    }

    //连接
    abstract fun openPort()

    //断开连接
    abstract fun closePort()

    abstract fun readData(bytes: ByteArray): Int
    abstract fun writeData(bytes: ByteArray)


}