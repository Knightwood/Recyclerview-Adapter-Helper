package com.kiylx.recyclerviewneko.wrapper.pagestate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.wrapper.pagestate.config.StateWrapperConfig

/**
 * empty，error，loading,content等状态转换
 */
class PageStateWrapperAdapter(private val wrapperConfig: StateWrapperConfig) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return wrapperConfig.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return wrapperConfig.getItemCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        wrapperConfig.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return wrapperConfig.getChildItemViewType(position)
    }

}