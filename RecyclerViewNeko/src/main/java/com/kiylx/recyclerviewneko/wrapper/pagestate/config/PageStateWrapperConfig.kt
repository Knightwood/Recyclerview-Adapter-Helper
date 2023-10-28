package com.kiylx.recyclerviewneko.wrapper.pagestate.config

import android.content.Context
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kiylx.recyclerviewneko.nekoadapter.config.IConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.wrapper.pagestate.PageStateWrapperAdapter
import com.kiylx.recyclerviewneko.wrapper.pagestate.base.*

/**
 * 在aplication中配置全局设置
 */
fun test() {
    GlobalWrapperConfig.configStateView {
        this[PageStateTypes.Empty] = PageStateWrapperView(android.R.id.edit) {

        }
        this[PageStateTypes.Error] = PageStateWrapperView(android.R.id.edit){

        }
    }
}

/**
 * 决定有多少种可使用的状态方法
 */
interface IWrapper {

    fun setEmpty(layoutId: Int, block: PageStateItemDelegate): IWrapper
    fun setLoading(layoutId: Int, block: PageStateItemDelegate): IWrapper
    fun setError(layoutId: Int, block: PageStateItemDelegate): IWrapper

    fun showLoading(): IWrapper
    fun showEmpty(): IWrapper
    fun showContent(): IWrapper
    fun showError(): IWrapper
    fun showStatePage(): IWrapper
}

class StateWrapperConfig(val context: Context) : IWrapper {

    private val statusViewArr: SparseArrayCompat<PageStateWrapperView> = SparseArrayCompat()

    init {
        //添加全局设置的默认值
        val viewSparseArray = GlobalWrapperConfig.wrappedViewArr
        for (i in viewSparseArray.size() - 1 downTo 0) {
            val wrapperView = viewSparseArray.valueAt(i)
            statusViewArr[wrapperView.type] = wrapperView
        }
    }


    lateinit var recyclerView: RecyclerView

    /**
     * 包装[beWrappedAdapter]的adapter
     */
    lateinit var stateWrapperAdapter: PageStateWrapperAdapter

    /**
     * 被包装起来的adapter
     */
    lateinit var beWrappedAdapter: Adapter<ViewHolder>

    /**
     * 根据此数据，生成不同的状态页
     */
    internal var mDatas: MutableList<PageStateTypes> = mutableListOf(PageStateTypes.Empty)

    override fun setEmpty(layoutId: Int, block: PageStateItemDelegate): IWrapper {
        statusViewArr[PageStateTypes.Empty] = PageStateWrapperView(layoutId, block)
        return this
    }

    override fun setLoading(layoutId: Int, block: PageStateItemDelegate): IWrapper {
        statusViewArr[PageStateTypes.Loading] = PageStateWrapperView(layoutId, block)
        return this
    }

    override fun setError(layoutId: Int, block: PageStateItemDelegate): IWrapper {
        statusViewArr[PageStateTypes.Error] = PageStateWrapperView(layoutId, block)
        return this
    }

    internal fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return BaseViewHolder.createViewHolder(
            context, parent,
            statusViewArr[viewType]!!.layoutId
        )
    }

    internal fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val type = mDatas[0]

            val data = statusViewArr[type]
            data?.pageStateItemDelegate?.convert(holder.itemView)
                ?: throw Exception("找不到view")
    }

    internal fun getItemCount(): Int {
        return 1
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
        mDatas = mutableListOf(PageStateTypes.Loading)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    override fun showEmpty(): IWrapper {
        recyclerView.adapter = stateWrapperAdapter
        mDatas = mutableListOf(PageStateTypes.Empty)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    override fun showContent(): IWrapper {
        recyclerView.adapter = beWrappedAdapter
        return this
    }

    override fun showError(): IWrapper {
        recyclerView.adapter = stateWrapperAdapter
        mDatas = mutableListOf(PageStateTypes.Error)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    internal fun getChildItemViewType(position: Int): Int {
        return mDatas[0].i
    }

    override fun showStatePage(): IWrapper {
        recyclerView.adapter = stateWrapperAdapter
        return this
    }
}