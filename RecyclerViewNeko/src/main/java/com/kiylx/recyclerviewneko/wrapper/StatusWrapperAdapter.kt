package com.kiylx.recyclerviewneko.wrapper

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.nekoadapter.config.BaseConfig
import com.kiylx.recyclerviewneko.nekoadapter.config.IWrapper
import com.kiylx.recyclerviewneko.nekoadapter.config.WrapperConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

/**
 * empty，error，loading,content等状态转换
 */
class StatusWrapperAdapter(private val wrapperConfig: WrapperConfig) :
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
}