/*
 * SlideInBottomAnimation.kt, 2024/4/16 下午8:50
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

package com.kiylx.recyclerviewneko.wrapper.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * An animation to let items slide in from the bottom.(Using a DecelerateInterpolator with 1.3 factor.) Default duration is 400ms.
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class SlideInBottomAnimation @JvmOverloads constructor(
    private val duration: Long = 400L,
) : ItemAnimator {

    private val interpolator = DecelerateInterpolator(1.3f)

    override fun animator(view: View): Animator {
        val animator = ObjectAnimator.ofFloat(view, "translationY", view.measuredHeight.toFloat(), 0f)
        animator.duration = duration
        animator.interpolator = interpolator
        return animator
    }
}