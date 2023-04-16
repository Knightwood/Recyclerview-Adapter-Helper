package com.kiylx.recyclerviewneko.nekoadapter.config

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.nekoadapter.NekoAdapter
import com.kiylx.recyclerviewneko.nekoadapter.NekoListAdapter
import com.kiylx.recyclerviewneko.nekoadapter.NekoPagingAdapter
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate

/**
 * 提供recyclerview和adapter的配置信息，viewholder创建方法，viewtype判断等
 */
open class DefaultConfig<T : Any>(context: Context, rv: RecyclerView) : BaseConfig<T>(context, rv)