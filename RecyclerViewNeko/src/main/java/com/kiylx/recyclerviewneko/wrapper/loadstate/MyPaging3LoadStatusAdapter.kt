/*
 * MyPaging3LoadStatusAdapter.kt
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

package com.kiylx.recyclerviewneko.wrapper.loadstate

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

/**
 * 用到的加载状态是paging3库中的，因此有此命名，并不是专为paging3设计。
 * 继承LoadStateAdapter，简化并统一构造LoadStateAdapter的方式
 */
class MyPaging3LoadStatusAdapter(var config: Paging3LoadStatusConfig) :
    LoadStateAdapter<BaseViewHolder>() {

    override fun onBindViewHolder(holder: BaseViewHolder, loadState: LoadState) {
        config.onBindViewHolder(holder, loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): BaseViewHolder {
        return config.onCreateViewHolder(parent, loadState)
    }

    /** 内部自动切换状态 */
    internal fun autoLoadStateChange(newLoadState: LoadState) {
        if (config.autoLoading) {
            loadState = newLoadState
        }
    }

}

/** NekoPagingStatusAdapter中的创建和绑定ViewHolder委托给此类 此类带承担着添加不同的itemview的职责 */
class Paging3LoadStatusConfig() {
    /**
     * 如果值大于0.则在触发[LoadState.Loading]状态后，
     * 延迟[autoClose]毫秒后触发[LoadState.NotLoading]
     */
    var autoClose = -1L

    /** true时自动触发[LoadState.Loading]状态 false时手动切换[LoadState]状态 */
    var autoLoading = false

    private val stateHashMap: HashMap<LoadState, Paging3LoadStatusItemViewWrapper> = hashMapOf()

    //内部用于添加公共状态状态页
    var innerDelegates: Paging3LoadStatusItemViewWrapper? = null

    /**
     * 是否以loadState区分不同的viewHolder 若是使用[addItemDelegate]添加viewholder，则此处为true
     * [setItemDelegate]设置的单个viewholder则不修改此状态
     */
    private var useType = false

    internal fun onBindViewHolder(holder: BaseViewHolder, loadState: LoadState) {
        val itemViewWrapper: Paging3LoadStatusItemViewWrapper? = if (useType) {
            stateHashMap[loadState] ?: innerDelegates
        } else {
            innerDelegates
        }
        Log.d("Paging3LoadStateAdapter", "onBindViewHolder方法被调用")
        itemViewWrapper?.itemViewDelegate?.convert(holder, loadState)
            ?: throw Exception("没有与状态相关的itemview且没有默认itemview")
    }

    /** 如果有添加根loadState关联的视图， */
    internal fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): BaseViewHolder {
        Log.d("Paging3LoadStateAdapter", "onCreateViewHolder方法被调用")
        val itemViewWrapper: Paging3LoadStatusItemViewWrapper? = if (useType) {
            stateHashMap[loadState] ?: innerDelegates
        } else {
            innerDelegates
        }
        itemViewWrapper?.let {
            val view =
                LayoutInflater.from(parent.context).inflate(itemViewWrapper.layoutId, parent, false)
            val holder = BaseViewHolder(parent.context, view)
            return holder
        } ?: throw Exception("没有与状态相关的itemview且没有默认itemview")

    }

    /**
     * 对某种状态添加itemview，若是没有调用[setItemDelegate]，若是缺失与某种状态对应的itemview，会抛出异常
     * 因此，可以仅调用[setItemDelegate]或混合调用，以确保没有与状态对应的itemview时的处理
     * 若重复添加，会替换之前已经添加的itemview
     */
    fun addItemDelegate(
        loadState: LoadState,
        layoutId: Int,
        dataConvert: (holder: BaseViewHolder, loadState: LoadState) -> Unit
    ) {
        useType = true
        stateHashMap[loadState] =
            Paging3LoadStatusItemViewWrapper(
                layoutId, loadState
            ) { h, ls ->
                dataConvert(h, ls)
            }
    }

    /** 添加没有loadState关联的ItemViewDelegate */
    fun setItemDelegate(
        layoutId: Int,
        dataConvert: (holder: BaseViewHolder, loadState: LoadState) -> Unit
    ) {
        innerDelegates =
            Paging3LoadStatusItemViewWrapper(layoutId) { h, ls ->
                dataConvert(h, ls)
            }

    }


}

data class Paging3LoadStatusItemViewWrapper(
    val layoutId: Int,
    val loadState: LoadState? = null,
    val itemViewDelegate: Paging3LoadStatusItemViewDelegate
)

/** 抽象然后取代bindViewHolder */
fun interface Paging3LoadStatusItemViewDelegate {
    fun convert(holder: BaseViewHolder, loadState: LoadState)
}