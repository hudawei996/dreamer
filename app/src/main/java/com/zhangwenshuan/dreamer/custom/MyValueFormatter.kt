package com.zhangwenshuan.dreamer.custom

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import com.zhangwenshuan.dreamer.util.decimalFormat

class MyValueFormatter(var list: MutableList<String>) : IValueFormatter {


    override fun getFormattedValue(
        value: Float,
        entry: Entry,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler
    ): String {
        // write your logic here
        return list[entry.x.toInt()] + System.lineSeparator() + decimalFormat.format(value) // e.g. append a dollar-sign
    }
}