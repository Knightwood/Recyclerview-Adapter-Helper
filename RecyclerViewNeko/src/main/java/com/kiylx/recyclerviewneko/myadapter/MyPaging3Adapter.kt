package com.kiylx.recyclerviewneko.myadapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.kiylx.recyclerviewneko.myadapter.config.Paging3AdapterConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class MyPaging3Adapter<T : Any>(
    var config: Paging3AdapterConfig<T>,
    diffCallback: DiffUtil.ItemCallback<T>,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : PagingDataAdapter<T, BaseViewHolder>(diffCallback, mainDispatcher, workerDispatcher),
    INekoAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        config.createViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        config.paging3bindData(holder, position, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return config.paging3GetItemViewType(position,getItem(position))
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        config.runAnim(holder)
    }
}
