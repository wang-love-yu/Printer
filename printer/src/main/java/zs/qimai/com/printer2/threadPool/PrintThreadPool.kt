package zs.qimai.com.printer2.threadPool

import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class PrintThreadPool {
    private var threadPoolExecutor: ThreadPoolExecutor? = null

    private constructor() {
        threadPoolExecutor = Executors.newFixedThreadPool(10) as ThreadPoolExecutor?
    }

    companion object {

        private var mPrintThreadPool: PrintThreadPool? = null
            get() {
                if (field == null) {
                    field = PrintThreadPool()
                }
                return field
            }

        fun getInstance(): PrintThreadPool {
            return mPrintThreadPool!!
        }
    }

    fun addTask(r: Runnable) {
        threadPoolExecutor?.execute(r)
    }

}