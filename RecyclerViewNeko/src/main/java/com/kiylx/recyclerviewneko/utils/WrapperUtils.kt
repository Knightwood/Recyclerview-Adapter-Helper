package com.kiylx.recyclerviewneko.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by zhy on 16/6/28.
 */
object WrapperUtils {
    fun onAttachedToRecyclerView(
        innerAdapter: RecyclerView.Adapter<out ViewHolder>,
        recyclerView: RecyclerView,
        callback: SpanSizeCallback
    ) {
        innerAdapter.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanSizeLookup = layoutManager.spanSizeLookup
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return callback.getSpanSize(layoutManager, spanSizeLookup, position)
                }
            }
            layoutManager.spanCount = layoutManager.spanCount
        }
    }

    fun setFullSpan(holder: RecyclerView.ViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
            lp.isFullSpan = true
        }
    }


}

fun interface SpanSizeCallback {
    fun getSpanSize(
        layoutManager: GridLayoutManager,
        oldLookup: GridLayoutManager.SpanSizeLookup,
        position: Int
    ): Int
}