package com.kiylx.recyclerviewneko.nekoadapter.config

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