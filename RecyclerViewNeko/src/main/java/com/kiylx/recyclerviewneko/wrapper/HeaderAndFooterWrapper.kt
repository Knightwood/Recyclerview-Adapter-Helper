package com.kiylx.recyclerviewneko.wrapper

import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kiylx.recyclerviewneko.utils.WrapperUtils
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

/**
 * Created by zhy on 16/6/23.
 */
class HeaderAndFooterWrapper<T>(private val mInnerAdapter: RecyclerView.Adapter<ViewHolder>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mHeaderViews = SparseArrayCompat<View?>()
    private val mFootViews = SparseArrayCompat<View?>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (mHeaderViews[viewType] != null) {
            return BaseViewHolder.createViewHolder(parent.context, mHeaderViews[viewType]!!)
        } else if (mFootViews[viewType] != null) {
            return BaseViewHolder.createViewHolder(parent.context, mFootViews[viewType]!!)
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - headersCount - realItemCount)
        }
        return mInnerAdapter.getItemViewType(position - headersCount)
    }

    private val realItemCount: Int
        private get() = mInnerAdapter.itemCount

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeaderViewPos(position)) {
            return
        }
        if (isFooterViewPos(position)) {
            return
        }
        mInnerAdapter.onBindViewHolder(holder, position - headersCount)
    }

    override fun getItemCount(): Int {
        return headersCount + footersCount + realItemCount
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        WrapperUtils.onAttachedToRecyclerView(
            mInnerAdapter,
            recyclerView,
            WrapperUtils.SpanSizeCallback { layoutManager, oldLookup, position ->
                val viewType = getItemViewType(position)
                if (mHeaderViews[viewType] != null) {
                    return@SpanSizeCallback layoutManager.spanCount
                } else if (mFootViews[viewType] != null) {
                    return@SpanSizeCallback layoutManager.spanCount
                }
                oldLookup?.getSpanSize(position) ?: 1
            })
    }

    fun onViewAttachedToWindow(holder: BaseViewHolder) {
        mInnerAdapter.onViewAttachedToWindow(holder)
        val position = holder.layoutPosition
        if (isHeaderViewPos(position) || isFooterViewPos(position)) {
            WrapperUtils.setFullSpan(holder)
        }
    }

    private fun isHeaderViewPos(position: Int): Boolean {
        return position < headersCount
    }

    private fun isFooterViewPos(position: Int): Boolean {
        return position >= headersCount + realItemCount
    }

    fun addHeaderView(view: View?) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view)
    }

    fun addFootView(view: View?) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view)
    }

    val headersCount: Int
        get() = mHeaderViews.size()
    val footersCount: Int
        get() = mFootViews.size()

    companion object {
        private const val BASE_ITEM_TYPE_HEADER = 100000
        private const val BASE_ITEM_TYPE_FOOTER = 200000
    }
}