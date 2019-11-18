package zs.qimai.com.printer2.enmu

/**
 * 方法字体
 * 0 不放大 0x1d, 0x21, 0x00
 * <p>
 * 1 横向 宽放大  0x1d, 0x21, 0x10
 * <p>
 * 2 纵向
 * 3 横线纵向都放大
 */
enum class FontSizeType {
    FONT_0,
    FONT_1,
    FONT_2,
    FONT_3
}