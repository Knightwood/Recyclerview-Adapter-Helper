package com.kiylx.recyclerviewneko.nekoadapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.nekoadapter.config.NekoPagingAdapterConfig
import com.kiylx.recyclerviewneko.nekoadapter.config.createViewHolder
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
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

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (config as NekoPagingAdapterConfig).paging3bindData(holder, position, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return (config as NekoPagingAdapterConfig).paging3ItemViewType(position,getItem(position))
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        config.runAnim(holder)
    }
}
