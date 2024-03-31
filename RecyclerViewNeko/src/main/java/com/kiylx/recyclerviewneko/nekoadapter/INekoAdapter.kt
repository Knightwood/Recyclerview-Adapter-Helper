package com.kiylx.recyclerviewneko.nekoadapter

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
fun interface ItemClickListener<T> {
    fun onItemClick(
        view: View,
        holder: BaseViewHolder,
        bindingAdapterPosition: Int,
        position: Int,
        data: T
    )
}

/** 整个itemView的长按事件 */
fun interface ItemLongClickListener<T> {
    fun onItemLongClick(
        view: View,
        holder: BaseViewHolder,
        bindingAdapterPosition: Int,
        position: Int,
        data: T
    ): Boolean
}