/*
 * NormalAdapter.kt
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

package com.kiylx.recyclerviewneko.myadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.myadapter.config.NormalAdapterConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder


/** NekoAdapter */
class NormalAdapter<T : Any>(//配置
    var config: NormalAdapterConfig<T>
) : RecyclerView.Adapter<BaseViewHolder>(), INekoAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        config.createViewHolder(parent, viewType)

    override fun getItemCount(): Int = config.mDatas.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        config.normalBindData(holder, position)
    }

    override fun getItemViewType(position: Int): Int = config.normalGetItemViewType(position)

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        config.runAnim(holder)
    }
}