package com.kiylx.recyclerviewneko.nekoadapter.config

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.nekoadapter.NekoAdapter

/**
 * 提供recyclerview和adapter的配置信息，viewholder创建方法，viewtype判断等
 */
class NekoAdapterConfig<T : Any>(context: Context, rv: RecyclerView) : BaseConfig<T>(context, rv) {
    lateinit var nekoAdapter: NekoAdapter
}