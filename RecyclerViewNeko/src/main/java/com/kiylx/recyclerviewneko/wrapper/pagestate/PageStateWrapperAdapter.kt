/*
 * PageStateWrapperAdapter.kt
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

package com.kiylx.recyclerviewneko.wrapper.pagestate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.wrapper.pagestate.config.StateWrapperConfig

/**
 * empty，error，loading,content等状态转换
 */
class PageStateWrapperAdapter(private val wrapperConfig: StateWrapperConfig) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return wrapperConfig.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return wrapperConfig.getItemCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        wrapperConfig.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return wrapperConfig.getChildItemViewType(position)
    }

}