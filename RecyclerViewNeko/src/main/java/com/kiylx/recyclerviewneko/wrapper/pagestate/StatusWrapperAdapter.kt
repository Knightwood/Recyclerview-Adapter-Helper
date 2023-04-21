package com.kiylx.recyclerviewneko.wrapper.pagestate

import android.animation.Animator
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kiylx.recyclerviewneko.wrapper.pagestate.config.StateWrapperConfig
import com.kiylx.recyclerviewneko.utils.WrapperUtils
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder
import com.kiylx.recyclerviewneko.wrapper.anim.AlphaInAnimation
import com.kiylx.recyclerviewneko.wrapper.anim.ItemAnimator
import com.kiylx.recyclerviewneko.wrapper.pagestate.base.StateTypes

/**
 * empty，error，loading,content等状态转换
 * 以及给内容添加动画
 */
class StatusWrapperAdapter(private val wrapperConfig: StateWrapperConfig) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return wrapperConfig.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        return wrapperConfig.getItemCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        wrapperConfig.onBindViewHolder(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return wrapperConfig.getChildItemViewType(position)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        if (wrapperConfig.mDatas[0] != StateTypes.Content) {
            super.onViewAttachedToWindow(holder)
            WrapperUtils.setFullSpan(holder)
        } else {
            wrapperConfig.beWrappedAdapter.onViewAttachedToWindow(holder)
            runAnim(holder)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        WrapperUtils.onAttachedToRecyclerView(
            wrapperConfig.beWrappedAdapter,
            recyclerView
        ) { layoutManager, oldLookup, position ->
            if (wrapperConfig.mDatas[0] != StateTypes.Content) {
                layoutManager.spanCount
            } else oldLookup.getSpanSize(position) ?: 1
        }
    }

    fun runAnim(holder: ViewHolder) {
        if (wrapperConfig.animationEnable) {
            if (!wrapperConfig.isAnimationFirstOnly) {
                val animation: ItemAnimator = wrapperConfig.itemAnimation ?: AlphaInAnimation()
                animation.animator(holder.itemView).apply {
                    startItemAnimator(this, holder)
                }
            }
        }
    }

    /**
     * start executing animation
     * override this method to execute more actions
     * 开始执行动画方法
     * 可以重写此方法，实行更多行为
     *
     * @param anim
     * @param holder
     */
    fun startItemAnimator(anim: Animator, holder: RecyclerView.ViewHolder) {
        anim.start()
    }


}