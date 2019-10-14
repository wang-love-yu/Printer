package zs.qimai.com.test

import android.app.Application
import zs.qimai.com.printer.utils.PrintManagerUtils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        PrintManagerUtils.getInstance().init(this)
    }

}