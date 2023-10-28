package com.kiylx.recyclerviewneko.wrapper.pagestate.base

class PageStateWrapperView constructor(
    val layoutId: Int,
) {
    var pageStateItemDelegate: PageStateItemDelegate? = null
    lateinit var type: PageStateTypes //当放进SparseArrayCompat保存时绑定

    constructor(layoutId: Int, itemDelegate: PageStateItemDelegate) : this(layoutId) {
        this.pageStateItemDelegate = itemDelegate
    }
}