package zs.qimai.com.printer.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import java.lang.RuntimeException

class PrintUtils {
    var application: Application? = null
    private  val TAG = "PrintUtils"
    /****
     * 初始化一个生命周期监听类
     * **/
    fun init(application: Application) {
        this.application = application
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityDestroyed(p0: Activity) {
                if (p0 is FragmentActivity) {
                    ActivityManagers.getInstance().removeActivity(p0)
                } else {
                    throw RuntimeException("仅支持Androidx Activity")
                }
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                Log.d(TAG, "onCreate: ")
                if (p0 is FragmentActivity) {
                    ActivityManagers.getInstance().addActivity(p0)
                } else {
                    throw RuntimeException("仅支持Androidx Activity")
                }
            }

            override fun onActivityResumed(p0: Activity) {
            }

        })

    }

    companion object {
        private val printUtils = PrintUtils()
        fun getInstance(): PrintUtils {
            return printUtils
        }
    }

}