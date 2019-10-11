package zs.qimai.com.printer.fragment

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tbruyelle.rxpermissions2.RxPermissions
import zs.qimai.com.printer.receiverManager.RequestBlueToothListReceiverManager
import zs.qimai.com.printer.callback.BlueToothSearchCallBack

/****
 * 没有界面的Fragment 管理获取定位权限，获取搜索到的蓝牙列表
 * **/
class BlueToothListFragment : Fragment() {
    private val TAG = "BlueToothListFragment"
    var activity: Activity? = null
    var callback: BlueToothSearchCallBack? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as Activity?
    }

    fun getBlueToothList(callBack1: BlueToothSearchCallBack? = null) {
        this.callback = callBack1
        this.callback?.onSearchStart()
        //先检查权限 蓝牙搜索必须要这些权限
        RxPermissions(this).apply {
            this.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH
            )
                .subscribe {
                    if (it!!) {
                        if (!isOpenBlueTothEnable()) {
                            openBlueToothEnable()
                        } else {
                            requestBlueList()
                        }
                    } else {
                        this@BlueToothListFragment.callback?.onRefuseGrantPermission()
                    }
                }
        }
    }

    /**
     * 获取列表
     * **/
    private fun requestBlueList() {
        RequestBlueToothListReceiverManager(this).blueToothSearchCallBack = this.callback
    }

    /***
     * 蓝牙开关是否已经打开
     * **/
    private fun isOpenBlueTothEnable(): Boolean {
        return BluetoothAdapter.getDefaultAdapter().isEnabled
    }

    /*****
     * 打开蓝牙
     * **/
    private fun openBlueToothEnable() {
        val mIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        startActivityForResult(mIntent, 99)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 99) {
            if (resultCode == Activity.RESULT_OK) {
                callback?.onSuccessOpenBtEnable()
                requestBlueList()

            } else {
                callback?.onRefuseOpenBtEnable()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity = null
        callback = null
    }
}