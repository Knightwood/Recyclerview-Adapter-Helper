package com.kiylx.recyclerviewneko.myadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.myadapter.config.NormalAdapterConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder


/** NekoAdapter */
class NormalAdapter<T : Any>(//配置
    var config: NormalAdapterConfig<T>
) : RecyclerView.Adapter<BaseViewHolder>(), INekoAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        config.createViewHolder(parent, viewType)

    override fun getItemCount(): Int = config.mDatas.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        config.normalBindData(holder, position)
    }

    override fun getItemViewType(position: Int): Int = config.normalGetItemViewType(position)

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        config.runAnim(holder)
    }
}