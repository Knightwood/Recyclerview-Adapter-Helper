package com.kiylx.recyclerviewneko.wrapper.anim

import android.animation.Animator
import android.view.View

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
interface ItemAnimator {
    fun animator(view: View): Animator
}