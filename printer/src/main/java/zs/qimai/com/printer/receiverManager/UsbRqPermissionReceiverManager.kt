package zs.qimai.com.printer.receiverManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import zs.qimai.com.printer.utils.PrintManagerUtils.Companion.ACTION_USB_PERMISSION
import android.hardware.usb.UsbDevice
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import zs.qimai.com.printer.callback.UsbPermissionRqCallBack


/***
 * uab申请权限的广播管理
 * **/
class UsbRqPermissionReceiverManager : LifecycleObserver {

    private var receiver: Receiver? = null
    var mUsbPermissionRqCallBack: UsbPermissionRqCallBack? = null

    private val TAG = "UsbRqPermissionReceiver"
    var activity: FragmentActivity? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestory() {
        this.activity?.unregisterReceiver(receiver)
        this.receiver = null
        this.activity = null
    }

    constructor(activity: FragmentActivity) {
        this.activity = activity
        init()
    }

    constructor(fragment: Fragment) {
        this.activity = fragment.activity
        init()

    }

    private fun init() {
        receiver = Receiver()
        var intentFilter = IntentFilter(ACTION_USB_PERMISSION)
        this.activity?.registerReceiver(receiver, intentFilter)

    }

    inner class Receiver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val action = intent?.action
            if (action == ACTION_USB_PERMISSION) {
                val mUsbDevice = intent.getParcelableExtra<UsbDevice?>(UsbManager.EXTRA_DEVICE)
                mUsbDevice?.let {
                    Log.d(TAG, "onReceive: ACTION_BOND_STATE_CHANGED")
                    if (intent.getBooleanExtra(
                            UsbManager.EXTRA_PERMISSION_GRANTED,
                            false
                        )
                    ) {
                        //获取权限成功
                        Log.d(TAG, "获取权限成功")
                        mUsbPermissionRqCallBack?.rqSuccess()
                        //

                        //connectUsbPrinter(mUsbDevice);
                    } else {
                        //获取权限失败
                        Log.d(TAG, "获取权限失败")
                        mUsbPermissionRqCallBack?.reqFailed("获取权限失败")
                    }
                } ?: mUsbPermissionRqCallBack?.reqFailed("获取不到USB设备")
                //处理完一次就要销毁，避免多次回调
                onDestory()

            }
        }
    }
}