package com.kiylx.recyclerviewneko.wrapper.pagestate.base

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
 * 如果某[StateTypes]类型的值已存在，则进行替换
 */
operator fun SparseArrayCompat<WrapperView>.set(stateTypes: StateTypes, wrapperView: WrapperView) {
    wrapperView.type=stateTypes
    put(stateTypes.i, wrapperView)
}

operator fun SparseArrayCompat<WrapperView>.get(stateTypes: StateTypes): WrapperView? {
   return get(stateTypes.i)
}