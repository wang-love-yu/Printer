# Printer

## 使用方法
#### 初始化
1. 在Application 中初始化 PrintManagerUtils.getInstance().init(BaseApplication.getApplication() as Application)
#### 获取设备与连接
2. 获取蓝牙打印机列表 PrintManagerUtils.getInstance().getSearchBlueToothList()   连接蓝牙打印机PrintManagerUtils.getInstance().btConnect（）
3. 获取USB打印机列表PrintManagerUtils.getInstance().getSearchUsbList()   连接USB打印机 PrintManagerUtils.getInstance().usbConnect（）
#### 打印准备类
 > 原理为把需要打印的内容转为字节数组

  
  
```
 class PtOrderStoreValueEscPrinterTemplate : PrintTemplate<PtStoreValueInfoBean> {
    override fun getPrintData(data: PtStoreValueInfoBean): ByteArray? {
        return PrintWriter().apply {

            val orderBean = data.order ?: return null
            //编号
            if (!TextUtils.isEmpty(orderBean.sort)) {
                writelnLineText("#${orderBean.sort}", aligin = FontAligin.CENTER, fontSizeType = FontSizeType.FONT_3)
                //  writeNewLine()
            }

            //店铺名
            writelnLineText("${orderBean.store_name}", aligin = FontAligin.CENTER, fontSizeType = FontSizeType.FONT_3)

            //printNextLine()
            //writeNewLine()
            /*   if (orderBean?.plat_name.contains("收银机")) {
                   writelnLineText(orderBean.plat_name, FontAligin.CENTER)
               } else */

            //writeNewLine(1)
            writelnLineText("---" + "已支付" + "---", FontAligin.CENTER, fontSizeType = FontSizeType.FONT_3)
            //writeNewLine(1)
            writelnLineText("【下单时间】 " + orderBean.order_time)
            // writeNewLine()
            //打印商品
            writelnLineText(formatAllLinePonit())
            for (item in orderBean.items) {
                if (item.num == 1) {
                    writelnLineText(formatGoods(item.item_name, "*" + item.num + " ", "" + item.price), fontSizeType = FontSizeType.FONT_2)

                } else {
                    writelnLineText(formatGoods(item.item_name, "【*" + item.num + "】", "" + item.price), fontSizeType = FontSizeType.FONT_2)

                }
                //打印规格
                writelnLineText(item.spec)
            }
            //支付方式
            writelnLineText(formatAllLinePonit())
            writelnLineText(formatValuesAliginLfAndAliginRight("支付方式:", "" + orderBean.pay_mode_text))
            // writeNewLine()
            writelnLineText(formatValuesAliginLfAndAliginRight("已付: ", orderBean.paid_amount.toString())
                    , fontSizeType = FontSizeType.FONT_2)

            writelnLineText(formatAllLinePonit())
            writeNewLine()
            writelnLineText("订单号:" + data.order_no)
            writelnLineText(orderBean.tail_msg)
            writeNewLine(4)


        }.convertByteArray()

    }
 ```
1. 实现 PrintTemplate<T>类getPrintData()方法
2. 通过PrintWriter类把数据类转为ByteArray，默认为一行32字体的那种小票（如果此类不满足需求，可继承此类，继续扩展）
    原理为内部创建个ByteArrayOutputStream对象，通过调用其他数据写入类往其中存数据，最终通过convertByteArray（）转为ByteArray
 ### 执行打印
 1，继承EscPrintExecutor（）重写 covertEscData（）方法
```
class PtStoreValuedPrinterExector(val storeValueInfoBean: PtStoreValueInfoBean) : EscPrintExecutor() {

    override fun covertEscData(): ByteArray? {

        return PtOrderStoreValueEscPrinterTemplate().getPrintData(storeValueInfoBean)
    }
}
```
2. 业务中调用
```
 PtStoreValuedPrinterExector(it.data!!).apply { 
                            mPrintCallBack = null
                            execute()
                        }
```

 
