package com.kiylx.recyclerviewneko.wrapper.pagestate.config

import android.content.Context
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kiylx.recyclerviewneko.nekoadapter.config.IConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.wrapper.anim.ItemAnimator
import com.kiylx.recyclerviewneko.wrapper.pagestate.StatusWrapperAdapter
import com.kiylx.recyclerviewneko.wrapper.pagestate.base.*

/**
 * 在aplication中配置全局设置
 */
fun test() {
    GlobalWrapperConfig.configStateView {
        this[StateTypes.Empty] = WrapperView(9) {

        }
    }
}

/**
 * 决定有多少种可使用的状态方法
 */
interface IWrapper {

    fun setEmpty(layoutId: Int, block: StatePageViewHolder): IWrapper
    fun setLoading(layoutId: Int, block: StatePageViewHolder): IWrapper
    fun setError(layoutId: Int, block: StatePageViewHolder): IWrapper

    fun showLoading(): IWrapper
    fun showEmpty(): IWrapper
    fun showContent(): IWrapper
    fun showError(): IWrapper
}

class StateWrapperConfig(val context: Context) : IWrapper {

    private val statusViewArr: SparseArrayCompat<WrapperView> = SparseArrayCompat()

    init {
        //添加全局设置的默认值
        val viewSparseArray = GlobalWrapperConfig.wrappedViewArr
        for (i in viewSparseArray.size() - 1 downTo 0) {
            val wrapperView = viewSparseArray.valueAt(i)
            statusViewArr[wrapperView.type] = wrapperView
        }
    }

    /**
     * Whether enable animation.
     * 是否打开动画
     */
    var animationEnable: Boolean = false

    /**
     * Whether the animation executed only the first time.
     * 动画是否仅第一次执行
     */
    var isAnimationFirstOnly = false

    /**
     * Set custom animation.
     * 设置自定义动画
     */
    var itemAnimation: ItemAnimator? = null
        set(value) {
            animationEnable = true
            field = value

//            recyclerView.adapter = stateWrapperAdapter
//            mDatas = mutableListOf(StateTypes.Anim)
//            stateWrapperAdapter.notifyItemChanged(0)
        }

    lateinit var recyclerView: RecyclerView

    /**
     * 包装[beWrappedAdapter]的adapter
     */
    lateinit var stateWrapperAdapter: StatusWrapperAdapter

    /**
     * 被包装起来的adapter
     */
    lateinit var beWrappedAdapter: Adapter<ViewHolder>

    /**
     * 根据此数据，生成不同的状态页
     */
    internal var mDatas: MutableList<StateTypes> = mutableListOf(StateTypes.Content)

    override fun setEmpty(layoutId: Int, block: StatePageViewHolder): IWrapper {
        statusViewArr[StateTypes.Empty] = WrapperView(layoutId, block)
        return this
    }

    override fun setLoading(layoutId: Int, block: StatePageViewHolder): IWrapper {
        statusViewArr[StateTypes.Loading] = WrapperView(layoutId, block)
        return this
    }

    override fun setError(layoutId: Int, block: StatePageViewHolder): IWrapper {
        statusViewArr[StateTypes.Error] = WrapperView(layoutId, block)
        return this
    }

    internal fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (mDatas[0] == StateTypes.Content) {
            beWrappedAdapter.createViewHolder(parent, viewType)
        } else {
            BaseViewHolder.createViewHolder(
                context, parent,
                statusViewArr[viewType]!!.layoutId
            )
        }
    }

    internal fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val type = mDatas[0]
        if (type == StateTypes.Content) {
            beWrappedAdapter.onBindViewHolder(holder, position)
        } else {
            val data = statusViewArr[type]
            data?.statePageViewHolder?.convert(holder.itemView)
                ?: throw Exception("找不到view")
        }

    }

    internal fun getItemCount(): Int {
        val type = mDatas[0]
        if (type == StateTypes.Content) {
            return beWrappedAdapter.itemCount
        } else {
            return 1
        }
    }

    companion object {
        operator fun invoke(config: IConfig): StateWrapperConfig {
            return StateWrapperConfig(config.context).apply {
                beWrappedAdapter = config.iNekoAdapter as Adapter<ViewHolder>
                recyclerView = config.rv
            }
        }

        operator fun invoke(
            contentAdapter: Adapter<ViewHolder>,
            context: Context,
            rv: RecyclerView
        ): StateWrapperConfig {
            return StateWrapperConfig(context).apply {
                beWrappedAdapter = contentAdapter
                recyclerView = rv
            }
        }
    }


    override fun showLoading(): IWrapper {
        recyclerView.adapter = stateWrapperAdapter
        mDatas = mutableListOf(StateTypes.Loading)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    override fun showEmpty(): IWrapper {
        recyclerView.adapter = stateWrapperAdapter
        mDatas = mutableListOf(StateTypes.Empty)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    override fun showContent(): IWrapper {
//        recyclerView.adapter = beWrappedAdapter
        recyclerView.adapter = stateWrapperAdapter
        mDatas = mutableListOf(StateTypes.Content)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    override fun showError(): IWrapper {
        recyclerView.adapter = stateWrapperAdapter
        mDatas = mutableListOf(StateTypes.Error)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    internal fun getChildItemViewType(position: Int): Int {
        if (mDatas[0] == StateTypes.Content) {
            return beWrappedAdapter.getItemViewType(position)
        }
        return mDatas[0].i
    }

    /**
     * 当包裹的adapter数据集发生变化，使用此方法刷新rv以更新页面
     */
    fun refresh() {
        stateWrapperAdapter.notifyItemChanged(0)
    }

    fun doneAndShow() {
        recyclerView.adapter = stateWrapperAdapter
    }
}