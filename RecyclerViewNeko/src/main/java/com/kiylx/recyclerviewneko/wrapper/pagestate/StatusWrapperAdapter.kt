package com.kiylx.recyclerviewneko.wrapper.pagestate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.wrapper.pagestate.config.StateWrapperConfig
import com.kiylx.recyclerviewneko.utils.WrapperUtils
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.wrapper.pagestate.base.StateTypes

/**
 * empty，error，loading,content等状态转换
 */
class StatusWrapperAdapter(private val wrapperConfig: StateWrapperConfig) :
    RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return wrapperConfig.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return wrapperConfig.getItemCount()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        wrapperConfig.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return wrapperConfig.getItemViewType(position)
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        WrapperUtils.onAttachedToRecyclerView(
            wrapperConfig.beWrappedAdapter,
            recyclerView
        ) { layoutManager, oldLookup, position ->
            if (wrapperConfig.mDatas[0] != StateTypes.Content) {
                layoutManager.spanCount
            } else oldLookup.getSpanSize(position) ?: 1
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        if (wrapperConfig.mDatas[0] != StateTypes.Content) {
            super.onViewAttachedToWindow(holder)
            WrapperUtils.setFullSpan(holder)
        } else {
            wrapperConfig.beWrappedAdapter.onViewAttachedToWindow(holder)
        }
    }
}