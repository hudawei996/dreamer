package com.zhangwenshuan.dreamer.custom

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


class ItemTouchHelperCallback(var callback: RecyclerViewCallback, var drag: Boolean, var swipe: Boolean) :
    ItemTouchHelper.Callback() {


    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {

        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView?,
        viewHolder: RecyclerView.ViewHolder?,
        target: RecyclerView.ViewHolder?
    ): Boolean {
         callback.onMove(viewHolder!!.adapterPosition, target!!.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        callback.onSwiped(viewHolder!!.adapterPosition)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return drag
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return swipe
    }
}