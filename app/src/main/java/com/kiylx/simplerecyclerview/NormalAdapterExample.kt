/*
 * NormalAdapterExample.kt
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

import android.app.Application
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.createNormalAdapterConfig
import com.kiylx.recyclerviewneko.myadapter.config.ViewTypeParser
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.viewholder.ItemViewDelegate
import com.kiylx.recyclerviewneko.viewholder.pack
import com.kiylx.simplerecyclerview.databinding.Item1Binding

/** 使用最普通的适配器为例； 1，ItemViewDelegate代理了各种不同的ViewHolder实现 2，快速构建一个适配器 */
class NormalAdapterExample(val application: Application) {

    fun c2() {
        val createConvert: (s: String) -> Unit = {}
        c3(createConvert)
    }

    fun c3(
        block: String.() -> Unit = {},
    ) {
        val s = ""

        s.block()
        block(s)
        block.invoke(s)

        var b: (s: String) -> Unit = {}
        val b2: String.() -> Unit = {}
        b=b2
    }

}