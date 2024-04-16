/*
 * ItemTouchUtils.kt, 2024/4/16 下午8:50
 *
 * Copyright [2023-2024] [KnightWood]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.kiylx.recyclerviewneko.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.myadapter.config.ListAdapterConfig
import com.kiylx.recyclerviewneko.myadapter.config.NormalAdapterConfig

/**
 * 让rv可以上下拖动itemview进行交换， 让rv可以侧滑删除
 *
 * @param canSlideDelete 是否可以侧滑删除
 */
fun <T : Any> NormalAdapterConfig<T>.drag(
    recyclerView: RecyclerView,
    canSlideDelete: Boolean = false,
    direction: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN,
) {
    val config = this
    recyclerView.attachDragListener {
        dragSwapDirection = direction
        //监听移动
        this.dragSwapListener =
            DragPosListener { source: Int, target: Int, sourceAbsolutePosition: Int, targetAbsolutePosition: Int ->
                config.moveData(source, target)
            }
        //监听侧滑
        useSlideSwiped = canSlideDelete
        this.slideSwipedListener =
            SlideSwipedPosListener { target: Int, absoluteAdapterPosition: Int ->
                config.removeData(target)
            }
    }
}

fun <T : Any> ListAdapterConfig<T>.drag(
    recyclerView: RecyclerView,
    canSlideDelete: Boolean = false,
    direction: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN,
) {
    val config = this
    recyclerView.attachDragListener {
        val TAG = "拖动"
        dragSwapDirection = direction
        //监听移动
        this.dragSwapListener =
            DragPosListener { source: Int, target: Int, sourceAbsolutePosition: Int, targetAbsolutePosition: Int ->
                config.myListAdapter.notifyItemMoved(source, target)
            }
        //监听侧滑
        useSlideSwiped = canSlideDelete
        this.slideSwipedListener =
            SlideSwipedPosListener { target: Int, absoluteAdapterPosition: Int ->
                config.removeData(target)
            }
        this.clearViewListener =
            DragPosListener { source, target, sourceAbsolutePosition, targetAbsolutePosition ->
                config.moveData(source, target)
            }
    }
}

/** 为rv添加侧滑和拖动监听 */
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
            /**
             * 该方法的返回值是一个复合标识，这个标识定义了 swip 和 drag 的可滑动方向。
             *
             * 一般我们会通过 makeMovementFlag(int, int) 或 makeFlag(int, int) 方法生成此标识。
             *
             * @param recyclerView
             * @param viewHolder
             * @return
             */
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

            /**
             * 当 ItemTouchHeler 想要 viewHolder 代表的项与 targe 参数代表的项交换位置时 onMove() 会被调用。
             * 如果返回 true 则两者交换位置，否则无法交换位置。
             *
             * 注意：在实现 onMove 方法时要注意如下几点：
             *
             * 在拖动过程中每次两个相邻 Item 之间交换位置都会调用 onMove 方法。 当 onMove 返回 true 时，要调用
             * notifyItemMoved() 方法交换位置，数据源也要做相应交换。
             *
             * @param recyclerView
             * @param viewHolder
             * @param target
             * @return
             */
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

            /**
             * 当用户侧滑删除某项时 onSwiped() 方法会被回调。
             *
             * 注意：onSwiped() 方法被调用时，要调用 notifyItemRemoved() 方法删除项，数据源也要做相应改变。
             *
             * @param viewHolder
             * @param direction
             */
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (touchConfig.useSlideSwiped)
                    touchConfig.slideSwipedListener?.onSwap(
                        viewHolder.bindingAdapterPosition,
                        viewHolder.absoluteAdapterPosition
                    )

            }

            /**
             * 当用户和视图之间交互结束并动画完成时，此方法被调用。 在此方法中可以清理在 onSelectedChanged()、onChildDraw()
             * 方法中做的操作以及动画等。
             *
             * @param recyclerView
             * @param viewHolder
             */
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

        /** 是否启用拖拽交换 */
        var useDragSwap = true

        /** 拖动交换item的回调 */
        var dragSwapListener: DragPosListener? = null


        var clearViewListener: DragPosListener? = null

        /** 可拖拽方向 */
        var dragSwapDirection: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN

        //======================================侧滑================================//

        /** 是否开启侧滑 */
        var useSlideSwiped = false

        /** 侧滑item的回调 */
        var slideSwipedListener: SlideSwipedPosListener? = null

        /** 侧滑方向 */
        var slideSwipedDirection: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    }
}



