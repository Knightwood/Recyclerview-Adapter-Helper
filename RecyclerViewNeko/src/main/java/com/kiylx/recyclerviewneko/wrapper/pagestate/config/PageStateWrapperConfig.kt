/*
 * PageStateWrapperConfig.kt, 2024/4/16 下午8:50
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

package com.kiylx.recyclerviewneko.wrapper.pagestate.config

import android.content.Context
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kiylx.recyclerviewneko.myadapter.config.IConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.wrapper.pagestate.PageStateWrapperAdapter
import com.kiylx.recyclerviewneko.wrapper.pagestate.base.GlobalWrapperConfig
import com.kiylx.recyclerviewneko.wrapper.pagestate.base.PageStateItemDelegate
import com.kiylx.recyclerviewneko.wrapper.pagestate.base.PageStateTypes
import com.kiylx.recyclerviewneko.wrapper.pagestate.base.PageStateWrapperView
import com.kiylx.recyclerviewneko.wrapper.pagestate.base.get
import com.kiylx.recyclerviewneko.wrapper.pagestate.base.set

/** 在aplication中配置全局设置 */
fun test() {
    GlobalWrapperConfig.configStateView {
        this[PageStateTypes.Empty] = PageStateWrapperView(android.R.id.edit) {

        }
        this[PageStateTypes.Error] = PageStateWrapperView(android.R.id.edit) {

        }
    }
}

/** 决定有多少种可使用的状态方法 */
interface IWrapper {

    fun setEmpty(layoutId: Int, block: PageStateItemDelegate): IWrapper
    fun setLoading(layoutId: Int, block: PageStateItemDelegate): IWrapper
    fun setError(layoutId: Int, block: PageStateItemDelegate): IWrapper

    fun showLoading(): IWrapper
    fun showEmpty(): IWrapper
    fun showContent(): IWrapper
    fun showError(): IWrapper
    fun showStatePage(layoutManager: LayoutManager?=null): IWrapper
}

class StateWrapperConfig private constructor(val context: Context) : IWrapper {

    private val statusViewArr: SparseArrayCompat<PageStateWrapperView> = SparseArrayCompat()

    init {
        //添加全局设置的默认值
        val viewSparseArray = GlobalWrapperConfig.wrappedViewArr
        for (i in viewSparseArray.size() - 1 downTo 0) {
            val wrapperView = viewSparseArray.valueAt(i)
            statusViewArr[wrapperView.type] = wrapperView
        }
    }


    lateinit var recyclerView: RecyclerView

    /** 包装[beWrappedAdapter]的adapter */
    lateinit var stateWrapperAdapter: PageStateWrapperAdapter

    /** 被包装起来的adapter */
    lateinit var beWrappedAdapter: Adapter<ViewHolder>

    /** 根据此数据，生成不同的状态页 */
    internal var mDatas: MutableList<PageStateTypes> = mutableListOf(PageStateTypes.Empty)

    override fun setEmpty(layoutId: Int, block: PageStateItemDelegate): IWrapper {
        statusViewArr[PageStateTypes.Empty] = PageStateWrapperView(layoutId, block)
        return this
    }

    override fun setLoading(layoutId: Int, block: PageStateItemDelegate): IWrapper {
        statusViewArr[PageStateTypes.Loading] = PageStateWrapperView(layoutId, block)
        return this
    }

    override fun setError(layoutId: Int, block: PageStateItemDelegate): IWrapper {
        statusViewArr[PageStateTypes.Error] = PageStateWrapperView(layoutId, block)
        return this
    }

    internal fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return BaseViewHolder.createViewHolder(
            parent.context, parent,
            statusViewArr[viewType]!!.layoutId
        )
    }

    internal fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val type = mDatas[0]

        val data = statusViewArr[type]
        data?.pageStateItemDelegate?.convert(holder.itemView)
            ?: throw Exception("找不到view")
    }

    internal fun getItemCount(): Int {
        return 1
    }

    companion object {
        operator fun invoke(config: IConfig, rv: RecyclerView): StateWrapperConfig {
            return StateWrapperConfig(rv.context).apply {
                beWrappedAdapter = config.iRecyclerViewAdapter as Adapter<ViewHolder>
                recyclerView = rv
            }
        }

        operator fun invoke(
            contentAdapter: Adapter<ViewHolder>,
            context: Context,
            rv: RecyclerView
        ): StateWrapperConfig {
            return StateWrapperConfig(context).apply {
                beWrappedAdapter = contentAdapter
                recyclerView = rv
            }
        }
    }


    override fun showLoading(): IWrapper {
        recyclerView.adapter = stateWrapperAdapter
        mDatas = mutableListOf(PageStateTypes.Loading)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    override fun showEmpty(): IWrapper {
        recyclerView.adapter = stateWrapperAdapter
        mDatas = mutableListOf(PageStateTypes.Empty)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    override fun showContent(): IWrapper {
        recyclerView.adapter = beWrappedAdapter
        return this
    }

    override fun showError(): IWrapper {
        recyclerView.adapter = stateWrapperAdapter
        mDatas = mutableListOf(PageStateTypes.Error)
        stateWrapperAdapter.notifyItemChanged(0)
        return this
    }

    internal fun getChildItemViewType(position: Int): Int {
        return mDatas[0].i
    }

    override fun showStatePage(layoutManager: LayoutManager?): IWrapper {
        recyclerView.adapter = stateWrapperAdapter
        layoutManager?.let {
            recyclerView.layoutManager=it
        }
        return this
    }
}