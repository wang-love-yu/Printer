package zs.qimai.com.printer2.lifecycle

import android.util.Log
import androidx.fragment.app.FragmentActivity
import java.util.HashSet

class ActivityManagers {

    private  val TAG = "ActivityManagers"
    private var list: HashSet<FragmentActivity> = LinkedHashSet()

    fun addActivity(activity: FragmentActivity) {
        list.add(activity)
        Log.d(TAG, "addActivity: activity= $activity")
    }

    fun removeActivity(activity: FragmentActivity) {
        list.remove(activity)
        Log.d(TAG, "removeActivity: activity= $activity")
    }

    fun getTopActivity(): FragmentActivity? {

        if (!list.isNullOrEmpty()){
            Log.d(TAG, "getTopActivity: $list")
            return list.last()
        }else{
            return null
        }
    }

    companion object{
        private var mActivityManagers:ActivityManagers = ActivityManagers()
        fun getInstance(): ActivityManagers {
            return mActivityManagers
        }

    }

}