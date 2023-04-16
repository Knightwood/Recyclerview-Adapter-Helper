package com.kiylx.recyclerviewneko.nekoadapter

import android.content.Context
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.nekoadapter.config.createViewHolder
import com.kiylx.recyclerviewneko.nekoadapter.config.dataSize
import com.kiylx.recyclerviewneko.nekoadapter.config.parseItemViewType
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class NekoPagingAdapter<T : Any>(
    var config: BaseConfig<T>,
    diffCallback: DiffUtil.ItemCallback<T>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingDataAdapter<T, BaseViewHolder>(diffCallback,mainDispatcher, workerDispatcher), INekoAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        config.createViewHolder(parent, viewType)

    override fun getItemCount(): Int = config.dataSize()

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        config.bindData(holder, position)

    override fun getItemViewType(position: Int): Int =config.parseItemViewType(position)

}
/**
 * 提供recyclerview和adapter的配置信息，viewholder创建方法，viewtype判断等
 */
class NekoPagingAdapterConfig<T : Any>(context: Context, rv: RecyclerView) : BaseConfig<T>(context, rv) {
    var nekoPagingAdapter: NekoPagingAdapter<T>? = null

}