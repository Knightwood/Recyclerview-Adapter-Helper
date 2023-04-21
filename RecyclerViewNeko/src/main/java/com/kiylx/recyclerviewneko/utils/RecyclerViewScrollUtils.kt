package com.kiylx.recyclerviewneko.utils

import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager

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
 * 默认左边或上边为列表的开始
 */
abstract class RecyclerViewScrollListener(rv: RecyclerView) : RecyclerView.OnScrollListener() {
    //第一个可见项
    private var firstVisiblePosition: Int = -1

    // 最后一个完全可见项的位置
    private var lastCompletelyVisibleItemPosition = 0

    // 最后几个完全可见项的位置（瀑布式布局会出现这种情况,需要单独处理，找出最大的那个可见位置）
    private var lastCompletelyVisiblePositionList: IntArray? = null
    private var firstCompletelyVisiblePositionList: IntArray? = null
    //private var upAndDown: Int = 0//0:没滑动事件，1：手指向上，向左，2：手指向下，向右

    init {
      /*  rv.addOnItemTouchListener(object : OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                var x: Float = 0F
                var y: Float = 0F
                val s = ViewConfiguration.get(rv.context).scaledTouchSlop * 10//最小可滑动距离

                if (e.action == MotionEvent.ACTION_DOWN) {
                    x = e.x
                    y = e.y
                }
                if (e.action == MotionEvent.ACTION_MOVE) {
                    if (y - e.y > s) {
                        //手指向上
                        if (upAndDown==0){
                            upAndDown=1
                        }
                    } else if (e.y - y > s) {
                        //手指向下
                        if (upAndDown==0) {
                            upAndDown = 2
                        }
                    } else if (x - e.x > s) {
                        //手指向左
                        if (upAndDown==0){
                            upAndDown=1
                        }
                    } else if (e.x - x > s) {
                        //手指向右
                        if (upAndDown==0){
                            upAndDown=2
                        }
                    }
                    Log.d("滑动", upAndDown.toString())
                }
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })*/
    }

    /**
     * dx > 0 时为手指向左滚动,列表滚动显示右面的内容
     * dx < 0 时为手指向右滚动,列表滚动显示左面的内容
     * dy > 0 时为手指向上滚动,列表滚动显示下面的内容
     * dy < 0 时为手指向下滚动,列表滚动显示上面的内容
     */
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager
        // 找到第一个和最后一个完全可见项的位置
        if (layoutManager is StaggeredGridLayoutManager) {
            val manager: StaggeredGridLayoutManager = layoutManager
            if (lastCompletelyVisiblePositionList == null) {
                lastCompletelyVisiblePositionList = IntArray(manager.spanCount)
            }
            if (firstCompletelyVisiblePositionList == null) {
                firstCompletelyVisiblePositionList = IntArray(manager.spanCount)
            }
            manager.findFirstCompletelyVisibleItemPositions(firstCompletelyVisiblePositionList)
            manager.findLastCompletelyVisibleItemPositions(lastCompletelyVisiblePositionList)
            lastCompletelyVisibleItemPosition = getMaxPosition(lastCompletelyVisiblePositionList!!)
            firstVisiblePosition = getMinPosition(firstCompletelyVisiblePositionList!!)
        } else if (layoutManager is GridLayoutManager) {
            firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            lastCompletelyVisibleItemPosition =
                layoutManager.findLastCompletelyVisibleItemPosition()
        } else if (layoutManager is LinearLayoutManager) {
            firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
            lastCompletelyVisibleItemPosition =
                layoutManager.findLastCompletelyVisibleItemPosition()
        } else {
            throw RuntimeException("Unsupported LayoutManager.")
        }
    }

    private fun getMinPosition(positions: IntArray): Int {
        var min = positions[0]
        for (i in 1 until positions.size) {
            if (positions[i] < min) {
                min = positions[i]
            }
        }
        return min
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
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//            Log.d("滑动","滑动终止")
            val layoutManager = recyclerView.layoutManager
            val itemCount = layoutManager!!.itemCount
            if (firstVisiblePosition != 0 || lastCompletelyVisibleItemPosition != itemCount - 1) {
                //可滑动，满一屏
                if (recyclerView.isSlideToBottom() || recyclerView.isSlideToRight()) {
                    onScrollToDataEnd()
                }
                if (recyclerView.isSlideToTop() || recyclerView.isSlideToLeft()) {
                    onScrollToDataStart()
                }
            } else {
                //不够一屏
                /*
                Log.d("滑动","不够一屏$upAndDown")
                if (upAndDown == 1) {
                    onScrollToDataStart()
                } else if (upAndDown == 2) {
                    onScrollToDataEnd()
                }
                upAndDown=0*/
                onDataNotFull()
            }
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

    /**
     * 数据不满一瓶，滑动时触发
     */
    abstract fun onDataNotFull()
}
