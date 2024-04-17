/*
 * IAdapter.kt
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

package com.kiylx.recyclerviewneko.myadapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

sealed interface INekoAdapter {}

/** 布局管理器 */
object Lm {

    fun Context.linear(): LinearLayoutManager {
        val lm = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        return lm
    }

    fun Context.staggeredGrid(spanCount: Int): StaggeredGridLayoutManager {
        val lm = StaggeredGridLayoutManager(spanCount, RecyclerView.VERTICAL)
        return lm
    }

}

/** 整个itemView的单击事件 */
fun interface ItemClickListener {
    /**
     * On item click
     *
     * @param view 被点击的view，可以根据id判断哪个view
     * @param holder
     */
    fun onItemClick(
        view: View,
        holder: BaseViewHolder,
    )
}

/** 整个itemView的长按事件 */
fun interface ItemLongClickListener {
    fun onItemLongClick(
        view: View,
        holder: BaseViewHolder,
    ): Boolean
}