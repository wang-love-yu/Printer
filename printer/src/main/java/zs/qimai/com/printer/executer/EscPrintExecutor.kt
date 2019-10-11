package zs.qimai.com.printer.executer

/****
 * 实现convertTscData方法，所以如果没有TSC打印机任务，可直接实现此方法
 * **/
abstract class EscPrintExecutor : PrintExecutor() {

    override fun convertTscData(): ByteArray? = null

}