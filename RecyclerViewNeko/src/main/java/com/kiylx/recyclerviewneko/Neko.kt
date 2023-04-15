package com.kiylx.recyclerviewneko

import android.content.Context
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.kiylx.recyclerviewneko.nekoadapter.NekoAdapter
import com.kiylx.recyclerviewneko.nekoadapter.NekoListAdapter
import com.kiylx.recyclerviewneko.nekoadapter.NekoPagingAdapter
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.nekoadapter.config.DefaultConfig
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate
import com.kiylx.recyclerviewneko.nekoadapter.config.ViewTypeParser
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


/**
 * # 根据配置，生成recyclerview
 * ## 一、添加viewholder
 * 1. 单一viewholder：使用[BaseConfig.addItemView]方法
 * 2. 多种类型的viewholder：使用[BaseConfig.addItemViews]方法
 *
 * ## 二、 若有多种viewholder,，指定不同的viewtype方式
 * * 方式一：重写[ItemViewDelegate]中的[ItemViewDelegate.isForViewType]方法
 * * 方式二：实现[ViewTypeParser]
 * * 若使用了方式二,则方式一不起作用。
 *
 */
fun <T : Any> Context.neko(
    recyclerView: RecyclerView,
    configBlock: DefaultConfig<T>.() -> Unit
): BaseConfig<T> {
    val config = DefaultConfig<T>(this, recyclerView)
    config.configBlock()
    val a = NekoAdapter(config)
    config.iNekoAdapter = a
    config.nekoAdapter = a
    return config
}

/**
 * 根据配置，生成NekoListAdapter
 * 若没有指定[asyncConfig]，则用[diffCallback]参数创建NekoListAdapter
 */
fun <T : Any> Context.listNeko(
    recyclerView: RecyclerView,
    asyncConfig: AsyncDifferConfig<T>? = null,
    diffCallback: DiffUtil.ItemCallback<T>,
    configBlock: DefaultConfig<T>.() -> Unit
): DefaultConfig<T> {
    val config = DefaultConfig<T>(this, recyclerView)
    config.configBlock()
    val a = asyncConfig?.let { NekoListAdapter(config, it) }
        ?: NekoListAdapter(config, diffCallback)
    config.iNekoAdapter = a
    config.nekoListAdapter = a
    return config
}
/**
 * 根据配置，生成NekoPagingAdapter
 */
fun <T : Any> Context.paging3Neko(
    recyclerView: RecyclerView,
    diffCallback: DiffUtil.ItemCallback<T>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
    configBlock: DefaultConfig<T>.() -> Unit
): DefaultConfig<T> {
    val config = DefaultConfig<T>(this, recyclerView)
    config.configBlock()
    val a = NekoPagingAdapter(config,diffCallback,mainDispatcher, workerDispatcher)
    config.iNekoAdapter = a
    config.nekoPagingAdapter = a
    return config
}

fun <T : Any> Context.customNeko(
    recyclerView: RecyclerView,
    customAdapter: Adapter<BaseViewHolder>,
    configBlock: DefaultConfig<T>.() -> Unit
): DefaultConfig<T> {
    val config = DefaultConfig<T>(this, recyclerView)
    config.configBlock()
    config.iNekoAdapter = customAdapter
    return config
}

fun <T : Any,N:BaseConfig<T>> N.done(): N {
    rv.adapter = iNekoAdapter
    rv.layoutManager = layoutManager
    return this
}