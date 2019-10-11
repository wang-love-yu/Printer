package zs.qimai.com.test

import android.bluetooth.BluetoothDevice
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_bt_list.*
import zs.qimai.com.printer.utils.PrintManagerUtils
import zs.qimai.com.printer.callback.BlueToothSearchCallBack
import zs.qimai.com.printer.callback.OnBtConnectCallBack
import zs.qimai.com.printer.manager.BlueDeviceManager
import zs.qimai.com.printer.manager.DeviceManager
import java.util.ArrayList
import java.util.HashSet

class BtListActivity : AppCompatActivity(), View.OnClickListener {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onClick(p0: View?) {
        Log.d(TAG, "onClick: ${datas[p0!!.tag as Int].name}")
        PrintManagerUtils.getInstance().btConnect(datas[p0!!.tag as Int].address, object :
            OnBtConnectCallBack {
            override fun onConnectSuccess(deviceManager: DeviceManager?) {
                (deviceManager as?BlueDeviceManager)?.let {
                    Toast.makeText(
                        this@BtListActivity,
                        "连接成功 name = ${it.mBlueToothDevice?.name} 类型是 ${if (deviceManager.mPrintMode==1)"小票" else "标签"}",
                        Toast.LENGTH_LONG
                    ).show()
                        //finish()
                }

                Log.d(TAG, "onConnectSuccess: ")
            }

            override fun onConnectError(errCode: Int, msg: String?) {
                Toast.makeText(
                    this@BtListActivity,
                    "连接失败 code= $errCode errmsg= $msg",
                    Toast.LENGTH_LONG
                ).show()

                Log.d(TAG, "onConnectError: errcode= $errCode  errmsg= $msg")
            }

            override fun onConnectStart() {
                Log.d(TAG, "onConnectStart: ")
            }


        })

    }

    private val TAG = "BtListActivity"
    var adapter: Adapter? = null
    var datas: ArrayList<BluetoothDevice> = ArrayList()
    var setDatas: HashSet<BluetoothDevice> = HashSet()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bt_list)
        Log.d(TAG, "onCreate: ")
        PrintManagerUtils.getInstance().getSearchBlueToothList(object : BlueToothSearchCallBack {
            override fun onRefuseGrantPermission() {

            }

            override fun onSearchStart() {

            }

            override fun searchFailed(code: Int, msg: String?) {

            }

            override fun onSearchFinish() {
            }

            override fun onRefuseOpenBtEnable() {
                Log.d(TAG, "onRefuseOpenBtEnable: ")
            }

            override fun onSuccessOpenBtEnable() {

                Log.d(TAG, "onSuccessOpenBtEnable: ")
            }

            override fun onBlueToothFound(bluetoothDevice: BluetoothDevice?) {
                //Log.d(TAG, "onBlueToothFound: ")
                bluetoothDevice?.let {

                    var status = setDatas.add(it)
                    if (status) {
                        datas.add(it)
                    }
                }
                if (adapter == null) {
                    adapter = Adapter()
                    rv_list.layoutManager = LinearLayoutManager(this@BtListActivity)
                    rv_list.adapter = adapter
                } else {
                    adapter?.notifyDataSetChanged()
                }

            }
        })
    }

    inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var tv = LayoutInflater.from(parent.context).inflate(R.layout.tv_layout, parent, false)
            return ViewHolder(tv)
        }

        override fun getItemCount(): Int = datas.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            (holder.itemView as? TextView)?.apply {
                text = datas[position].name
            }
            holder.itemView.tag = position
            holder.itemView.setOnClickListener(this@BtListActivity)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

        }
    }

}
