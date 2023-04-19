package com.kiylx.recyclerviewneko.nekoadapter.config

import android.content.Context
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.wrapper.StatusWrapperAdapter
import com.kiylx.recyclerviewneko.wrapper.base.*

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

class WrapperConfig(val context: Context) : IWrapper {
    lateinit var recyclerView: RecyclerView
    lateinit var stateWrapperAdapter: StatusWrapperAdapter
    private val wrappedViewArr: SparseArrayCompat<WrapperView> = SparseArrayCompat()

    init {
        //添加全局设置的默认值
        val viewSparseArray = GlobalWrapperConfig.wrappedViewArr
        for (i in viewSparseArray.size() - 1 downTo 0) {
            val wrapperView = viewSparseArray.valueAt(i)
            wrappedViewArr[wrapperView.type] = wrapperView
        }
    }

    /**
     * 被包装起来的adapter
     */
    lateinit var wrappedAdapter: Adapter<BaseViewHolder>

    /**
     * 根据此数据，生成不同的状态页
     */
    var mDatas: MutableList<StateTypes> = mutableListOf()

    override fun setEmpty(layoutId: Int, block: StatePageViewHolder): IWrapper {
        wrappedViewArr[StateTypes.Empty] = WrapperView(layoutId, block)
        return this
    }

    override fun setLoading(layoutId: Int, block: StatePageViewHolder): IWrapper {
        wrappedViewArr[StateTypes.Loading] = WrapperView(layoutId, block)
        return this
    }

    override fun setError(layoutId: Int, block: StatePageViewHolder): IWrapper {
        wrappedViewArr[StateTypes.Error] = WrapperView(layoutId, block)
        return this
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (mDatas[0] == StateTypes.Content) {
            wrappedAdapter.createViewHolder(parent, viewType)
        } else {
            BaseViewHolder.createViewHolder(
                context, parent,
                wrappedViewArr[viewType]!!.layoutId
            )
        }
    }

    fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        wrappedViewArr[mDatas[0]]?.statePageViewHolder?.convert(holder.getConvertView())
            ?: throw Exception("找不到view")
    }

    fun getItemCount(): Int = 1

    companion object {
        operator fun <T : Any> invoke(config: BaseConfig<T>): WrapperConfig {
            return WrapperConfig(config.context).apply {
                wrappedAdapter = config.iNekoAdapter
                recyclerView = config.rv
            }
        }

        operator fun <T : Any, N : BaseConfig<T>> invoke(config: ConcatConfig<T, N>): WrapperConfig {
            return WrapperConfig(config.context).apply {
                wrappedAdapter = config.concatAdapter as Adapter<BaseViewHolder>
                recyclerView = config.rv!!
            }
        }
    }


    override fun showLoading(): IWrapper {
        mDatas = mutableListOf(StateTypes.Loading)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    override fun showEmpty(): IWrapper {
        mDatas = mutableListOf(StateTypes.Empty)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    override fun showContent(): IWrapper {
        mDatas = mutableListOf(StateTypes.Content)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    override fun showError(): IWrapper {
        mDatas = mutableListOf(StateTypes.Error)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }
}