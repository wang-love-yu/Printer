package zs.qimai.com.printer2.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

/*****
 * 这里的Activity只支持支持实现LifeCycle的Activity
 * **/
class PrintLifeCycleCallBack : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {

        if (activity is LifecycleOwner) {
            ActivityManagers.getInstance().removeActivity(activity as FragmentActivity)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is LifecycleOwner) {
            ActivityManagers.getInstance().addActivity(activity as FragmentActivity)
        } else {
            //throw RuntimeException("仅支持Androidx Activity")
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }
}