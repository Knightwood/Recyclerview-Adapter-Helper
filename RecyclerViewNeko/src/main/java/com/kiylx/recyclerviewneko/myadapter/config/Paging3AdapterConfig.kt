/*
 * Paging3AdapterConfig.kt
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

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.myadapter.Lm.linear
import com.kiylx.recyclerviewneko.myadapter.MyPaging3Adapter
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.wrapper.loadstate.MyPaging3LoadStatusAdapter
import com.kiylx.recyclerviewneko.wrapper.loadstate.Paging3LoadStatusConfig

/** 提供recyclerview和adapter的配置信息，viewholder创建方法，viewtype判断等 datas不可用 */
class Paging3AdapterConfig<T : Any> :
    BaseConfig<T>() {

    //如果给pagingDataAdapter添加header和footer，则这里不为null
    var header: MyPaging3LoadStatusAdapter? = null
    var footer: MyPaging3LoadStatusAdapter? = null

    /** 当有header或footer时，这里不为null，且应该使用此adapter设置给recyclerview */
    var concatAdapter: ConcatAdapter? = null
    lateinit var myPaging3Adapter: MyPaging3Adapter<T>


    //<editor-fold desc="给PagingAdapter添加header和footer">
    /** 给pagingAdapter添加状态加载状态item */
    inline fun withHeader(block: Paging3LoadStatusConfig.() -> Unit): Paging3AdapterConfig<T> {
        val tmp = Paging3LoadStatusConfig()
        tmp.block()
        val header = MyPaging3LoadStatusAdapter(tmp)
        this.header = header
        return this
    }

    /** 给pagingAdapter添加状态加载状态item */
    inline fun withFooter(block: Paging3LoadStatusConfig.() -> Unit): Paging3AdapterConfig<T> {
        val tmp = Paging3LoadStatusConfig()
        tmp.block()
        val footer = MyPaging3LoadStatusAdapter(tmp)
        this.footer = footer
        return this
    }
//</editor-fold>


    fun complete(): Paging3AdapterConfig<T> {
        header?.let { it1 ->
            footer?.let { it2 ->
                concatAdapter = myPaging3Adapter.withLoadStateHeaderAndFooter(it1, it2)
            } ?: let {
                concatAdapter = myPaging3Adapter.withLoadStateHeader(it1)
            }
        } ?: let {
            footer?.let { it2 ->
                concatAdapter = myPaging3Adapter.withLoadStateFooter(it2)
            }
        }
        return this
    }

    /** 有header或footer时，给pagingAdapter添加上header或footer， 然后设置给recyclerview */
    fun done(rv: RecyclerView): Paging3AdapterConfig<T> {
        rv.adapter = concatAdapter ?: myPaging3Adapter
        rv.layoutManager = layoutManager?: rv.context.linear()
        return this
    }


    //对placeHolder的处理
    internal var placeholderViewType: Int = -1
    internal var placeHolderBind: ((viewHolder: BaseViewHolder) -> Unit)? = null

    /**
     * Config place holder view type
     *
     * 配置placeholder
     *
     * @param viewType
     * @param onBind
     * @receiver
     */
    fun configPlaceHolderViewType(viewType: Int, onBind: (viewHolder: BaseViewHolder) -> Unit) {
        this.placeholderViewType = viewType
        this.placeHolderBind = onBind
    }

    internal fun paging3GetItemViewType(pos: Int, data: T?): Int {
        return data?.let {
            getItemViewType(pos, it)
        } ?: placeholderViewType
    }

    fun refreshData(datas: MutableList<T>) {
        myPaging3Adapter.refresh()
    }

    internal fun paging3bindData(holder: BaseViewHolder, position: Int, data: T?) {
        if (data != null) {
            bindData(holder, position, data)
        } else {
            placeHolderBind?.invoke(holder)
        }
    }
}