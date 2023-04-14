package com.kiylx.recyclerviewneko.nekoadapter.config

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate

/**
 * recyclerview和adapter的配置信息
 */
class DefaultConfig<T : Any>(context: Context, rv: RecyclerView) : BaseConfig<T>(context, rv) {
    /**
     * 不能和另一个[addItemViews]同时使用
     * 若使用此方法，可以达到单一类型ViewHolder
     * 使用此方法时，[ViewTypeParser]将不可用
     */
    override fun addItemView(
        layoutId: Int,
        type: Int,
        isThisView: (data: T, position: Int) -> Boolean,
        dataConvert: (holder: BaseViewHolder, data: T, position: Int) -> Unit
    ) {
        val itemview: ItemViewDelegate<T> = object : ItemViewDelegate<T>(type,layoutId) {
            override fun convert(holder: BaseViewHolder, data: T, position: Int) {
                dataConvert(holder, data, position)
            }

            override fun isForViewType(data: T, position: Int): Boolean {
                return isThisView(data, position)
            }

        }
        mItemViewDelegateManager.addDelegate(type, itemview)
        viewTypeParser=null
    }

    /**
     * 添加多种itemview类型
     */
    override fun addItemViews(vararg itemViewDelegates: ItemViewDelegate<T>) {
        //将itemview添加进管理器
        itemViewDelegates.forEach {
            mItemViewDelegateManager.addDelegate(it.type, it)
        }
    }

    /**
     * 判断是否使用了[com.kiylx.recyclerviewneko.viewholder.ItemViewDelegateManager]
     */
    override fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.itemViewDelegateCount > 0
    }

    /**
     * 判断itemview的类型
     */
    override fun getItemViewType(position: Int): Int {
        return mItemViewDelegateManager.getItemViewType(
            mDatas[position],
            position
        )
    }

    /**
     * 根据itemview类型，获取[ItemViewDelegate]
     */
    override fun getItemViewDelegate(viewType: Int): ItemViewDelegate<T> {
        return mItemViewDelegateManager.getItemViewDelegate(viewType)!!;
    }

    /**
     * 创建viewholder，如果指定了[createHolder],则使用[createHolder]创建viewholder
     */
    override fun createHolderInternal(parent: ViewGroup, layoutId: Int): BaseViewHolder {
        return createHolder?.let {
            it(parent, layoutId)
        } ?: BaseViewHolder.createViewHolder(
            context,
            parent,
            layoutId
        )
    }

    /**
     * 将数据绑定到viewholder
     */
    override fun bindData(holder: BaseViewHolder, position: Int) {
        mItemViewDelegateManager.convert(holder, mDatas[position] as T, holder.adapterPosition)
    }

}