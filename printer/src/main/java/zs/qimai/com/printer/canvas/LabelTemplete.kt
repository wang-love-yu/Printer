package zs.qimai.com.printer.canvas

import com.tools.command.EscCommand
import com.tools.command.LabelCommand
import java.util.*

class LabelTemplete : PrintTemplate {
    override fun getPrintData(): ByteArray? {

        val datas = ArrayList<ByteArray>()

        val storeName = "企小店"

        val tsc = LabelCommand()
        // 设置标签尺寸，按照实际尺寸设置
//        tsc.addSize(Constant.cupStick_width, Constant.cupStick_height);
        tsc.addSize(40, 30)

        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL)
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON)
        // 设置原点坐标
        tsc.addReference(0, 0)
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON)
        // 清除打印缓冲区
        tsc.addCls()
        //后期兼容其它尺寸时 添加自定义尺寸计算
//        int widthDot=Constant.cupStick_width*8;
//        int heightDot=Constant.cupStick_height*8;
        // 绘制简体中文
        tsc.addText(
            20,
            30,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_1,
            "12"
        )
        tsc.addText(
            260,
            30,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_1,
            "!"
        )
        tsc.addText(
            20,
            60,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_2,
            "hello"
        )
        // 打印标签
        tsc.addPrint(1, 1)
        // 打印标签后 蜂鸣器响
        tsc.addSound(2, 100)
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255)
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(2)
        val datas1 = tsc.command
        return convertVectorByteToBytes(datas1)
    }

     fun getDatas(): ByteArray {
        val datas = ArrayList<ByteArray>()

        val storeName = "企小店"

        val tsc = LabelCommand()
        // 设置标签尺寸，按照实际尺寸设置
//        tsc.addSize(Constant.cupStick_width, Constant.cupStick_height);
        tsc.addSize(40, 30)

        // 设置打印方向
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL)
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON)
        // 设置原点坐标
        tsc.addReference(0, 0)
        // 撕纸模式开启
        tsc.addTear(EscCommand.ENABLE.ON)
        // 清除打印缓冲区
        tsc.addCls()
        //后期兼容其它尺寸时 添加自定义尺寸计算
//        int widthDot=Constant.cupStick_width*8;
//        int heightDot=Constant.cupStick_height*8;
        // 绘制简体中文
        tsc.addText(
            20,
            30,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_1,
            "12"
        )
        tsc.addText(
            260,
            30,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_1,
            "!"
        )
        tsc.addText(
            20,
            60,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_2,
            "hello"
        )
        // 打印标签
        tsc.addPrint(1, 1)
        // 打印标签后 蜂鸣器响
        tsc.addSound(2, 100)
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255)
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(2)
         val datas1 = tsc.command
         return convertVectorByteToBytes(datas1)
    }
    protected fun convertVectorByteToBytes(data: Vector<Byte>): ByteArray {
        val sendData = ByteArray(data.size)
        if (data.size > 0) {
            for (i in data.indices) {
                sendData[i] = data[i] as Byte
            }
        }

        return sendData
    }
}