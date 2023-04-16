package com.kiylx.recyclerviewneko.nekoadapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.nekoadapter.config.createViewHolder
import com.kiylx.recyclerviewneko.nekoadapter.config.dataSize
import com.kiylx.recyclerviewneko.nekoadapter.config.parseItemViewType
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder


/**
 * NekoAdapter
 */
class NekoAdapter(//配置
    var config: BaseConfig<out Any>
) : RecyclerView.Adapter<BaseViewHolder>(), INekoAdapter {

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
class NekoAdapterConfig<T : Any>(context: Context, rv: RecyclerView) : BaseConfig<T>(context, rv) {
    var nekoAdapter: NekoAdapter? = null
}