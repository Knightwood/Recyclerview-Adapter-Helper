package com.kiylx.recyclerviewneko.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * 为rv添加侧滑和拖动监听
 */
fun RecyclerView.attachDragListener(block: ItemTouchUtils.TouchConfig.() -> Unit) {
    val touchConfig = ItemTouchUtils.TouchConfig()
    touchConfig.block()
    ItemTouchUtils(this, touchConfig)
}

fun interface DragPosListener {
    fun onSwap(source: Int, target: Int, sourceAbsolutePosition: Int, targetAbsolutePosition: Int)
}

fun interface SlideSwipedPosListener {
    fun onSwap(target: Int, absoluteAdapterPosition: Int)
}

class ItemTouchUtils(private val recyclerView: RecyclerView, val touchConfig: TouchConfig) {
    private var isFirstMove = true
    private var fromPosition = 0
    private var fromAbsolutePosition = 0

    init {
        completeConfig()
    }

    private fun completeConfig() {
        val callback: ItemTouchHelper.Callback = object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                //侧滑方向
                val slideSwipedFlag =
                    if (touchConfig.useSlideSwiped) touchConfig.slideSwipedDirection else 0
                //拖动方向
                val dragSwapFlag: Int =
                    if (touchConfig.useDragSwap) touchConfig.dragSwapDirection else 0
                return makeMovementFlags(dragSwapFlag, slideSwipedFlag)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                if (touchConfig.useDragSwap) {
                    //记录下第一次滑动的位置，因为滑动过程中会反复调用这个方法，导致得到的position一直在变
                    //onMove方法会被反复调用，因此试图更新放在clearView比较好
                    if (isFirstMove) {
                        fromPosition = viewHolder.bindingAdapterPosition
                        fromAbsolutePosition = viewHolder.absoluteAdapterPosition
                        isFirstMove = false
                    }
                    //正常只要有下面这一句就可以看到拖动效果了，但后面列表的position会乱掉，所以处理起来就要稍稍变更一下了
                    touchConfig.dragSwapListener?.onSwap(
                        viewHolder.bindingAdapterPosition,
                        target.bindingAdapterPosition,
                        viewHolder.absoluteAdapterPosition,
                        target.absoluteAdapterPosition
                    )
                }
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (touchConfig.useSlideSwiped)
                    touchConfig.slideSwipedListener?.onSwap(
                        viewHolder.bindingAdapterPosition,
                        viewHolder.absoluteAdapterPosition
                    )

            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                touchConfig.clearViewListener?.onSwap(
                    fromPosition,
                    viewHolder.bindingAdapterPosition,
                    fromAbsolutePosition,
                    viewHolder.absoluteAdapterPosition
                )
                isFirstMove = true
            }
        }
        val itemTouch = ItemTouchHelper(callback)
        itemTouch.attachToRecyclerView(recyclerView)
    }

    class TouchConfig {

        /**
         * 是否启用拖拽交换
         */
        var useDragSwap = true

        /**
         * 拖动交换item的回调
         */
        var dragSwapListener: DragPosListener? = null


        var clearViewListener: DragPosListener? = null

        /**
         * 可拖拽方向
         */
        var dragSwapDirection: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN

        //======================================侧滑================================//

        /**
         * 是否开启侧滑
         */
        var useSlideSwiped = false

        /**
         * 侧滑item的回调
         */
        var slideSwipedListener: SlideSwipedPosListener? = null

        /**
         * 侧滑方向
         */
        var slideSwipedDirection: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }
}



