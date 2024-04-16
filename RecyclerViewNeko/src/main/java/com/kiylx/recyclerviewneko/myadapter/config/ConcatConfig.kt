/*
 * ConcatConfig.kt, 2024/4/16 下午8:50
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
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.kiylx.recyclerviewneko.wrapper.anim.ItemAnimator

class ConcatConfig<T : Any, N : BaseConfig<T>>(
    val configList: Array<out N>,
) : IConfig() {

    // 1. 定义Config
    private val concatConfigBuilder = ConcatAdapter.Config.Builder()
        .setIsolateViewTypes(true)
        .setStableIdMode(ConcatAdapter.Config.StableIdMode.NO_STABLE_IDS)

    lateinit var concatAdapter: ConcatAdapter

    /** 调整concatConfig */
    fun changeConcatConfig(block: ConcatAdapter.Config.Builder.() -> Unit): ConcatConfig<T, N> {
        concatConfigBuilder.block()
        return this
    }

    /** 给每个子adapter设置动画 */
    fun setAnim(anim: ItemAnimator): ConcatConfig<T, N> {
        configList.forEach {
            it.itemAnimation = anim
        }
        return this
    }
    // 2. 使用ConcatAdapter将Adapter组合起来。
    /** 传入的多个不同[N]，应该设置同样的LayoutManager换rv */
    fun complete(): ConcatConfig<T, N> {
        concatAdapter = ConcatAdapter(concatConfigBuilder.build())
        iRecyclerViewAdapter = concatAdapter
        configList.forEachIndexed { index, n ->
            concatAdapter.addAdapter(index, n.iRecyclerViewAdapter)
        }
        return this
    }

    //3. 给rv设置布局管理器和adapter
    fun done(rv: RecyclerView, layoutManager: LayoutManager): ConcatConfig<T, N> {
        rv.layoutManager = layoutManager
        rv.adapter = concatAdapter
        return this
    }
}