package com.kiylx.recyclerviewneko.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.util.Linkify
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder(private var mContext: Context, itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private var mViews: SparseArray<View> = SparseArray()
    private var mConvertView: View = itemView
    var binding: Any? = null

    //在绑定数据时将更新这里的内容
   internal var _data: Any? = null
   internal var _pos: Int = -1


    /** 返回绑定的数据*/
    fun <T> getBindData(): T = _data as T
    fun getPos(): Int = _pos

    /** viewbinding绑定itemview，传入的viewBinding类型需要与itemview匹配 */
    inline fun <reified T : ViewBinding> withBinding(block: T.() -> Unit) {
        val b = binding?.let {
            it as T
        } ?: let {
            binding = T::class.java.getMethod("bind", View::class.java).invoke(null, itemView) as T
            binding as T
        }
        b.block()
    }

    companion object {
        fun createViewHolder(
            context: Context,
            itemView: View
        ): BaseViewHolder {
            return BaseViewHolder(context, itemView)
        }

        /**
         * Create view holder
         *
         * @param context
         * @param parent
         * @param layoutId
         * @param block
         *     可以做一些初始化的事情，例如itemview是个rv，就可以在创建vh的时候初始化rv，这样，在绑定的时候，就可以只做数据的刷新
         * @return
         * @receiver
         */
        fun createViewHolder(
            context: Context,
            parent: ViewGroup, layoutId: Int,
            block: BaseViewHolder.() -> Unit = {},
        ): BaseViewHolder {
            val itemView = LayoutInflater.from(context).inflate(
                layoutId, parent,
                false
            )
            return BaseViewHolder(context, itemView).apply {
                this.block()
            }
        }
    }


    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    fun <T : View> getView(viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = mConvertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    fun getConvertView(): View {
        return mConvertView
    }


    /** **以下为辅助方法 */
    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    fun setText(viewId: Int, text: String?): BaseViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.text = text
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): BaseViewHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageResource(resId)
        return this
    }

    fun setImageBitmap(
        viewId: Int,
        bitmap: Bitmap?
    ): BaseViewHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(
        viewId: Int,
        drawable: Drawable?
    ): BaseViewHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(
        viewId: Int,
        color: Int
    ): BaseViewHolder {
        val view = getView<View>(viewId)!!
        view.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(
        viewId: Int,
        backgroundRes: Int
    ): BaseViewHolder {
        val view = getView<View>(viewId)!!
        view.setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): BaseViewHolder {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(textColor)
        return this
    }

    fun setTextColorRes(
        viewId: Int,
        textColorRes: Int
    ): BaseViewHolder {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(mContext.resources.getColor(textColorRes))
        return this
    }

    @SuppressLint("NewApi")
    fun setAlpha(viewId: Int, value: Float): BaseViewHolder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId)!!.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId)!!.startAnimation(alpha)
        }
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): BaseViewHolder {
        val view = getView<View>(viewId)!!
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun linkify(viewId: Int): BaseViewHolder {
        val view = getView<TextView>(viewId)!!
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(
        typeface: Typeface?,
        vararg viewIds: Int
    ): BaseViewHolder {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)!!
            view.setTypeface(typeface)
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): BaseViewHolder? {
        val view = getView<ProgressBar>(viewId)!!
        view.progress = progress
        return this
    }

    fun setProgress(
        viewId: Int,
        progress: Int,
        max: Int
    ): BaseViewHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): BaseViewHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): BaseViewHolder {
        val view = getView<RatingBar>(viewId)!!
        view.rating = rating
        return this
    }

    fun setRating(
        viewId: Int,
        rating: Float,
        max: Int
    ): BaseViewHolder {
        val view = getView<RatingBar>(viewId)!!
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any?): BaseViewHolder {
        val view = getView<View>(viewId)!!
        view.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any?): BaseViewHolder {
        val view = getView<View>(viewId)!!
        view.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): BaseViewHolder {
        val view = getView<View>(viewId) as Checkable
        view.isChecked = checked
        return this
    }

    /** 关于事件的 */
    fun setOnClickListener(
        viewId: Int,
        listener: View.OnClickListener
    ): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(
        viewId: Int,
        listener: View.OnTouchListener
    ): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(
        viewId: Int,
        listener: View.OnLongClickListener
    ): BaseViewHolder {
        val view = getView<View>(viewId)
        view?.setOnLongClickListener(listener)
        return this
    }


}