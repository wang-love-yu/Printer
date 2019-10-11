package zs.qimai.com.printer.fragment

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import zs.qimai.com.printer.callback.BlueToothSearchCallBack

/***
 * BlueToothListFragment管理类
 * **/
class BlueToothLists {
    private val TAG = "BlueToothLists"
    var mBlueToothListFragment: BlueToothListFragment? = null

    constructor(fragmentActivity: FragmentActivity) {
        mBlueToothListFragment = getFragment(fragmentActivity)
    }

    constructor(fragment: Fragment) {
        mBlueToothListFragment = getFragment(fragment)
    }

    private fun getFragment(fragment: Fragment): BlueToothListFragment? {
        return findFragment(fragment.childFragmentManager)
    }

    private fun getFragment(fragmentActivity: FragmentActivity): BlueToothListFragment? {
        return findFragment(fragmentActivity.supportFragmentManager)

    }

    private fun findFragment(supportFragmentManager: FragmentManager?): BlueToothListFragment? {
        var blueToothListFragment: BlueToothListFragment? = null

        blueToothListFragment =
            supportFragmentManager?.findFragmentByTag(TAG) as? BlueToothListFragment
        if (blueToothListFragment == null) {
            Log.d(TAG, "findFragment: blueToothListFragment==null")
            blueToothListFragment = BlueToothListFragment()
        }
        if (!blueToothListFragment.isAdded) {
            supportFragmentManager?.beginTransaction()?.add(blueToothListFragment, TAG)
                ?.commitNow()
        }
        return blueToothListFragment
    }

    /***
     * 获取搜索到的蓝牙列表
     * ***/
    fun getBlueToothList(callBack: BlueToothSearchCallBack? = null) {
        mBlueToothListFragment?.getBlueToothList(callBack)
    }
}