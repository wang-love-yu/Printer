package zs.qimai.com.printer.canvas


/****
 * 类比canvas 这里是画布的意思
 * 实现此接口  获取字节数组，传入PrintExecutor中进行打印
 *
 * getPrntData 中来处理画布的
 *
 *  * **/
interface PrintTemplate<T> {

    fun getPrintData(data: T): ByteArray?
}