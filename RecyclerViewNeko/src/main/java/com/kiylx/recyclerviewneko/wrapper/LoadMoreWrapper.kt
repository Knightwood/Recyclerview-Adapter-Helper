package com.kiylx.recyclerviewneko.wrapper

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kiylx.recyclerviewneko.utils.SpanSizeCallback
import com.kiylx.recyclerviewneko.utils.WrapperUtils
import com.kiylx.recyclerviewneko.viewholder.BaseViewHolder

/**
 * Created by zhy on 16/6/23.
 */
class LoadMoreWrapper(private val mInnerAdapter: RecyclerView.Adapter<BaseViewHolder>) :
    RecyclerView.Adapter<BaseViewHolder>() {
    private var mLoadMoreView: View? = null
    private var mLoadMoreLayoutId = 0
    private fun hasLoadMore(): Boolean {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0
    }

    private fun isShowLoadMore(position: Int): Boolean {
        return hasLoadMore() && position >= mInnerAdapter.itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return if (isShowLoadMore(position)) {
            ITEM_TYPE_LOAD_MORE
        } else mInnerAdapter.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            val holder: BaseViewHolder
            holder = if (mLoadMoreView != null) {
                BaseViewHolder.createViewHolder(parent.context, mLoadMoreView!!)
            } else {
                BaseViewHolder.createViewHolder(parent.context, parent, mLoadMoreLayoutId)
            }
            return holder
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (isShowLoadMore(position)) {
            if (mOnLoadMoreListener != null) {
                mOnLoadMoreListener!!.onLoadMoreRequested()
            }
            return
        }
        mInnerAdapter.onBindViewHolder(holder, position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        WrapperUtils.onAttachedToRecyclerView(
            mInnerAdapter,
            recyclerView,
            object :SpanSizeCallback{
                override fun getSpanSize(
                    layoutManager: GridLayoutManager,
                    oldLookup: GridLayoutManager.SpanSizeLookup,
                    position: Int
                ): Int {
                    if (isShowLoadMore(position)) {
                        return layoutManager.spanCount
                    }
                   return oldLookup.getSpanSize(position) ?: 1
                }
            }
         )
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        mInnerAdapter.onViewAttachedToWindow(holder)
        if (isShowLoadMore(holder.layoutPosition)) {
            setFullSpan(holder)
        }
    }

    private fun setFullSpan(holder: BaseViewHolder) {
        val lp = holder.itemView.layoutParams
        if (lp != null
            && lp is StaggeredGridLayoutManager.LayoutParams
        ) {
            lp.isFullSpan = true
        }
    }

    override fun getItemCount(): Int {
        return mInnerAdapter.itemCount + if (hasLoadMore()) 1 else 0
    }

    interface OnLoadMoreListener {
        fun onLoadMoreRequested()
    }

    private var mOnLoadMoreListener: OnLoadMoreListener? = null
    fun setOnLoadMoreListener(loadMoreListener: OnLoadMoreListener?): LoadMoreWrapper {
        if (loadMoreListener != null) {
            mOnLoadMoreListener = loadMoreListener
        }
        return this
    }

    fun setLoadMoreView(loadMoreView: View): LoadMoreWrapper {
        mLoadMoreView = loadMoreView
        return this
    }

    fun setLoadMoreView(layoutId: Int): LoadMoreWrapper {
        mLoadMoreLayoutId = layoutId
        return this
    }

    companion object {
        const val ITEM_TYPE_LOAD_MORE = Int.MAX_VALUE - 2
    }
}