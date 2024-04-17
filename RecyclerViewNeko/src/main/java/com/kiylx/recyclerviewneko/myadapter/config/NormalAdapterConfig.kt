/*
 * NormalAdapterConfig.kt
 *
 * Copyright [2023-2024] [KnightWood]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.kiylx.recyclerviewneko.myadapter.config

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.myadapter.Lm.linear
import com.kiylx.recyclerviewneko.myadapter.NormalAdapter
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

/** 提供recyclerview和adapter的配置信息，viewholder创建方法，viewtype判断等 */
class NormalAdapterConfig<T : Any> :
    BaseConfig<T>() {
    lateinit var normalAdapter: NormalAdapter<T>

    var mDatas: MutableList<T> = mutableListOf()

    /** 判断itemview的类型 */
    internal fun normalGetItemViewType(position: Int): Int {
        return getItemViewType(
            position,
            mDatas[position],
        )
    }

    /** 将数据绑定到viewholder */
    internal fun normalBindData(holder: BaseViewHolder, position: Int) {
        val data = mDatas[position]
        bindData(holder, position, data)
    }

    /** 刷新recyclerview */
    @SuppressLint("NotifyDataSetChanged")
    open fun refreshData(datas: MutableList<T>) {
        mDatas.clear()
        mDatas.addAll(datas)
        iRecyclerViewAdapter.notifyDataSetChanged()
    }

    /** 移除某个位置的item，并删除对应的数据 */
    fun removeData(pos: Int) {
        val i = mDatas.size - pos
        normalAdapter.notifyItemRemoved(pos)
        normalAdapter.notifyItemRangeChanged(pos, i)//刷新pos之后item的position
        mDatas.removeAt(pos)
    }

    /**
     * 移动item和数据位置
     *
     * @param source
     * @param target
     */
    fun moveData(source:Int,target:Int){
        val old = mDatas[source]
        mDatas[source] = mDatas[target]
        mDatas[target] = old
        normalAdapter.notifyItemMoved(source, target)
    }

    fun removeRangeData(pos: Int, endPos: Int) {
        val i = mDatas.size - endPos
        normalAdapter.notifyItemRangeRemoved(pos, endPos)
        normalAdapter.notifyItemRangeChanged(endPos, i)//刷新pos之后item的position
        for (index in endPos downTo pos) {
            mDatas.removeAt(index)
        }
    }


    /** 普通adapter 完成rv的创建 */
    fun done(rv: RecyclerView, datas: List<T> = emptyList()): NormalAdapterConfig<T> {
        if (datas.isNotEmpty()) {
            mDatas.clear()
            mDatas.addAll(datas)
        }
        rv.layoutManager = layoutManager ?: rv.context.linear()
        rv.adapter = iRecyclerViewAdapter
        return this
    }

    fun changeDatas(datas: List<T> = emptyList()): NormalAdapterConfig<T> {
        mDatas.clear()
        mDatas.addAll(datas)
        return this
    }

}