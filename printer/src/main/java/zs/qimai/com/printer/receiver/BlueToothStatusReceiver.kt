package zs.qimai.com.printer.receiver

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothDevice.EXTRA_DEVICE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import zs.qimai.com.printer.manager.DeviceManagerUtils

class BlueToothStatusReceiver : BroadcastReceiver() {

    private val TAG = "BlueToothStatusReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        Log.d(TAG, "onReceive: intent.action= ${intent.action}")
        when (intent.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {

            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                var bluetoothDevice = intent.getParcelableExtra<BluetoothDevice?>(EXTRA_DEVICE)
                bluetoothDevice?.let {
                    //如果检测到设备已经断开连接，就从保存的集合中删除
                    DeviceManagerUtils.getInstance().removeBtDevice(it)
                }
            }
            BluetoothAdapter.ACTION_STATE_CHANGED ->{
                val blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                //判断蓝牙开关是否关闭了
                if (blueState==BluetoothAdapter.STATE_TURNING_OFF){
                    Log.d(TAG, "onReceive: bluetooth enable closed")
                    DeviceManagerUtils.getInstance().removeAllBtDevice()
                }
            }
        }
    }
}
