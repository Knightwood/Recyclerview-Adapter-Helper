package com.kiylx.recyclerviewneko.nekoadapter.config

import android.content.Context
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.wrapper.anim.ItemAnimator

class ConcatConfig<T : Any, N : BaseConfig<T>>(
    val configList: Array<out N>,
    context: Context,
    rv: RecyclerView,
) : IConfig(context, rv) {

    // 1. 定义Config
    private val concatConfigBuilder = ConcatAdapter.Config.Builder()
        .setIsolateViewTypes(true)
        .setStableIdMode(ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS)

    lateinit var concatAdapter: ConcatAdapter

    /**
     * 调整concatConfig
     */
    fun changeConcatConfig(block: ConcatAdapter.Config.Builder.() -> Unit): ConcatConfig<T, N> {
        concatConfigBuilder.block()
        return this
    }

    /**
     * 给每个子adapter设置动画
     */
    fun setAnim(anim: ItemAnimator): ConcatConfig<T, N> {
        configList.forEach {
            it.itemAnimation = anim
        }
        return this
    }

    // 2. 使用ConcatAdapter将Adapter组合起来。
    /**
     * 传入的多个不同[N]，应该设置同样的LayoutManager换rv
     */
    fun done(): ConcatConfig<T, N> {
        concatAdapter = ConcatAdapter(concatConfigBuilder.build())
        iNekoAdapter = concatAdapter
        configList.forEachIndexed { index, n ->
            concatAdapter.addAdapter(index, n.iNekoAdapter)
        }
        configList[0].apply {
            rv.layoutManager = layoutManager
        }
        return this
    }

    fun show(): ConcatConfig<T, N> {
        configList[0].apply {
            rv.adapter = concatAdapter
        }
        return this
    }

    fun doneAndShow() {
        done()
        show()
    }
}