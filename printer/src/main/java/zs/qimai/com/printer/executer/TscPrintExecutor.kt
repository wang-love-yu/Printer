package zs.qimai.com.printer.executer

/***
 * convertTscData，所以如果没有ESC打印机任务，可直接实现此方法
 * **/
abstract class TscPrintExecutor : PrintExecutor() {

    override fun convertTscData(): ByteArray? = null

}