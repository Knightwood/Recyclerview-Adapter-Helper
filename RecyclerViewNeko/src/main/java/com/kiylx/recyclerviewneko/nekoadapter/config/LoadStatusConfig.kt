package com.kiylx.recyclerviewneko.nekoadapter.config

import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import com.kiylx.recyclerviewneko.nekoadapter.NekoPagingStatusAdapter
import com.kiylx.recyclerviewneko.nekoadapter.PagingStatusConfig
import com.kiylx.recyclerviewneko.utils.RecyclerViewScrollListener
import com.kiylx.recyclerviewneko.utils.addOnScrollListener

class LoadStatusConfig<T : Any, N : BaseConfig<T>>(
    val config: N,//需要添加footer或header的原始adapter
) {
    var header: NekoPagingStatusAdapter? = null
    var footer: NekoPagingStatusAdapter? = null
    lateinit var concatAdapter: ConcatAdapter
    var whenEnd: (() -> Unit)? = null

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
            config.rv.addOnScrollListener(object : RecyclerViewScrollListener() {
                override fun onScrollToBottom() {
                    whenEnd?.invoke()
                }
            })
        }
    }

    /**
     * 设置当滑动到底部时的回调监听
     */
    fun whenScrollToEnd(block: () -> Unit) {
        this.whenEnd = block
    }

    /**
     * 通过此方法改变header的itemview状态
     */
    fun headerState(loadState: LoadState) {
        header?.loadState = loadState
    }

    /**
     * 通过此方法改变footer的itemview状态
     */
    fun footerState(loadState: LoadState) {
        footer?.loadState = loadState
    }

}