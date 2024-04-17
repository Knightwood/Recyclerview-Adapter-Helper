/*
 * WrapperUtils.kt
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