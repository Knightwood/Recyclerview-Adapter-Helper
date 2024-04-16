package com.kiylx.recyclerviewneko.myadapter.config

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.myadapter.Lm.linear
import com.kiylx.recyclerviewneko.myadapter.MyListAdapter

/** 提供recyclerview和adapter的配置信息，viewholder创建方法，viewtype判断等 */
class ListAdapterConfig<T : Any> : BaseConfig<T>() {
    lateinit var myListAdapter: MyListAdapter<T>
    val TAG = "ListAdapterConfig"

    /** 刷新recyclerview 仅限listadapter类型 */
    fun submitList(datas: MutableList<T>, commitCallback: Runnable? = null) {
        myListAdapter.submitList(datas, commitCallback)
    }

    fun done(rv: RecyclerView): ListAdapterConfig<T> {
        rv.adapter = myListAdapter
        rv.layoutManager = layoutManager ?: rv.context.linear()
        return this
    }

    fun moveData(source: Int, target: Int) {
        val list = myListAdapter.currentList.toMutableList()
//                Log.d(TAG, "drag: $source X $target")
        if (source != target && source > 0 && target > 0) {
            val old = list[source]
            list.removeAt(source)
            list.add(target, old)
            val f = mutableListOf<T>()
            f.addAll(list)
            myListAdapter.submitList(f) {
                myListAdapter.notifyItemMoved(target, source)
//                myListAdapter.currentList.forEachIndexed { index, t ->
//                    Log.d(TAG, "drag data: $t")
//                }
            }
        }
    }

    fun removeData(target: Int) {
        val list: MutableList<T> = mutableListOf()
        list.addAll(myListAdapter.currentList)
        val i = list.size - target
        list.removeAt(target)
//                Log.d(TAG, "slide: $target")
        myListAdapter.submitList(list) {
            myListAdapter.notifyItemRemoved(target)
            myListAdapter.notifyItemRangeChanged(target, i)//刷新pos之后item的position
//        myListAdapter.currentList.forEachIndexed { index, t ->
//                Log.d(TAG, "slide data: $t")
//            }
        }
    }

    fun refreshData(datas: MutableList<T>) {
        val d = mutableListOf<T>()
        d.addAll(datas)
        myListAdapter.submitList(d)
    }
}