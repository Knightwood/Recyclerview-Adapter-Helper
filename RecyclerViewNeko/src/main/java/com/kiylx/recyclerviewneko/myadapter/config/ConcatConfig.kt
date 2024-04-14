package com.kiylx.recyclerviewneko.myadapter.config

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.kiylx.recyclerviewneko.wrapper.anim.ItemAnimator

class ConcatConfig<T : Any, N : BaseConfig<T>>(
    val configList: Array<out N>,
) : IConfig() {

    // 1. 定义Config
    private val concatConfigBuilder = ConcatAdapter.Config.Builder()
        .setIsolateViewTypes(true)
        .setStableIdMode(ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS)

    lateinit var concatAdapter: ConcatAdapter

    /** 调整concatConfig */
    fun changeConcatConfig(block: ConcatAdapter.Config.Builder.() -> Unit): ConcatConfig<T, N> {
        concatConfigBuilder.block()
        return this
    }

    /** 给每个子adapter设置动画 */
    fun setAnim(anim: ItemAnimator): ConcatConfig<T, N> {
        configList.forEach {
            it.itemAnimation = anim
        }
        return this
    }
    // 2. 使用ConcatAdapter将Adapter组合起来。
    /** 传入的多个不同[N]，应该设置同样的LayoutManager换rv */
    fun complete(): ConcatConfig<T, N> {
        concatAdapter = ConcatAdapter(concatConfigBuilder.build())
        iRecyclerViewAdapter = concatAdapter
        configList.forEachIndexed { index, n ->
            concatAdapter.addAdapter(index, n.iRecyclerViewAdapter)
        }
        return this
    }

    //3. 给rv设置布局管理器和adapter
    fun done(rv: RecyclerView, layoutManager: LayoutManager): ConcatConfig<T, N> {
        rv.layoutManager = layoutManager
        rv.adapter = concatAdapter
        return this
    }
}