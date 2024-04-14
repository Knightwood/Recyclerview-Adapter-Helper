package com.kiylx.recyclerviewneko.viewholder

import android.util.SparseArray
import com.kiylx.recyclerviewneko.myadapter.ItemClickListener
import com.kiylx.recyclerviewneko.myadapter.ItemLongClickListener

class ItemViewDelegate<T>(var layoutId: Int) {
    /** 将数据绑定到viewholder */
    internal var convert: (holder: BaseViewHolder, data: T, position: Int) -> Unit = { _, _, _ -> }

    /**
     * 例如某类型数据里有个type字段，有四个取值:1，2，3，4.代表四种viewholder
     * 那么，可以通过 type==1 这样的判断，确定此ItemViewDelegate是否显示此数据，
     * 如果返回true，即此ItemViewDelegate(也就是此viewholder)将绑定此条数据显示内容
     *
     * @param data
     * @param position
     * @return
     *     对item(也就是dateList[position])判断，如果是显示这个类型的itemview,就返回true,否则返回false
     */
    internal var isForViewType: ((data: T, position: Int) -> Boolean) = { _, _ -> true }

    /** 可以重写此方法，干预创建过程 或者在config中指代 */
    internal var createConvert: (vh: BaseViewHolder) -> Unit = {}
    //view 的id与对应的ItemClickListener
    internal val clicks: SparseArray<ItemClickListener> = SparseArray()
    internal val longClicks: SparseArray<ItemLongClickListener> = SparseArray()

    fun onCreate(createConvert: (vh: BaseViewHolder) -> Unit = {}): ItemViewDelegate<T> {
        this.createConvert = createConvert
        return this
    }

    fun onBind(convert: (holder: BaseViewHolder, data: T, position: Int) -> Unit): ItemViewDelegate<T> {
        this.convert = convert
        return this
    }

    fun isThisType(block: (data: T, position: Int) -> Boolean): ItemViewDelegate<T> {
        this.isForViewType = block
        return this
    }

    /**
     * On click
     * 添加点击事件，多次调用时：若id一样，listener不同，会覆盖掉前面设置的点击事件
     *
     * @param ids
     * @param clickEvent
     * @return
     */
    fun onClick(vararg ids: Int, clickEvent: ItemClickListener): ItemViewDelegate<T> {
        for (id in ids) {
            clicks.put(id, clickEvent)
        }
        return this
    }
    fun onLongClick(vararg ids: Int, clickEvent: ItemLongClickListener): ItemViewDelegate<T> {
        for (id in ids) {
            longClicks.put(id, clickEvent)
        }
        return this
    }

    companion object{

    }
}

data class DelegatePair<T>(val type: Int, val delegate: ItemViewDelegate<T>)

infix fun <T : Any> Int.pack(that: ItemViewDelegate<T>): DelegatePair<T> = DelegatePair(this, that)


