package com.kiylx.recyclerviewneko.nekoadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.nekoadapter.config.createViewHolder
import com.kiylx.recyclerviewneko.nekoadapter.config.parseItemViewType
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
        return config.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val data = getItem(position)
        config.bindData(holder,position, data)
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItem(position)
        return config.parseItemViewType(position, data)
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        config.runAnim(holder)
    }
}