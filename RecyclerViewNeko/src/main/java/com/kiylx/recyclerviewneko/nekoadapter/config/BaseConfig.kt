package com.kiylx.recyclerviewneko.nekoadapter.config

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.kiylx.recyclerviewneko.nekoadapter.*
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegateManager
import com.kiylx.recyclerviewneko.nekoadapter.Lm.linear
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate
import com.kiylx.recyclerviewneko.wrapper.pagestate.StatusWrapperAdapter

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
) : LifecycleEventObserver {
    /**
     * 使用concatAdapter连接多个adapter，同时用[StatusWrapperAdapter]添加状态页时是否要监听数据变更
     * 标记为true的，将会监听数据变更决定状态页变更
     */
    var canObserveDataChange = true
    lateinit var iNekoAdapter: RecyclerView.Adapter<BaseViewHolder>

    var layoutManager: RecyclerView.LayoutManager = context.linear()
    private var mItemViewDelegateManager = ItemViewDelegateManager<T>()//管理itemview相关配置

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
     * 添加单个的itemViewDelegate，即仅有一种viewHolder
     * 多次调用将报错
     * @param dataConvert 将data绑定到此viewHolder
     * @param layoutId viewHolder布局id
     */
    open fun setSingleItemView(
        layoutId: Int,
        dataConvert: (holder: BaseViewHolder, data: T, position: Int) -> Unit
    ) {
        addItemView(
            layoutId = layoutId,
            type = 0,
            dataConvert = dataConvert
        )
    }

    /**
     * 监听数据变化
     */
    open fun observeDataLists(observer: AdapterDataObserver) {
        rv.adapter?.registerAdapterDataObserver(observer)
    }

    /**
     * 取消监听数据变化
     */
    open fun unObserveDataLists(observer: AdapterDataObserver) {
        rv.adapter?.unregisterAdapterDataObserver(observer)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    }
    /**
     * 添加多种itemview类型
     * @param layoutId viewHolder布局id
     * @param type 标识viewHolder的类型，即vieType
     * @param isThisView 在多种viewHolder下，此方法用来识别某viewHolder是否应该显示data类型的数据，是的话，返回true
     * @param dataConvert 将data绑定到此viewHolder
     */
    open fun addItemView(
        layoutId: Int,
        type: Int,
        isThisView: (data: T, position: Int) -> Boolean = { _, _ -> true },
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
    internal fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.itemViewDelegateCount > 0
    }

    /**
     * 判断itemview的类型
     */
    internal fun getItemViewType(position: Int): Int {
        return mItemViewDelegateManager.getItemViewType(
            mDatas[position],
            position
        )
    }

    /**
     * 根据itemview类型，获取[ItemViewDelegate]
     */
    internal open fun getItemViewDelegate(viewType: Int): ItemViewDelegate<T> {
        return mItemViewDelegateManager.getItemViewDelegate(viewType)!!;
    }

    /**
     * 创建viewholder，如果指定了[createHolder],则使用[createHolder]创建viewholder
     */
    internal fun createHolderInternal(parent: ViewGroup, layoutId: Int): BaseViewHolder {
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
    internal fun bindData(holder: BaseViewHolder, position: Int) {
        mItemViewDelegateManager.convert(
            holder,
            mDatas[position] as T,
            holder.bindingAdapterPosition
        )
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