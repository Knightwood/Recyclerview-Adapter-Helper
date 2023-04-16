package com.kiylx.recyclerviewneko.nekoadapter.config

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.nekoadapter.*
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegateManager
import com.kiylx.recyclerviewneko.nekoadapter.Lm.linear
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate

/**
 * 视图类型解析
 */
fun interface ViewTypeParser<T> {
    fun parse(data: T, pos: Int): Int
}

/**
 * adapter的配置，创建viewholder,判断viewtype等一系列的方法
 */
abstract class BaseConfig<T : Any>(
    internal var context: Context,
    var rv: RecyclerView,
) {
    lateinit var iNekoAdapter: RecyclerView.Adapter<BaseViewHolder>

    var layoutManager: RecyclerView.LayoutManager = context.linear()
    var mItemViewDelegateManager = ItemViewDelegateManager<T>()//管理itemview相关配置

    /**
     * 作用：根据数据和位置判断应该返回什么类型
     *
     * 如果此解析器为null，则使用[ItemViewDelegate.isForViewType]进行判断viewtype
     */
    var viewTypeParser: ViewTypeParser<T>? = null
        set(value) {
            field = value
            mItemViewDelegateManager.viewTypeParser = value
        }

    var mDatas: MutableList<T> = mutableListOf()

    //指定此匿名函数，可以手动创建viewholder
    var createHolder: ((parent: ViewGroup, layoutId: Int) -> BaseViewHolder)? = null

    var clickEnable = true    //itemview是否可点击
    var longClickEnable = false    //itemview是否可长按
    var itemClickListener: ItemClickListener? = null
    var itemLongClickListener: ItemLongClickListener? = null

    /**
     * 不能和另一个[addItemViews]同时使用
     * 若使用此方法，可以达到单一类型ViewHolder
     * 使用此方法时，[ViewTypeParser]将不可用
     */
   open fun addItemView(
        layoutId: Int,
        type: Int,
        isThisView: (data: T, position: Int) -> Boolean,
        dataConvert: (holder: BaseViewHolder, data: T, position: Int) -> Unit
    ) {
        val itemview: ItemViewDelegate<T> = object : ItemViewDelegate<T>(type, layoutId) {
            override fun convert(holder: BaseViewHolder, data: T, position: Int) {
                dataConvert(holder, data, position)
            }

            override fun isForViewType(data: T, position: Int): Boolean {
                return isThisView(data, position)
            }

        }
        mItemViewDelegateManager.addDelegate(type, itemview)
        viewTypeParser = null
    }

    /**
     * 添加多种itemview类型
     */
   open fun addItemViews(vararg itemViewDelegates: ItemViewDelegate<T>) {
        //将itemview添加进管理器
        itemViewDelegates.forEach {
            mItemViewDelegateManager.addDelegate(it.type, it)
        }
    }

    /**
     * 判断是否使用了[com.kiylx.recyclerviewneko.viewholder.ItemViewDelegateManager]
     */
    fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.itemViewDelegateCount > 0
    }

    /**
     * 判断itemview的类型
     */
    open fun getItemViewType(position: Int): Int {
        return mItemViewDelegateManager.getItemViewType(
            mDatas[position],
            position
        )
    }

    /**
     * 根据itemview类型，获取[ItemViewDelegate]
     */
    open fun getItemViewDelegate(viewType: Int): ItemViewDelegate<T> {
        return mItemViewDelegateManager.getItemViewDelegate(viewType)!!;
    }

    /**
     * 创建viewholder，如果指定了[createHolder],则使用[createHolder]创建viewholder
     */
    open fun createHolderInternal(parent: ViewGroup, layoutId: Int): BaseViewHolder {
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
    open fun bindData(holder: BaseViewHolder, position: Int) {
        mItemViewDelegateManager.convert(holder, mDatas[position] as T, holder.adapterPosition)
    }

    /**
     * 刷新recyclerview
     */
    @SuppressLint("NotifyDataSetChanged")
    open fun refreshData(datas: MutableList<T>) {
        mDatas.clear()
        mDatas.addAll(datas)
        iNekoAdapter.notifyDataSetChanged()
    }

}