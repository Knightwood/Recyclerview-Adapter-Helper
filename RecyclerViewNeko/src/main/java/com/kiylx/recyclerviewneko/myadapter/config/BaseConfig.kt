package com.kiylx.recyclerviewneko.myadapter.config

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.viewholder.DelegatePair
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegateManager
import com.kiylx.recyclerviewneko.wrapper.anim.ItemAnimator

/** 视图类型解析 */
fun interface ViewTypeParser<T> {
    fun parse(data: T, pos: Int): Int
}

sealed class IConfig() {
    lateinit var iRecyclerViewAdapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>
}

/** adapter的配置，创建viewholder,判断viewtype等一系列的方法 */
sealed class BaseConfig<T : Any> : IConfig(), LifecycleEventObserver {
    //动画配置
    protected val animConfig: AnimConfig by lazy { AnimConfig() }

//    /**
//     * 使用concatAdapter连接多个adapter，同时用[PageStateWrapperAdapter]添加状态页时是否要监听数据变更
//     * 标记为true的，将会监听数据变更决定状态页变更
//     */
//    var canObserveDataChange = true

    var layoutManager: RecyclerView.LayoutManager? = null
    internal var mItemViewDelegateManager = ItemViewDelegateManager<T>()//管理itemview相关配置

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


    //指定此匿名函数，可以手动创建viewholder
    var createHolder: ((parent: ViewGroup, layoutId: Int, viewType: Int) -> BaseViewHolder)? = null


//    /**
//     * 监听数据变化
//     */
//    open fun observeDataLists(observer: AdapterDataObserver) {
//        rv.adapter?.registerAdapterDataObserver(observer)
//    }
//
//    /**
//     * 取消监听数据变化
//     */
//    open fun unObserveDataLists(observer: AdapterDataObserver) {
//        rv.adapter?.unregisterAdapterDataObserver(observer)
//    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {

    }

    fun addItemView(layoutId: Int,block:ItemViewDelegate<T>.()->Unit) {
        if (viewTypeParser != null) {
            throw IllegalArgumentException("viewTypeParser should be null")
        }
        val itemview = ItemViewDelegate<T>(layoutId)
        itemview.block()
        mItemViewDelegateManager.addDelegate(itemview)
    }

    /**
     * 添加多种itemview类型
     *
     * @param layoutId viewHolder布局id
     * @param type 标识viewHolder的类型，即vieType
     */
    open fun addItemView(
        layoutId: Int,
        type: Int,
        block:ItemViewDelegate<T>.()->Unit
    ) {
        if (viewTypeParser == null) {
            throw NullPointerException("viewTypeParser cannot be null")
        }
        val itemview: ItemViewDelegate<T> = ItemViewDelegate<T>(layoutId)
        itemview.block()
        mItemViewDelegateManager.addDelegate(type, itemview)
    }

    /** 添加多种itemview类型 */
    open fun addItemViews(vararg itemViewDelegates: DelegatePair<T>) {
        if (viewTypeParser == null) {
            throw NullPointerException("viewTypeParser cannot be null")
        }
        //将itemview添加进管理器
        itemViewDelegates.forEach {
            mItemViewDelegateManager.addDelegate(it.type, it.delegate)
        }
    }

    /** 添加多种itemview类型 */
    open fun addItemViews(vararg itemViewDelegates: ItemViewDelegate<T>) {
        if (viewTypeParser != null) {
            throw IllegalArgumentException("viewTypeParser should be null")
        }
        //将itemview添加进管理器
        itemViewDelegates.forEach {
            mItemViewDelegateManager.addDelegate(it)
        }
    }

    /** 判断是否使用了[com.kiylx.recyclerviewneko.viewholder.ItemViewDelegateManager] */
    internal fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.itemViewDelegateCount > 0
    }

    /** 判断itemview的类型 */
    internal fun getItemViewType(position: Int, data: T): Int {
        return if (!useItemViewDelegateManager()) {
            throw Exception("没有类型信息")
        } else {
            mItemViewDelegateManager.getItemViewType(
                data,
                position
            )
        }
    }

    /** 根据itemview类型，获取[ItemViewDelegate] */
    internal open fun getItemViewDelegate(viewType: Int): ItemViewDelegate<T> {
        return mItemViewDelegateManager.getItemViewDelegate(viewType)!!
    }

    /** 创建viewholder，如果指定了[createHolder],则使用[createHolder]创建viewholder */
    internal fun createViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val itemViewDelegate = getItemViewDelegate(viewType)
        val layoutId: Int = itemViewDelegate.layoutId
        val vh = createHolder?.let {
            //让createHolder接管创建过程
            it(parent, layoutId, viewType)
        } ?: BaseViewHolder.createViewHolder(
            parent.context,
            parent,
            layoutId
        ) {
            //干预创建过程
            itemViewDelegate.createConvert(this)
        }
        setClickListener(vh, itemViewDelegate)
        setLongListener(vh, itemViewDelegate)
        return vh
    }


    /** 将数据绑定到viewholder */
    internal fun bindData(holder: BaseViewHolder, position: Int, data: T) {
        mItemViewDelegateManager.convert(
            holder,
            data,
            holder.bindingAdapterPosition
        )

        holder._data = data
        holder._pos = position

    }


    /** 更改动画配置，设置动画等 */
    fun configAnim(block: AnimConfig.() -> Unit) {
        animConfig.block()
    }

    /** 设置自定义动画，使用默认的动画配置 若调用[configAnim]方法，可以直接在block中设置动画，这种情况下没必要一定在这里设置动画 */
    var itemAnimation: ItemAnimator?
        set(value) {
            animConfig.itemAnimation = value
        }
        get() {
            return animConfig.itemAnimation
        }

    /** adapter调用此方法开始动画的播放 */
    internal fun runAnim(holder: RecyclerView.ViewHolder) {
        animConfig.runAnim(holder)
    }
}