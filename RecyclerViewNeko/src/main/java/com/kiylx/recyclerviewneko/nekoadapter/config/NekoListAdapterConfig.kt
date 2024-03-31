package com.kiylx.recyclerviewneko.nekoadapter.config

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.nekoadapter.NekoListAdapter

/**
 * 提供recyclerview和adapter的配置信息，viewholder创建方法，viewtype判断等
 */
class NekoListAdapterConfig<T : Any>(context: Context, rv: RecyclerView) : BaseConfig<T>(context, rv) {
   lateinit var nekoListAdapter: NekoListAdapter<T>

    override var mDatas: MutableList<T>
        get() = throw IllegalArgumentException("cannot use mDatas")
        set(value) {
            throw IllegalArgumentException("list adapter cannot use mDatas")
        }

    /**
     * 刷新recyclerview
     * 仅限listadapter类型
     */
    fun submitList(datas: MutableList<T>, commitCallback: Runnable? = null) {
        nekoListAdapter.submitList(datas, commitCallback)
    }

    fun done():NekoListAdapterConfig<T>{
         rv.adapter = nekoListAdapter
        return this
    }
}
