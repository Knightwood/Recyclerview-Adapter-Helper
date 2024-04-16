/*
 * AnimConfig.kt, 2024/4/16 下午8:50
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

package com.kiylx.recyclerviewneko.myadapter.config

import android.animation.Animator
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.wrapper.anim.AlphaInAnimation
import com.kiylx.recyclerviewneko.wrapper.anim.ItemAnimator

class AnimConfig {
    /**
     * Whether enable animation.
     * 是否打开动画
     */
    var animationEnable: Boolean = false

    /**
     * Whether the animation executed only the first time.
     * 动画是否仅第一次执行
     */
    var isAnimationFirstOnly = false

    /**
     * Set custom animation.
     * 设置自定义动画
     */
    var itemAnimation: ItemAnimator? = null
        set(value) {
            animationEnable = true
            field = value
        }

    /**
     * 执行动画
     */
    internal fun runAnim(holder: RecyclerView.ViewHolder) {
        if (animationEnable) {
            if (!isAnimationFirstOnly) {
                val animation: ItemAnimator = itemAnimation ?: AlphaInAnimation()
                animation.animator(holder.itemView).apply {
                    startItemAnimator(this, holder)
                }
            }
        }
    }

    /**
     * 自定义动画开始的配置
     */
    var startAnimBlock: ((anim: Animator, holder: RecyclerView.ViewHolder) -> Unit)? = null

    /**
     * start executing animation
     * override this method to execute more actions
     * 开始执行动画方法
     * 可以配置[startAnimBlock],实行更多行为
     *
     * @param anim
     * @param holder
     */
    private fun startItemAnimator(anim: Animator, holder: RecyclerView.ViewHolder) {
        startAnimBlock?.invoke(anim, holder) ?: anim.start()
    }

}