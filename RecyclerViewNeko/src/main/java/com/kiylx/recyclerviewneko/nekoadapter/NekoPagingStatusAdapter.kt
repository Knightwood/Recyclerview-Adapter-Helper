package com.kiylx.recyclerviewneko.nekoadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

class NekoPagingStatusAdapter(var config: PagingStatusConfig) :
    LoadStateAdapter<BaseViewHolder>() {

    override fun onBindViewHolder(holder: BaseViewHolder, loadState: LoadState) {
        config.onBindViewHolder(holder, loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): BaseViewHolder {
        return config.onCreateViewHolder(parent, loadState)
    }

}

/**
 * 承担NekoPagingStatusAdapter中的创建和绑定viewholder
 */
class PagingStatusConfig() {
    private val stateHashMap: HashMap<LoadState, PagingStatusWrapper> = hashMapOf()

    fun onBindViewHolder(holder: BaseViewHolder, loadState: LoadState) {
        stateHashMap[loadState]?.itemViewDelegate?.convert(holder)
    }

    fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): BaseViewHolder {
        stateHashMap[loadState]?.let {
            val view = LayoutInflater.from(parent.context).inflate(it.layoutId, parent, false)
            val holder = BaseViewHolder(parent.context, view)
            return holder
        } ?: throw Exception("没有状态页")
    }

    /**
     * 对某种状态添加itemview
     * 若重复添加，会替换之前已经添加的itemview
     */
    fun addItemDelegate(
        loadState: LoadState,
        layoutId: Int,
        dataConvert: (holder: BaseViewHolder) -> Unit
    ) {
        stateHashMap[loadState] =
            PagingStatusWrapper(
                loadState,
                layoutId
            ) { h ->
                dataConvert(h)
            }

    }

    fun addItemDelegate(pagingStatusWrapper: PagingStatusWrapper) {
        stateHashMap[pagingStatusWrapper.loadState] = pagingStatusWrapper
    }


}

data class PagingStatusWrapper(
    val loadState: LoadState,
    val layoutId: Int,
    val itemViewDelegate: PagingStatusItemViewDelegate
)

fun interface PagingStatusItemViewDelegate {
    fun convert(holder: BaseViewHolder)
}