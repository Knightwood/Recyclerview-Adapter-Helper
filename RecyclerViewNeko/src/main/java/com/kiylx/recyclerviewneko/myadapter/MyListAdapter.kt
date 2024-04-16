/*
 * MyListAdapter.kt, 2024/4/16 下午8:50
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
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kiylx.recyclerviewneko.myadapter.config.ListAdapterConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

class MyListAdapter<T : Any> : ListAdapter<T, BaseViewHolder>, INekoAdapter {
    var config: ListAdapterConfig<T>

    constructor(
        config: ListAdapterConfig<T>,
        diffCallback: DiffUtil.ItemCallback<T>
    ) : super(diffCallback) {
        this.config = config
    }

    constructor(config: ListAdapterConfig<T>, asyncConfig: AsyncDifferConfig<T>) : super(asyncConfig) {
        this.config = config
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return config.createViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val data = getItem(position)
        config.bindData(holder,position, data)
    }

    override fun getItemViewType(position: Int): Int {
        val data = getItem(position)
        return config.getItemViewType(position, data)
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        config.runAnim(holder)
    }
}