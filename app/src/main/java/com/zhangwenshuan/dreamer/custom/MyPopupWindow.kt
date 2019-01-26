package com.zhangwenshuan.dreamer.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.PopupWindow

class MyPopupWindow:PopupWindow {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}


}