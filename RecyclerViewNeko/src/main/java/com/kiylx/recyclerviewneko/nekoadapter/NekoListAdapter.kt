package com.kiylx.recyclerviewneko.nekoadapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

class NekoListAdapter<T : Any> : ListAdapter<T, BaseViewHolder>, INekoAdapter {
    var config: BaseConfig<T>

    constructor(
        config: BaseConfig<T>,
        diffCallback: DiffUtil.ItemCallback<T>
    ) : super(diffCallback) {
        this.config = config
    }

    constructor(config: BaseConfig<T>, asyncConfig: AsyncDifferConfig<T>) : super(asyncConfig) {
        this.config = config
    }

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