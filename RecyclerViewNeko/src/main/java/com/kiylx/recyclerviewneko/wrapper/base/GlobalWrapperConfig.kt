package com.kiylx.recyclerviewneko.wrapper.base

import androidx.collection.SparseArrayCompat

/**
 * 状态页的全局配置
 */
object GlobalWrapperConfig {
    /**
     * 全局的view列表
     */
    val wrappedViewArr: SparseArrayCompat<WrapperView> = SparseArrayCompat()

    init {
        //todo 内置添加默认的状态页
    }

    /**
     * dsl配置状态页
     */
    fun configStateView(block: SparseArrayCompat<WrapperView>.() -> Unit): GlobalWrapperConfig {
        wrappedViewArr.block()
        return this
    }
}

/**
 * 只能添加非[StateTypes.Content]类型的[WrapperView]
 * 如果某[StateTypes]类型的值已存在，则进行替换
 */
operator fun SparseArrayCompat<WrapperView>.set(stateTypes: StateTypes, wrapperView: WrapperView) {
    if (stateTypes == StateTypes.Content) {
        return
    }
    wrapperView.type=stateTypes
    put(stateTypes.i, wrapperView)
}

operator fun SparseArrayCompat<WrapperView>.get(stateTypes: StateTypes): WrapperView? {
   return get(stateTypes.i)
}