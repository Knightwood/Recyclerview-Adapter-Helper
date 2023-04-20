package com.kiylx.recyclerviewneko.viewholder

import androidx.collection.SparseArrayCompat
import com.kiylx.recyclerviewneko.nekoadapter.config.ViewTypeParser

class ItemViewDelegateManager<T>() {
    /**
     * 根据数据和位置判断应该返回什么类型
     */
    var viewTypeParser: ViewTypeParser<T>? = null

    var delegates: SparseArrayCompat<ItemViewDelegate<T>> =
        SparseArrayCompat<ItemViewDelegate<T>>()

    val itemViewDelegateCount: Int
        get() = delegates.size()

    /**
     * 使用[delegates]的size作为ItemViewDelegate的type
     * 将ItemViewDelegate添加进容器
     */
    fun addDelegate(delegate: ItemViewDelegate<T>): ItemViewDelegateManager<T> {
        val viewType = delegates.size()
        delegates.put(viewType, delegate)
        return this
    }

    /**
     * 自定义ItemViewDelegate的type
     * 将ItemViewDelegate添加进容器[delegates]
     */
    fun addDelegate(
        viewType: Int,
        delegate: ItemViewDelegate<T>
    ): ItemViewDelegateManager<T> {
        if (delegates[viewType] != null) {
            throw IllegalArgumentException(
                "An ItemViewDelegate is already registered for the viewType = "
                        + viewType
                        + ". Already registered ItemViewDelegate is "
                        + delegates[viewType]
            )
        }
        delegates.put(viewType, delegate)
        return this
    }

    fun removeDelegate(delegate: ItemViewDelegate<T>): ItemViewDelegateManager<T> {
        val indexToRemove = delegates.indexOfValue(delegate)
        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove)
        }
        return this
    }

    fun removeDelegate(itemType: Int): ItemViewDelegateManager<T> {
        val indexToRemove = delegates.indexOfKey(itemType)
        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove)
        }
        return this
    }

    /**
     * 遍历[delegates]，并调用[ItemViewDelegate.isForViewType]方法(根据此position及data，
     *  确定ItemViewDelegate是否是与viewholder对应的显示类型 )
     *
     * 如果是此类型，返回[delegates]的key(即viewType)
     */
    fun getItemViewType(data: T, position: Int): Int {
        //如果viewtype解析器存在，使用解析器返回viewtype。否则使用delegate.isForViewType方法
        viewTypeParser?.let {
            return it.parse(data, position)
        } ?: let {
            val delegatesCount = delegates.size()
            for (i in delegatesCount - 1 downTo 0) {
                val delegate: ItemViewDelegate<T> = delegates.valueAt(i)
                if (delegate.isForViewType(data, position)) {
                    //如果此delegate是与data或pos对应显示的ViewHolder，返回delegate的key，即viewType
                    return delegates.keyAt(i)
                }
            }
            throw IllegalArgumentException(
                "No ItemViewDelegate added that matches position=$position in data source"
            )
        }

    }

    fun convert(holder: BaseViewHolder, data: T, position: Int) {
        //如果viewtype解析器存在，使用解析器返回viewtype。否则使用delegate.isForViewType方法
        viewTypeParser?.let {
            val type = it.parse(data, position)
            delegates[type]?.convert(holder, data, position)
                ?: throw IllegalArgumentException(
                    "No ItemViewDelegateManager added that matches position=$position in data source"
                )
            return
        } ?: let {
            val delegatesCount = delegates.size()
            for (i in 0 until delegatesCount) {
                val delegate: ItemViewDelegate<T> = delegates.valueAt(i)
                if (delegate.isForViewType(data, position)) {
                    delegate.convert(holder, data, position)
                    return
                }
            }
            throw IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=$position in data source"
            )
        }
    }

    fun getItemViewDelegate(viewType: Int): ItemViewDelegate<T>? {
        return delegates[viewType]
    }

    /**
     * 若查找不到布局id，返回-1
     */
    fun getItemViewLayoutId(viewType: Int): Int {
        return getItemViewDelegate(viewType)?.layoutId ?: -1
    }

    fun getItemViewType(itemViewDelegate: ItemViewDelegate<T>): Int {
        return delegates.indexOfValue(itemViewDelegate)
    }
}