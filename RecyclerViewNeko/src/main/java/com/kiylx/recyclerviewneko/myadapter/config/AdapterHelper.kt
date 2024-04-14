package com.kiylx.recyclerviewneko.myadapter.config

import android.view.View
import androidx.core.util.forEach
import androidx.core.util.isNotEmpty
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate


/** 给itemview设置长按事件 */
internal fun <T : Any> setLongListener(
    holder: BaseViewHolder,
    itemViewDelegate: ItemViewDelegate<T>,
) {

    //触发点击事件时，找到稀疏数组中view的id对应的listener来调用
    val listener = object : View.OnClickListener {
        override fun onClick(v: View) {
            itemViewDelegate.longClicks[v.id]?.onItemLongClick(v, holder)
        }
    }
    if (itemViewDelegate.longClicks.isNotEmpty()) {
        itemViewDelegate.longClicks.forEach { key, event ->
            holder.getView<View>(key)?.setOnClickListener(listener)
        }
    }
}


/** 给itemview设置点击事件 */
internal fun <T : Any> setClickListener(
    holder: BaseViewHolder,
    itemViewDelegate: ItemViewDelegate<T>
) {
    //触发点击事件时，找到稀疏数组中view的id对应的listener来调用
    val listener = object : View.OnClickListener {
        override fun onClick(v: View) {
            itemViewDelegate.clicks[v.id]?.onItemClick(v, holder)
        }
    }
    if (itemViewDelegate.clicks.isNotEmpty()) {
        itemViewDelegate.clicks.forEach { key, event ->
            holder.getView<View>(key)?.setOnClickListener(listener)
        }
    }

}