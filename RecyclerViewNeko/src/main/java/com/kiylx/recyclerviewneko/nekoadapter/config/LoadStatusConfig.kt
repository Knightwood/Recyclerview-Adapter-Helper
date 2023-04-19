package com.kiylx.recyclerviewneko.nekoadapter.config

import androidx.recyclerview.widget.ConcatAdapter
import com.kiylx.recyclerviewneko.nekoadapter.NekoPagingStatusAdapter
import com.kiylx.recyclerviewneko.nekoadapter.PagingStatusConfig

class LoadStatusConfig<T : Any, N : BaseConfig<T>>(
    val config: N,//需要添加footer或header的原始adapter
) {
    var header: NekoPagingStatusAdapter? = null
    var footer: NekoPagingStatusAdapter? = null
    lateinit var concatAdapter: ConcatAdapter

    /**
     * 给pagingAdapter添加状态加载状态item
     */
    fun withFooter(block: PagingStatusConfig.() -> Unit) {
        val tmp = PagingStatusConfig()
        tmp.block()
        footer = NekoPagingStatusAdapter(tmp)
    }

    /**
     * 给pagingAdapter添加状态加载状态item
     */
    fun withHeader(block: PagingStatusConfig.() -> Unit) {
        val tmp = PagingStatusConfig()
        tmp.block()
        header = NekoPagingStatusAdapter(tmp)
    }

    fun done() {
        if (this::concatAdapter.isInitialized) {
            throw Exception("已经初始化完成")
        } else {
            concatAdapter = ConcatAdapter(header, config.iNekoAdapter, footer)
            config.rv.adapter = concatAdapter
        }
    }

}