package zs.qimai.com.printer2.canvas

import com.tools.command.EscCommand
import com.tools.command.LabelCommand
import java.util.*

class LabelTemplete : PrintTemplate<Any> {
    override fun getPrintData(data: Any): ByteArray? {

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
            20,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_1,
            "123"
        )
        tsc.addText(
            260,
            30,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_1,
            "1"
        )
        tsc.addText(
            20,
            60,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_2,
            "洛神拧香鲜鲜橙香"
        )
        var spec = "大杯，加冰"
        //计算规格长度是否超出杯贴纸张
        val cutIndex = getCutIndex(spec)
        if (cutIndex > 0) {
            val spec1 = spec.substring(0, cutIndex)
            // val spec2 = getSpec2(spec.substring(cutIndex))
            tsc.addText(
                20,
                110,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                spec1
            )
            /*tsc.addText(
                20,
                135,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                spec2
            )*/
        } else {
            tsc.addText(
                20,
                110,
                LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
                LabelCommand.ROTATION.ROTATION_0,
                LabelCommand.FONTMUL.MUL_1,
                LabelCommand.FONTMUL.MUL_1,
                spec
            )
        }

        tsc.addText(
            20,
            160,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_1,
            "企小店"
        )
        tsc.addText(
            20,
            190,
            LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE,
            LabelCommand.ROTATION.ROTATION_0,
            LabelCommand.FONTMUL.MUL_1,
            LabelCommand.FONTMUL.MUL_1,
            "2019-12-23 10:12:33"
        )
        ////////////////
//        tsc.addText(0, 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
//                "123456789012345678901234567890");
//        tsc.addText(0, 60, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
//                "天气好热啊天气好热啊天气好热啊天气好热啊天气好热啊");
//        tsc.addText(0, 90, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
//                "////-////-////-////-////-////-////-");
        ////////////////
        // 打印标签
        tsc.addPrint(1, 1)
        // 打印标签后 蜂鸣器响
        tsc.addSound(2, 100)
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255)
        // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addGap(2)
        val datas = tsc.command
        return convertVectorByteToBytes(datas)
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

    fun getCutIndex(str: String): Int {
        var maxlength = getMaxLength()
        var index = str.length
        var newStr = str
        if (str.length > maxlength) {
            while (newStr.length > maxlength) {
                index--
                newStr = str.substring(0, index)
            }
            return index
        } else {
            return -1
        }
    }

    private fun getMaxLength(): Int {
        when (40) {
            40 -> return 24

            else -> return 24
        }
    }
}