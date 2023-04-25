package com.kiylx.recyclerviewneko.nekoadapter

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.nekoadapter.config.createViewHolder
import com.kiylx.recyclerviewneko.nekoadapter.config.dataSize
import com.kiylx.recyclerviewneko.nekoadapter.config.parseItemViewType
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.wrapper.loadstate.NekoPaging3LoadStatusAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class NekoPagingAdapter<T : Any>(
    var config: BaseConfig<T>,
    diffCallback: DiffUtil.ItemCallback<T>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingDataAdapter<T, BaseViewHolder>(diffCallback, mainDispatcher, workerDispatcher),
    INekoAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        config.createViewHolder(parent, viewType)

    override fun getItemCount(): Int = config.dataSize()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        config.bindData(holder, position)

    override fun getItemViewType(position: Int): Int = config.parseItemViewType(position)
    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        config.runAnim(holder)
    }
}

/**
 * 提供recyclerview和adapter的配置信息，viewholder创建方法，viewtype判断等
 */
class NekoPagingAdapterConfig<T : Any>(context: Context, rv: RecyclerView) :
    BaseConfig<T>(context, rv) {
    //如果给pagingDataAdapter添加header和footer，则这里不为null
    var header: NekoPaging3LoadStatusAdapter? = null
    var footer: NekoPaging3LoadStatusAdapter? = null

    /**
     * 当有header或footer时，这里不为null，且应该使用此adapter设置给recyclerview
     */
    var concatAdapter: ConcatAdapter? = null
    lateinit var nekoPagingAdapter: NekoPagingAdapter<T>

    /**
     * 有header或footer时，给pagingAdapter添加上header或footer，
     * 然后设置给recyclerview
     */
    fun done(): NekoPagingAdapterConfig<T> {
        header?.let { it1 ->
            footer?.let { it2 ->
                concatAdapter = nekoPagingAdapter.withLoadStateHeaderAndFooter(it1, it2)
            } ?: let {
                concatAdapter = nekoPagingAdapter.withLoadStateHeader(it1)
            }
        } ?: let {
            footer?.let { it2 ->
                concatAdapter = nekoPagingAdapter.withLoadStateFooter(it2)
            }
        }
        rv.adapter = concatAdapter ?: nekoPagingAdapter
        return this
    }

}