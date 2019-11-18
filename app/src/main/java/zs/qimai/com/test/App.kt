package zs.qimai.com.test

import android.app.Application
import zs.qimai.com.printer2.utils.PrintManagerUtils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        PrintManagerUtils.getInstance().apply {
            init(this@App)
        }
    }

}