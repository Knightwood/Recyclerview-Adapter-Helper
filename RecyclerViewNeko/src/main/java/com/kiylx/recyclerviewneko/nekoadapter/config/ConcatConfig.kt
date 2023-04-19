package com.kiylx.recyclerviewneko.nekoadapter.config

import android.content.Context
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

class ConcatConfig<T : Any, N : BaseConfig<T>>(val configList: Array<out N>) {
    var rv: RecyclerView? = null
    var context: Context=configList[0].context

    // 1. 定义Config
    val config = ConcatAdapter.Config.Builder()
        .setIsolateViewTypes(true)
        .setStableIdMode(ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS)

    lateinit var concatAdapter: ConcatAdapter

    // 2. 使用ConcatAdapter将Adapter组合起来。
    /**
     * 传入的多个不同[N]，应该设置同样的LayoutManager换rv
     */
    fun show(): ConcatConfig<T, N> {
        concatAdapter = ConcatAdapter(config.build())
        configList.forEachIndexed { index, n ->
            concatAdapter.addAdapter(index, n.iNekoAdapter)
        }
        rv = configList[0].rv
        configList[0].apply {
            rv.adapter = concatAdapter
            rv.layoutManager = layoutManager
        }
        return this
    }
}