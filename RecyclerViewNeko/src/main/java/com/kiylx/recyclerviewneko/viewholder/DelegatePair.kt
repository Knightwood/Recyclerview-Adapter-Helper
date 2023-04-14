package com.kiylx.recyclerviewneko.viewholder

data class DelegatePair<T>(val type: Int, val delegate: ItemViewDelegate<T>)

infix fun <T : Any> Int.pack(that: ItemViewDelegate<T>): DelegatePair<T> = DelegatePair(this, that)
