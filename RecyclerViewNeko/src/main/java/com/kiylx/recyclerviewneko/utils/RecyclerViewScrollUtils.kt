package com.kiylx.recyclerviewneko.utils

import android.util.Log
import androidx.recyclerview.widget.RecyclerView

/**
 * 监听是否滑动到底部和顶部
 */
fun RecyclerView.addOnScrollListener(
    block1: (isEnd: Boolean) -> Unit,
    block2: (isTop: Boolean) -> Unit
) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            block1(recyclerView.isSlideToBottom())
            block2(recyclerView.isSlideToTop())
        }
    })
}

/**
 * @return true:手指向右滑动，列表向右移动，到达左侧的“底部”
 */
fun RecyclerView.isSlideToLeft(): Boolean {
    return !canScrollHorizontally(-1)
}

/**
 * @return true:手指向左滑动，列表向左移动，右侧到达“底部”
 */
fun RecyclerView.isSlideToRight(): Boolean {
    return !canScrollHorizontally(1)
}

/**
 * true:到达底部
 */
fun RecyclerView.isSlideToBottom(): Boolean {
    return !canScrollVertically(1)
}

/**
 * true：到达顶部
 */
fun RecyclerView.isSlideToTop(): Boolean {
    return !canScrollVertically(-1)
}


/**
 * 实现了RecyclerView滚动到底部监听的OnScrollListener
 */
abstract class RecyclerViewScrollListener : RecyclerView.OnScrollListener() {
    /**
     * dx > 0 时为手指向左滚动,列表滚动显示右面的内容
     * dx < 0 时为手指向右滚动,列表滚动显示左面的内容
     * dy > 0 时为手指向上滚动,列表滚动显示下面的内容
     * dy < 0 时为手指向下滚动,列表滚动显示上面的内容
     */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (recyclerView.isSlideToBottom()) {
                    onScrollToDataEnd()
                }

                if (recyclerView.isSlideToTop()) {
                    onScrollToDataStart()
                }

//                if (recyclerView.isSlideToRight()) {
//                    onScrollToDataEnd()
//                }
//
//                if (recyclerView.isSlideToLeft()) {
//                    onScrollToDataStart()
//                }

        }
    }

    /**
     * 滑动到数据列表的最后一项
     */
    abstract fun onScrollToDataEnd()

    /**
     * 滑动到数据列表的开始
     */
    abstract fun onScrollToDataStart()
}
