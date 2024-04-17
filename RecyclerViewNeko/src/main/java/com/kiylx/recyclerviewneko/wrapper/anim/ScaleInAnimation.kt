/*
 * ScaleInAnimation.kt
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
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator

/**
 * An animation to scale item in, changing item's scaleX and scaleY from default 0.5f to 1.0f in default 300ms.(Using a DecelerateInterpolator with default factor.)
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class ScaleInAnimation @JvmOverloads constructor(
    private val duration: Long = 300,
    private val mFrom: Float = DEFAULT_SCALE_FROM) : ItemAnimator {

    private val interpolator = DecelerateInterpolator()

    override fun animator(view: View): Animator {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f)

        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.duration = duration
        animatorSet.interpolator = interpolator
        animatorSet.play(scaleX).with(scaleY)

        return animatorSet
    }

    companion object {
        private const val DEFAULT_SCALE_FROM = .5f
    }
}