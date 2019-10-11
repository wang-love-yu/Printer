package zs.qimai.com.printer.canvas


/****
 * 类比canvas 这里是画布的意思
 * getPrntData 中来处理画布的
 *
 *  * **/
interface PrintTemplate {

    fun getPrintData(): ByteArray?
}