package zs.qimai.com.printer2.receiverManager

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import zs.qimai.com.printer2.callback.BlueToothPwdCallBack

/****
 *蓝牙连接时候输入密码，需要广播监听状态
 *
 * **/
class BlueToothPwdReceiverManager : LifecycleObserver {
    private val TAG = "BlueToothPwdrvMg"
    lateinit var receiver: Receiver
    //activity用来管理广播的
   private var activity: FragmentActivity? = null
    private var intentFilter = IntentFilter()
    var callBack: BlueToothPwdCallBack? = null
    private var bluetoothAdapter: BluetoothAdapter? = null

    constructor(activity: FragmentActivity) {
        this.activity = activity
        this.activity?.lifecycle?.addObserver(this)
        init()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory() {
        this?.activity?.unregisterReceiver(receiver)
        activity = null
    }

    constructor(fragment: Fragment) {
        this.activity = fragment.activity
        fragment.lifecycle.addObserver(this)
        init()
    }

    private fun init() {
        receiver = Receiver()
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        this.activity?.registerReceiver(receiver, intentFilter)
    }

    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                Log.d(TAG, "onReceive: ACTION_BOND_STATE_CHANGED")
                val bluetoothDevice =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                Log.d(
                    TAG,
                    "onReceive: ACTION_BOND_STATE_CHANGED bluetoothDevice= " + bluetoothDevice!!.name
                            + " state= " + bluetoothDevice.bondState
                )
                callBack?.onInputStatus(bluetoothDevice.bondState)
                if (bluetoothDevice.bondState == BluetoothDevice.BOND_NONE || bluetoothDevice.bondState == BluetoothDevice.BOND_BONDED)
                    cancelReceiver()
            }
        }
    }

    /**
     * 获取状态后就要提前取消广播。避免系统多次发送广播
     * **/
    private fun cancelReceiver() {
        this.callBack = null
        this.activity?.registerReceiver(receiver, intentFilter)
    }
}