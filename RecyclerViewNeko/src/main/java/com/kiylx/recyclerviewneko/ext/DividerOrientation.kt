/*
 * DividerOrientation.kt
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

package com.kiylx.recyclerviewneko.ext

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView

enum class DividerOrientation {
    VERTICAL, HORIZONTAL, GRID
}

/**
 * 函数配置分割线
 * 具体配置参数查看[DefaultDecoration]
 */
fun RecyclerView.divider(
    block: DefaultDecoration.() -> Unit
): RecyclerView {
    val itemDecoration = DefaultDecoration(context).apply(block)
    addItemDecoration(itemDecoration)
    return this
}

/**
 * 指定Drawable资源为分割线, 分割线的间距和宽度应在资源文件中配置
 * @param drawable 描述分割线的drawable
 * @param orientation 分割线方向, 仅[androidx.recyclerview.widget.GridLayoutManager]需要使用此参数, 其他LayoutManager都是根据其方向自动推断
 */
fun RecyclerView.divider(
    @DrawableRes drawable: Int,
    orientation: DividerOrientation = DividerOrientation.HORIZONTAL
): RecyclerView {
    return divider {
        setDrawable(drawable)
        this.orientation = orientation
    }
}

/**
 * 设置空白间距分割
 * @param space item的空白间距
 * @param orientation 分割线方向, 仅[androidx.recyclerview.widget.GridLayoutManager]需要使用此参数, 其他LayoutManager都是根据其方向自动推断
 */
fun RecyclerView.dividerSpace(
    space: Int,
    orientation: DividerOrientation = DividerOrientation.HORIZONTAL,
): RecyclerView {
    return divider {
        setDivider(space)
        this.orientation = orientation
    }
}