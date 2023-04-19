package com.kiylx.recyclerviewneko.wrapper.base

class WrapperView constructor(
    val layoutId: Int,
) {
    var statePageViewHolder: StatePageViewHolder? = null
    lateinit var type: StateTypes //当放进SparseArrayCompat保存时绑定

    constructor(layoutId: Int, viewListener: StatePageViewHolder) : this(layoutId) {
        this.statePageViewHolder = viewListener
    }
}