package zs.qimai.com.test

import android.hardware.usb.UsbDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_usb.*
import zs.qimai.com.printer2.callback.UsbPrintConnCallBack
import zs.qimai.com.printer2.utils.PrintManagerUtils
import zs.qimai.com.printer2.callback.UsbSearchCallBack
import zs.qimai.com.printer2.callback.UsbSearchCallBack2
import zs.qimai.com.printer2.manager.UsbDeviceManager

class UsbActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "UsbActivity"
    override fun onClick(v: View?) {
        (v!!.tag as? String)?.let {
            PrintManagerUtils.getInstance().usbConnect(it, datas[it]!!,object: UsbPrintConnCallBack{
                override fun onConnStart() {

                }

                override fun onConnSucess(usbDeviceManager: UsbDeviceManager) {
                    Log.d(TAG, "onConnSucess: ")
                    finish()
                }

                override fun onConnFailed(code: Int, error: String) {
                    Log.d(TAG, "onConnFailed:code= $code msg= $error")

                }

            })
        }
        //datas[v!!.tag as String]?.let {  }
    }

    var datas: MutableMap<String, UsbDevice> = HashMap()
    var adapter: Adapter = Adapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usb)
        rv_list.layoutManager = LinearLayoutManager(this@UsbActivity)
        rv_list.adapter = adapter
        PrintManagerUtils.getInstance().getSearchUsbList(object : UsbSearchCallBack2 {
            override fun onSearchFoundAll(devices: HashMap<String, UsbDevice>?) {
                Log.d(TAG, "onSearchFoundAll: ")
            }

            override fun onSearchStart() {
                Log.d(TAG, "onSearchStart: ")
            }

            override fun onSearchFound(address: String, usbDevice: UsbDevice) {
                datas[address] = usbDevice
                adapter.notifyDataSetChanged()
                Log.d(TAG, "onSearchFound: ")
            }

            override fun onSearchError(errCode: Int, errMsg: String?) {
                Log.d(TAG, "onSearchError: ")
            }

            override fun onSearchFinish() {
                Log.d(TAG, "onSearchFinish: ")
            }
        })

        PrintManagerUtils.getInstance().getSearchUsbList(object : UsbSearchCallBack {

            override fun onSearchStart() {
                Log.d(TAG, "onSearchStart1: ")
            }

            override fun onSearchFound(address: String, usbDevice: UsbDevice) {
                datas[address] = usbDevice
                adapter.notifyDataSetChanged()
                Log.d(TAG, "onSearchFound1: ")
            }

            override fun onSearchError(errCode: Int, errMsg: String?) {
                Log.d(TAG, "onSearchError1: ")
            }

            override fun onSearchFinish() {
                Log.d(TAG, "onSearchFinish1: ")
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
                //text = datas[position].name
                datas.keys.forEachIndexed { index, s ->
                    if (index == position) {
                        text = s + " " + datas.get(s)?.deviceName
                        holder.itemView.tag = s
                    }

                }
            }
            holder.itemView.setOnClickListener(this@UsbActivity)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

        }
    }
}
