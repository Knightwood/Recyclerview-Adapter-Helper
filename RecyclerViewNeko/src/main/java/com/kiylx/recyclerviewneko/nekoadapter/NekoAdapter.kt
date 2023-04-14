package com.kiylx.recyclerviewneko.nekoadapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder


/**
 * NekoAdapter
 */
class NekoAdapter(//配置
    var config: BaseConfig<out Any>
) : RecyclerView.Adapter<BaseViewHolder>(), INekoAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemViewDelegate = config.getItemViewDelegate(viewType)
        val layoutId: Int = itemViewDelegate.layoutId
        val holder: BaseViewHolder = config.createHolderInternal(parent, layoutId)
        setClickListener(parent, holder, viewType)
        setLongListener(parent, holder, viewType)
        return holder
    }

    private fun setLongListener(parent: ViewGroup, holder: BaseViewHolder, viewType: Int) {
        if (config.longClickEnable) {
            holder.getConvertView().setOnLongClickListener(View.OnLongClickListener { v ->
                return@OnLongClickListener config.itemLongClickListener?.let {
                    val pos = holder.adapterPosition
                    it.onItemLongClick(v, holder, pos)
                    true
                } ?: false
            })
        }
    }

    private fun setClickListener(parent: ViewGroup, holder: BaseViewHolder, viewType: Int) {
        if (config.clickEnable) {
            holder.getConvertView().setOnClickListener(View.OnClickListener { v ->
                config.itemClickListener?.let {
                    val pos = holder.adapterPosition
                    it.onItemClick(v, holder, pos)
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return config.mDatas.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        config.bindData(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (!config.useItemViewDelegateManager()) {
            super.getItemViewType(position)
        } else {
            config.getItemViewType(position)
        }
    }

}