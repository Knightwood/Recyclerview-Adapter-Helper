package com.kiylx.recyclerviewneko.wrapper.loadstate

import android.util.Log
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.kiylx.recyclerviewneko.nekoadapter.Lm.linear
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.utils.RecyclerViewScrollListener
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

/**
 * 生成header和footer（其实就是持有一个itemview的LoadStateAdapter），
 * 通过ConcatAdapter包装给普通Adapter
 */
class NekoAdapterLoadStatusWrapperUtil(
    val rv: RecyclerView,
    val adapter: Adapter<BaseViewHolder>//需要添加footer或header的原始adapter
) {
    var header: NekoPaging3LoadStatusAdapter? = null
    var footer: NekoPaging3LoadStatusAdapter? = null

    //给普通adapter通过使用concatAdapter的方式添加header和footer
    lateinit var concatAdapter: ConcatAdapter
    var whenEnd: (() -> Unit)? = null
    var whenTop: (() -> Unit)? = null

    /**
     * 给pagingAdapter添加状态加载状态item
     * 生成一个包含itemview的NekoPaging3LoadStatusAdapter
     * @param block 配置NekoPaging3LoadStatusAdapter，添加itemview
     */
    fun withFooter(block: Paging3LoadStatusConfig.() -> Unit) {
        val tmp = Paging3LoadStatusConfig()
        tmp.block()
        footer = NekoPaging3LoadStatusAdapter(tmp)
    }

    /**
     * 给pagingAdapter添加状态加载状态item
     * 生成一个包含itemview的NekoPaging3LoadStatusAdapter
     * @param block 配置NekoPaging3LoadStatusAdapter，添加itemview
     */
    fun withHeader(block: Paging3LoadStatusConfig.() -> Unit) {
        val tmp = Paging3LoadStatusConfig()
        tmp.block()
        header = NekoPaging3LoadStatusAdapter(tmp)
    }

    /**
     * 完成包装，且监听recyclerview的滑动，在滑动到底部时，触发[whenEnd]的回调
     */
    fun done() {
        if (this::concatAdapter.isInitialized) {
            throw Exception("已经初始化完成")
        } else {
            // 1. 定义Config
            val config = ConcatAdapter.Config.Builder()
                .setIsolateViewTypes(true)
                .setStableIdMode(ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS)
                .build()
            header?.let { it1 ->
                footer?.let { it2 ->
                    concatAdapter = ConcatAdapter(config, header, adapter, footer)
                } ?: let {
                    concatAdapter = ConcatAdapter(config, header, adapter)
                }
            } ?: let {
                footer?.let { it2 ->
                    concatAdapter = ConcatAdapter(config, adapter, footer)
                } ?: let {
                    throw Exception("header and footer does not exist!!")
                }
            }
            rv.adapter = concatAdapter
            rv.addOnScrollListener(object : RecyclerViewScrollListener() {
                override fun onScrollToDataEnd() {
                    Log.d("tag", "scroll to onScrollToDataEnd")
                    whenEnd?.invoke()
                }

                override fun onScrollToDataStart() {
                    Log.d("tag", "scroll to onScrollToDataStart")
                    whenTop?.invoke()
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
     * 设置当滑动到顶部时的回调监听
     */
    fun whenScrollToTop(block: () -> Unit) {
        this.whenTop = block
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