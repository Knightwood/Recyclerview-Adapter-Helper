package com.kiylx.recyclerviewneko.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 监听是否滑动到底部
 */
fun RecyclerView.addOnScrollListener(block: (isEnd: Boolean) -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            block(isSlideToBottom(recyclerView))
        }
    })
}

private fun isSlideToBottom(rv: RecyclerView): Boolean {
    return rv.computeVerticalScrollExtent() + rv.computeVerticalScrollOffset() >= rv.computeVerticalScrollRange()
}

interface BottomListener {
    /**
     * 滑动到底部时回调
     */
    fun onScrollToBottom()
}

/**
 * 实现了RecyclerView滚动到底部监听的OnScrollListener
 */
open class RecyclerViewScrollListener : RecyclerView.OnScrollListener(),
    BottomListener {
    // 最后几个完全可见项的位置（瀑布式布局会出现这种情况）
    private var lastCompletelyVisiblePositions: IntArray?=null

    // 最后一个完全可见项的位置
    private var lastCompletelyVisibleItemPosition = 0
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager
        // 找到最后一个完全可见项的位置
        if (layoutManager is StaggeredGridLayoutManager) {
            val manager: StaggeredGridLayoutManager = layoutManager
            if (lastCompletelyVisiblePositions == null) {
                lastCompletelyVisiblePositions = IntArray(manager.spanCount)
            }
            manager.findLastCompletelyVisibleItemPositions(lastCompletelyVisiblePositions)
            lastCompletelyVisibleItemPosition = getMaxPosition(lastCompletelyVisiblePositions!!)
        } else if (layoutManager is GridLayoutManager) {
            lastCompletelyVisibleItemPosition =
                layoutManager.findLastCompletelyVisibleItemPosition()
        } else if (layoutManager is LinearLayoutManager) {
            lastCompletelyVisibleItemPosition =
                layoutManager.findLastCompletelyVisibleItemPosition()
        } else {
            throw RuntimeException("Unsupported LayoutManager.")
        }
    }

    private fun getMaxPosition(positions: IntArray): Int {
        var max = positions[0]
        for (i in 1 until positions.size) {
            if (positions[i] > max) {
                max = positions[i]
            }
        }
        return max
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        val layoutManager = recyclerView.layoutManager
        // 通过比对 最后完全可见项位置 和 总条目数，来判断是否滑动到底部
        val visibleItemCount = layoutManager!!.childCount
        val totalItemCount = layoutManager.itemCount
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (visibleItemCount > 0 && lastCompletelyVisibleItemPosition >= totalItemCount - 1) {
                onScrollToBottom()
            }
        }
    }

    override fun onScrollToBottom() {}
}
