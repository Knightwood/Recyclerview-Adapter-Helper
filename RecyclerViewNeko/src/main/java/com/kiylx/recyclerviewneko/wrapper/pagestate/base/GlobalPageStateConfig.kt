/*
 * GlobalPageStateConfig.kt, 2024/4/16 下午8:50
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

package com.kiylx.recyclerviewneko.wrapper.pagestate.base

import androidx.collection.SparseArrayCompat

/**
 * 状态页的全局配置
 */
object GlobalWrapperConfig {
    /**
     * 全局的view列表
     */
    val wrappedViewArr: SparseArrayCompat<PageStateWrapperView> = SparseArrayCompat()

    init {
        //todo 内置添加默认的状态页
    }

    /**
     * dsl配置状态页
     */
    fun configStateView(block: SparseArrayCompat<PageStateWrapperView>.() -> Unit): GlobalWrapperConfig {
        wrappedViewArr.block()
        return this
    }
}

/**
 * 如果某[PageStateTypes]类型的值已存在，则进行替换
 */
operator fun SparseArrayCompat<PageStateWrapperView>.set(pageStateTypes: PageStateTypes, wrapperView: PageStateWrapperView) {
    wrapperView.type=pageStateTypes
    put(pageStateTypes.i, wrapperView)
}

operator fun SparseArrayCompat<PageStateWrapperView>.get(pageStateTypes: PageStateTypes): PageStateWrapperView? {
   return get(pageStateTypes.i)
}