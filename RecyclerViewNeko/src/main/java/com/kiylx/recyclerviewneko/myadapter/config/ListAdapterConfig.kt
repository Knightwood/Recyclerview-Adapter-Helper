package com.kiylx.recyclerviewneko.myadapter.config

import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.myadapter.Lm.linear
import com.kiylx.recyclerviewneko.myadapter.MyListAdapter

/** 提供recyclerview和adapter的配置信息，viewholder创建方法，viewtype判断等 */
class ListAdapterConfig<T : Any> : BaseConfig<T>() {
    lateinit var myListAdapter: MyListAdapter<T>

    /** 刷新recyclerview 仅限listadapter类型 */
    fun submitList(datas: MutableList<T>, commitCallback: Runnable? = null) {
        myListAdapter.submitList(datas, commitCallback)
    }

    fun done(rv: RecyclerView) {
        rv.adapter = myListAdapter
        rv.layoutManager = layoutManager ?: rv.context.linear()
    }

    fun refreshData(datas: MutableList<T>) {
        myListAdapter.notifyDataSetChanged()
    }
}