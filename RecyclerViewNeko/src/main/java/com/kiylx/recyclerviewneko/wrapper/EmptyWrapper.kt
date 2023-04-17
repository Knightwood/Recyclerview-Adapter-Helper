package com.kiylx.recyclerviewneko.wrapper

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kiylx.recyclerviewneko.utils.WrapperUtils
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

/**
 * Created by zhy on 16/6/23.
 */
class EmptyWrapper(private val mInnerAdapter: RecyclerView.Adapter<BaseViewHolder>) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private var mEmptyView: View? = null
    private var mEmptyLayoutId = 0
    private val isEmpty: Boolean
        get() = (mEmptyView != null || mEmptyLayoutId != 0) && mInnerAdapter.itemCount == 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (isEmpty) {
            val holder: BaseViewHolder = if (mEmptyView != null) {
                BaseViewHolder.createViewHolder(parent.context, mEmptyView!!)
            } else {
                BaseViewHolder.createViewHolder(parent.context, parent, mEmptyLayoutId)
            }
            return holder
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        WrapperUtils.onAttachedToRecyclerView(
            mInnerAdapter,
            recyclerView
        ) { layoutManager, oldLookup, position ->
            if (isEmpty) {
                layoutManager.spanCount
            } else oldLookup.getSpanSize(position) ?: 1
        }
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        mInnerAdapter.onViewAttachedToWindow(holder)
        if (isEmpty) {
            WrapperUtils.setFullSpan(holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isEmpty) {
            ITEM_TYPE_EMPTY
        } else mInnerAdapter.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (isEmpty) {
            return
        }
        mInnerAdapter.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return if (isEmpty) 1 else mInnerAdapter.itemCount
    }

    fun setEmptyView(emptyView: View) {
        mEmptyView = emptyView
    }

    fun setEmptyView(layoutId: Int) {
        mEmptyLayoutId = layoutId
    }

    companion object {
        const val ITEM_TYPE_EMPTY = Int.MAX_VALUE - 1
    }
}