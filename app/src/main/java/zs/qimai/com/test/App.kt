package zs.qimai.com.test

import android.app.Application
import zs.qimai.com.printer.lifecycle.PrintUtils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        PrintUtils.getInstance().init(this)
    }

}