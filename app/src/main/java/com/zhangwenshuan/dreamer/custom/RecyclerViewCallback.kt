package com.zhangwenshuan.dreamer.custom

interface RecyclerViewCallback {
    fun onMove(fromPosition: Int, toPosition: Int)

    fun onSwiped(position: Int)
}