/*
 * Paging3Example.kt
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

package com.kiylx.simplerecyclerview

import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import com.kiylx.recyclerviewneko.createPaging3AdapterConfig
import com.kiylx.recyclerviewneko.myadapter.config.Paging3AdapterConfig
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

class Paging3Example {
    fun test() {
        val config: Paging3AdapterConfig<String> = createPaging3AdapterConfig<String>(object :
            DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }

        }) {
//            addItemView()
            withFooter {
                setItemDelegate(com.kiylx.recyclerviewneko.R.layout.footer_item) { holder: BaseViewHolder, loadState: LoadState ->

                }
            }
            withHeader {

            }
        }
    }
}