package com.kiylx.recyclerviewneko.nekoadapter.config

import android.view.View
import android.view.ViewGroup
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

/**
 * adapter中的方法实现由这里提供
 */
class AdapterHelper

/**
 * 返回数据列表size
 */
fun <T : Any> BaseConfig<T>.dataSize() = mDatas.size

/**
 * 创建viewholder
 */
fun <T : Any> BaseConfig<T>.createViewHolder(
    parent: ViewGroup,
    viewType: Int
): BaseViewHolder {
    val itemViewDelegate = getItemViewDelegate(viewType)
    val layoutId: Int = itemViewDelegate.layoutId
    val holder: BaseViewHolder = createHolderInternal(parent, layoutId)
    setClickListener(parent, holder, viewType)
    setLongListener(parent, holder, viewType)
    return holder
}

/**
 * 获取viewtype
 */
fun <T : Any> BaseConfig<T>.parseItemViewType(position: Int): Int {
    return if (!useItemViewDelegateManager()) {
        throw Exception("没有类型信息")
    } else {
        getItemViewType(position)
    }
}

/**
 * 给itemview设置长按事件
 */
fun <T : Any> BaseConfig<T>.setLongListener(
    parent: ViewGroup,
    holder: BaseViewHolder,
    viewType: Int
) {
    if (longClickEnable) {
        holder.getConvertView().setOnLongClickListener(View.OnLongClickListener { v ->
            return@OnLongClickListener itemLongClickListener?.let {
                val pos = holder.adapterPosition
                it.onItemLongClick(v, holder, pos)
                true
            } ?: false
        })
    }
}

/**
 * 给itemview设置点击事件
 */
fun <T : Any> BaseConfig<T>.setClickListener(
    parent: ViewGroup,
    holder: BaseViewHolder,
    viewType: Int
) {
    if (clickEnable) {
        holder.getConvertView().setOnClickListener(View.OnClickListener { v ->
            itemClickListener?.let {
                val pos = holder.adapterPosition
                it.onItemClick(v, holder, pos)
            }
        })
    }
}