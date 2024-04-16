/*
 * AlphaInAnimation.kt, 2024/4/16 下午8:50
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
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView

/**
 * An animation to fade item in, changing alpha from default 0f to 1.0f at a uniform rate in default 300ms.
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
class AlphaInAnimation @JvmOverloads constructor(
    private val duration: Long = 300,
    private val mFrom: Float = DEFAULT_ALPHA_FROM
) : ItemAnimator {

    private val interpolator = LinearInterpolator()

    override fun animator(view: View): Animator {
        val animator = ObjectAnimator.ofFloat(view, "alpha", mFrom, 1f)
        animator.duration = duration
        animator.interpolator = interpolator
        return animator
    }

    companion object {
        private const val DEFAULT_ALPHA_FROM = 0f
    }

}