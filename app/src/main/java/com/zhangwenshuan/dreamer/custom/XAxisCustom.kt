package com.zhangwenshuan.dreamer.custom

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter

/**
 * 图标底部定制
 */
class XAxisCustom(val list: List<String>) : IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return list[value.toInt()]
    }

    fun getDecimalDigits(): Int {
        return 0
    }
}