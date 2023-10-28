package com.kiylx.recyclerviewneko.nekoadapter.config

import android.content.Context
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.nekoadapter.NekoPagingAdapter
import com.kiylx.recyclerviewneko.wrapper.loadstate.NekoPaging3LoadStatusAdapter
import com.kiylx.recyclerviewneko.wrapper.loadstate.Paging3LoadStatusConfig

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


    //<editor-fold desc="给PagingAdapter添加header和footer">
    /**
     * 给pagingAdapter添加状态加载状态item
     */
    inline fun withHeader(block: Paging3LoadStatusConfig.() -> Unit): NekoPagingAdapterConfig<T> {
        val tmp = Paging3LoadStatusConfig()
        tmp.block()
        val header = NekoPaging3LoadStatusAdapter(tmp)
        this.header = header
        return this
    }

    /**
     * 给pagingAdapter添加状态加载状态item
     */
    inline fun withFooter(block: Paging3LoadStatusConfig.() -> Unit): NekoPagingAdapterConfig<T> {
        val tmp = Paging3LoadStatusConfig()
        tmp.block()
        val footer = NekoPaging3LoadStatusAdapter(tmp)
        this.footer = footer
        return this
    }
//</editor-fold>


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