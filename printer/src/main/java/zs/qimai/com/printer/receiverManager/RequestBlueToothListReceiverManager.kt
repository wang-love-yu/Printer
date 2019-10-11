package zs.qimai.com.printer.receiverManager

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
import zs.qimai.com.printer.callback.BlueToothSearchCallBack

/***
 * 蓝牙搜索列表展示
 * **/
class RequestBlueToothListReceiverManager : LifecycleObserver {
    private val TAG = "RequestBlueToothLis"
    var activity: FragmentActivity? = null
    var receiver = Receiver()
    var blueToothSearchCallBack: BlueToothSearchCallBack? = null
    var intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
    var bluetoothAdapter: BluetoothAdapter? = null

    //监听生命周期
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory() {
        Log.d(TAG, "onDestory: ")
        this.activity?.unregisterReceiver(receiver)
        this.activity = null
        blueToothSearchCallBack = null
        bluetoothAdapter?.cancelDiscovery()
        bluetoothAdapter = null
    }

    constructor(activity: FragmentActivity) {
        this.activity = activity
        Log.d(TAG, "activity1= :$activity")
        init()
    }

    constructor(fragment: Fragment) {
        this.activity = fragment.activity
        Log.d(TAG, "activity2= :$activity ")
        init()

    }

    private fun init() {
        this.activity?.lifecycle?.addObserver(this)
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        this?.activity?.registerReceiver(receiver, intentFilter)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        bluetoothAdapter?.cancelDiscovery()
        bluetoothAdapter?.startDiscovery()
    }

    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == BluetoothDevice.ACTION_FOUND) {
                Log.d(TAG, "onReceive: BluetoothDevice.ACTION_FOUND")
                val bluetoothDevice =
                    intent?.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                bluetoothDevice?.let {
                    if (!it.name.isNullOrEmpty()) {
                        blueToothSearchCallBack?.onBlueToothFound(bluetoothDevice)
                        Log.d(
                            TAG,
                            "onReceive: bluetoothDevice name= " + bluetoothDevice!!.name + " address= "
                                    + bluetoothDevice!!.address + "  "
                        )
                    }
                }

            }
            if (action == BluetoothAdapter.ACTION_DISCOVERY_STARTED) {
                Log.d(TAG, "onReceive: ACTION_DISCOVERY_STARTED")

            }
            if (action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
                Log.d(TAG, "onReceive: ACTION_DISCOVERY_FINISHED")
                //  blueToothSearchCallBack?.onSearchFinish()
                bluetoothAdapter?.startDiscovery()
            }

            if (action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                Log.d(TAG, "onReceive: ACTION_BOND_STATE_CHANGED")
            }

        }
    }
}